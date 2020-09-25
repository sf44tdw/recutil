/*
 * Copyright (C) 2016 normal
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
package recutil.excludechannel;

import static recutil.commmonutil.Util.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
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
import recutil.dbaccessor.entity.Excludechannel;
import recutil.dbaccessor.entity.TempExcludechannel;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import loggerconfigurator.LoggerConfigurator;

/**
 * BS_191 BS_192 BS_193 BS_200 BS_201 BS_202 BS_231 BS_232 BS_233 BS_234 BS_236
 * BS_238 BS_241 BS_242 BS_243 BS_244 BS_245 BS_251 BS_252 BS_255 BS_256
 */
/**
 * 転記元テーブルに除外チャンネルの登録、削除を行う。転記元テーブルの内容は番組情報更新時に転記先テーブルに転記される。
 *
 * @author normal
 */
public class Main {

	protected static final String CHANNELS_TEMP = "物理チャンネル番号 = {0}, チャンネルID = {1} ,局名 = {2}";

	private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

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

	public void start(String[] args) throws org.apache.commons.cli.ParseException {
		final Option printAllOption = Option.builder("p")
				.longOpt("printall")
				.desc("転記元テーブルに記録された除外チャンネルの情報をすべて表示する。他のオプションとは同時に指定できない。")
				.hasArg(false)
				.build();

		final Option deleteAllOption = Option.builder("d")
				.longOpt("deleteall")
				.desc("転記元テーブルに除外チャンネルの登録をすべて削除する。他のオプションとは同時に指定できない。")
				.hasArg(false)
				.build();

		final Option updateAllOption = Option.builder("u")
				.longOpt("updateall")
				.desc("転記元テーブルの除外するチャンネルの一覧を更新する。除外するチャンネルのチャンネルIDを全て指定する。既存の登録内容はすべて削除される。チャンネルテーブルに登録済みのチャンネルID以外は登録できない。他のオプションとは同時に指定できない。")
				.hasArgs()
				.type(String.class)
				.build();

		OptionGroup exOpts = new OptionGroup();
		exOpts.setRequired(true);
		exOpts.addOption(updateAllOption);
		exOpts.addOption(deleteAllOption);
		exOpts.addOption(printAllOption);

		Options opts = new Options();
		opts.addOptionGroup(exOpts);
		CommandLineParser parser = new DefaultParser();

		HelpFormatter help = new HelpFormatter();
		CommandLine cl;
		try {
			cl = parser.parse(opts, args);
		} catch (org.apache.commons.cli.ParseException ex) {
			help.printHelp("使用法", opts);
			throw ex;
		}

		final boolean printAll;
		if (cl.hasOption(printAllOption.getOpt())) {
			printAll = true;
		} else {
			printAll = false;
		}

		final boolean deleteAll;
		if (cl.hasOption(deleteAllOption.getOpt())) {
			deleteAll = true;
		} else {
			deleteAll = false;
		}

		final List<String> channelIds = new ArrayList<>();
		if (cl.hasOption(updateAllOption.getOpt())) {
			channelIds.addAll(Arrays.asList(cl.getOptionValues(updateAllOption.getOpt())));
		}

		try (EntityManagerMaker emm = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
			final EntityManager em = emm.getEntityManager();

			if (printAll == true) {
				List<Channel> chs = em.createQuery(
						"select t1 from Channel t1, TempExcludechannel t2 where t1.channelId = t2.channelId",
						Channel.class).getResultList();
				MessageFormat format = new MessageFormat(CHANNELS_TEMP);
				for (Channel ch : chs) {
					Object[] parameters = { ch.getChannelNo(), ch.getChannelId(), ch.getDisplayName() };
					System.out.println(format.format(parameters));
				}
			} else {
				EntityTransaction trans = em.getTransaction();
				trans.begin();
				LOG.info("トランザクション開始。");
				final TypedQuery<Excludechannel> ql_echs = em.createNamedQuery("Excludechannel.findAll",
						Excludechannel.class);
				ql_echs.setLockMode(LockModeType.PESSIMISTIC_READ);
				List<Excludechannel> lock_ech = null;
				lock_ech = ql_echs.getResultList();
				if (Objects.nonNull(lock_ech)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("除外チャンネルテーブルをロック。");
					}
					TRANSACTION: {
						final TypedQuery<TempExcludechannel> ql_del;
						LOG.info("現在の除外登録を全て削除します。");
						ql_del = em.createNamedQuery("TempExcludechannel.deleteAll", TempExcludechannel.class);
						ql_del.executeUpdate();
						LOG.info("現在の除外登録を全て削除しました。");
						if (deleteAll == true) {
							LOG.info("削除のみ実施。");
							break TRANSACTION;
						}
						if (channelIds.size() > 0) {
							LOG.info("除外チャンネル登録開始。");
							for (String s : channelIds) {
								final TempExcludechannel ech = new TempExcludechannel(s);
								em.persist(ech);
								LOG.info("チャンネルID = " + ech.getChannelId() + "を除外登録しました。");
							}
							LOG.info("除外チャンネル登録完了。");
						} else {
							LOG.info("除外チャンネルID指定なし。");
						}
					}
					trans.commit();
					LOG.info("トランザクションコミット。");
				}else {
					trans.rollback();
					LOG.info("トランザクションロールバック。");
				}
			}

			em.close();
		}
	}
}
