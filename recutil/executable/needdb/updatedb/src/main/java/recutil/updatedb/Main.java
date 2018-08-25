/*
 * Copyright (C) 2017 normal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package recutil.updatedb;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import static recutil.commmonutil.Util.getDefaultLineSeparator;
import recutil.dbaccessor.entity.Excludechannel;
import recutil.dbaccessor.entity.TempExcludechannel;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.dataextractor.channel.AllChannelDataExtractor;
import recutil.updatedb.dataextractor.channel.ChannelData;
import recutil.updatedb.dataextractor.programme.AllProgrammeDataExtractor;
import recutil.updatedb.dataextractor.programme.ProgrammeData;
import recutil.updatedb.jpa.entitymaker.EntityMaker;
import recutil.updatedb.listmaker.EpgListMaker;

/**
 *
 * @author normal
 */
public class Main {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public static final String getSep() {
        return getDefaultLineSeparator();
    }

    private static String dumpArgs(String[] args) {
        return ArrayUtils.toString(args, "引数なし。");
    }

    public static void main(String[] args) {
        try {
            SelectedPersistenceName.selectPersistence(PERSISTENCE.PRODUCT);
            new Main().start(args);
            System.exit(0);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + dumpArgs(args), ex);
            System.exit(1);
        }
    }

    protected void deleteAll(EntityManager manager) {

        LOG.info("現在の除外チャンネル登録を全て削除します。");
        final Query ex_del;
        ex_del = manager.createNamedQuery("Excludechannel.deleteAll", recutil.dbaccessor.entity.Programme.class);
        ex_del.executeUpdate();
        LOG.info("現在の除外チャンネル登録を全て削除しました。");

        LOG.info("現在の番組登録を全て削除します。");
        final Query pg_del;
        pg_del = manager.createNamedQuery("Programme.deleteAll", recutil.dbaccessor.entity.Programme.class);
        pg_del.executeUpdate();
        LOG.info("現在の番組登録を全て削除しました。");

        LOG.info("現在のチャンネル登録を全て削除します。");
        final Query ch_del;
        ch_del = manager.createNamedQuery("Channel.deleteAll", recutil.dbaccessor.entity.Channel.class);
        ch_del.executeUpdate();
        LOG.info("現在のチャンネル登録を全て削除しました。");
    }

    protected void start(String[] args) throws org.apache.commons.cli.ParseException {

        final Option targetDirectoryOption = Option.builder("d")
                .required(true)
                .longOpt("directory")
                .desc("epgdumpが生成したEPGのファイルを捜索するディレクトリ。サブディレクトリは捜索しない。")
                .hasArg(true)
                .type(String.class)
                .build();

        final Option charSetOption = Option.builder("c")
                .required(false)
                .longOpt("charset")
                .desc("EPGファイル読み込み時の文字コード。指定されていない場合はシステム既定の文字コードを使用する。")
                .hasArg(true)
                .type(String.class)
                .build();

        Options opts = new Options();
        opts.addOption(targetDirectoryOption);
        opts.addOption(charSetOption);
        CommandLineParser parser = new DefaultParser();

        HelpFormatter help = new HelpFormatter();
        CommandLine cl;
        try {
            cl = parser.parse(opts, args);
        } catch (org.apache.commons.cli.ParseException ex) {
            help.printHelp("epgdumpで生成したXMLファイルをDBに取り込む。", opts);
            throw ex;
        }
        final File targetDir;
        if (cl.hasOption(targetDirectoryOption.getOpt())) {
            targetDir = new File(cl.getOptionValue(targetDirectoryOption.getOpt()));
        } else {
            targetDir = null;
        }

        if (targetDir == null) {
            throw new Error("ファイルを捜索するディレクトリが指定されていません。");
        }

        if (!targetDir.isDirectory()) {
            throw new Error(targetDir.getAbsolutePath() + " は、ディレクトリではありません。");
        }

        final String csnStr;
        final Charset charset;
        Charset temp = null;
        if (cl.hasOption(charSetOption.getOpt())) {
            csnStr = cl.getOptionValue(charSetOption.getOpt());
        } else {
            csnStr = null;
        }

        if (csnStr != null) {
            temp = Charset.forName(csnStr);
        } else {
            temp = Charset.defaultCharset();
            LOG.info("文字コードの指定がありません。デフォルトの文字コードを使用します。");
        }
        charset = temp;
        LOG.info("文字コードを設定しました。文字コード = {}", charset);

        EpgListMaker elm = new EpgListMaker(targetDir, charset);

        List<ChannelData> chs = new AllChannelDataExtractor(elm.getEpgList()).getAllEPGRecords();

        List<ProgrammeData> ps = new AllProgrammeDataExtractor(elm.getEpgList()).getAllEPGRecords();

        EntityMaker em = new EntityMaker(chs, ps);

        em.makeEntities();

        try (EntityManagerMaker emm = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            final EntityManager manager = emm.getEntityManager();

            final EntityTransaction trans = manager.getTransaction();
            trans.begin();
            LOG.info("番組登録トランザクション開始。");

            this.deleteAll(manager);

            LOG.info("チャンネル登録開始。");
            for (recutil.dbaccessor.entity.Channel ch : em.getChannelEntities()) {
                manager.persist(ch);
                LOG.info("チャンネル登録 = {}", ToStringBuilder.reflectionToString(ch));
            }
            LOG.info("チャンネル登録開完了。");

            LOG.info("番組登録開始。");
            for (recutil.dbaccessor.entity.Programme pg : em.getProgrammeEntities()) {
                manager.persist(pg);
                LOG.info("番組登録 = {}", ToStringBuilder.reflectionToString(pg));
            }
            LOG.info("番組登録完了。");
            trans.commit();
            LOG.info("番組登録トランザクションコミット。");
            trans.begin();
            LOG.info("除外チャンネル登録転記トランザクション開始。");
            final TypedQuery<TempExcludechannel> ql = manager.createNamedQuery("TempExcludechannel.findAll", TempExcludechannel.class);
            final List<TempExcludechannel> res = ql.getResultList();
            for (TempExcludechannel tech : res) {
                manager.persist(new Excludechannel(tech.getChannelId()));
                LOG.info("転記した除外登録チャンネルID = {}", tech.getChannelId());
            }
            trans.commit();
            LOG.info("除外チャンネル登録転記トランザクションコミット。");
            manager.close();

        }

    }
}
