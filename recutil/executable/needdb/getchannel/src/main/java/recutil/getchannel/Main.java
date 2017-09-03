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
package recutil.getchannel;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.comparator.ChannelComparator_AscendingByChannelNo;
import recutil.dbaccessor.manager.EntityManagerMaker;
import static recutil.dbaccessor.query.QueryString.Channel.ALL_USEABLE_CHANNEL;
import static recutil.dbaccessor.query.QueryString.Channel.PARAMNAME_CHANNEL_NO;
import static recutil.dbaccessor.query.QueryString.Channel.USEABLE_CHANNEL_BY_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Channel.USEABLE_CHANNEL_BY_CHANNEL_NO;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class Main {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    private static enum excludeState {
        ALL, USEABLE
    };

    private static enum channelState {
        ALL, BY_NO, BY_ID
    };
    protected static final String OUTPUT_FORMAT = "{0,number,#}/{1,number,#} ,物理チャンネル番号 = {2,number,#}, チャンネルID = {3} ,局名 = {4}";

    public static final String getSep() {
        return System.getProperty("line.separator");
    }

    private static String dumpArgs(String[] args) {
        return ArrayUtils.toString(args, "引数なし。");
    }

    public static void main(String[] args) {
        try {
            new Main().start(args);
            System.exit(0);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + dumpArgs(args), ex);
            System.exit(1);
        }
    }

    public void start(String[] args) throws org.apache.commons.cli.ParseException {

        final Option withExcludeChannelOption = Option.builder("e")
                .required(false)
                .longOpt("withoutexcludechannel")
                .desc("除外設定されたチャンネルは無視する。省略すると、除外設定されたチャンネルも検索の対象とする。")
                .hasArg(false)
                .build();

        final Option channelNumberOption = Option.builder("n")
                .longOpt("channelNumber")
                .required(true)
                .desc("チャンネル番号。省略すると、登録されている全てのチャンネル情報を表示する。チャンネルIDとは併用不可。")
                .hasArg()
                .type(Integer.class)
                .build();

        final Option channelIdOption = Option.builder("i")
                .longOpt("channelid")
                .required(true)
                .desc("チャンネルID。省略すると、登録されている全てのチャンネル情報を表示する。チャンネル番号とは併用不可。")
                .hasArg()
                .type(String.class)
                .build();

        final OptionGroup channelOpts = new OptionGroup();
        channelOpts.setRequired(false);
        channelOpts.addOption(channelNumberOption);
        channelOpts.addOption(channelIdOption);

        final Options opts = new Options();
        opts.addOption(withExcludeChannelOption);
        opts.addOptionGroup(channelOpts);
        CommandLineParser parser = new DefaultParser();

        HelpFormatter help = new HelpFormatter();
        CommandLine cl;
        try {
            cl = parser.parse(opts, args);
        } catch (org.apache.commons.cli.ParseException ex) {
            help.printHelp("使用法", opts);
            throw ex;
        }

        final excludeState exclude;
        if (cl.hasOption(withExcludeChannelOption.getOpt())) {
            exclude = excludeState.USEABLE;
        } else {
            exclude = excludeState.ALL;
        }

        final Integer channelNo;
        if (cl.hasOption(channelNumberOption.getOpt())) {
            channelNo = Integer.valueOf(cl.getOptionValue(channelNumberOption.getOpt()));
        } else {
            channelNo = null;
        }

        final String channelId;
        if (cl.hasOption(channelIdOption.getOpt())) {
            channelId = cl.getOptionValue(channelIdOption.getOpt());
        } else {
            channelId = null;
        }

        final channelState chState;
        if (channelNo != null) {
            chState = channelState.BY_NO;
        } else if (channelId != null || "".equals(channelId)) {
            chState = channelState.BY_ID;
        } else {
            chState = channelState.ALL;
        }

        try (EntityManagerMaker mk = new EntityManagerMaker()) {
            EntityManager man = mk.getEntityManager();

            final TypedQuery<Channel> ql;
            if (exclude == excludeState.USEABLE) {
                switch (chState) {
                    case BY_NO:
                        ql = man.createQuery(USEABLE_CHANNEL_BY_CHANNEL_NO, Channel.class);
                        ql.setParameter(PARAMNAME_CHANNEL_NO, channelNo);
                        break;
                    case BY_ID:
                        ql = man.createQuery(USEABLE_CHANNEL_BY_CHANNEL_ID, Channel.class);
                        ql.setParameter(PARAMNAME_CHANNEL_ID, channelId);
                        break;
                    case ALL:
                        ql = man.createQuery(ALL_USEABLE_CHANNEL, Channel.class);
                        break;
                    default:
                        System.out.println("チャンネル検索条件が設定できません。");
                        ql = null;
                }
            } else {
                switch (chState) {
                    case BY_NO:
                        ql = man.createNamedQuery("Channel.findByChannelNo", Channel.class);
                        ql.setParameter(PARAMNAME_CHANNEL_NO, channelNo);
                        break;
                    case BY_ID:
                        ql = man.createNamedQuery("Channel.findByChannelId", Channel.class);
                        ql.setParameter(PARAMNAME_CHANNEL_ID, channelId);
                        break;
                    case ALL:
                        ql = man.createNamedQuery("Channel.findAll", Channel.class);
                        break;
                    default:
                        System.out.println("チャンネル検索条件が設定できません。");
                        ql = null;
                }
            }

            if (ql != null) {
                final List<Channel> table = ql.getResultList();

                Collections.sort(table, new ChannelComparator_AscendingByChannelNo());

                final MessageFormat format = new MessageFormat(OUTPUT_FORMAT);

                int count = 0;
                for (Channel ch : table) {
                    count++;
                    Object[] parameters = {count, table.size(), ch.getChannelNo(), ch.getChannelId(), ch.getDisplayName()};
                    System.out.println(format.format(parameters));
                }
            }
            man.close();
        }
    }

}
