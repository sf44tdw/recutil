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
package recutil.executerecordcommand;

import static recutil.commmonutil.Util.*;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.commandexecutor.CommandExecutor;
import recutil.commandexecutor.CommandResult;
import recutil.commandexecutor.Executor;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import recutil.timeclioption.TimeCliOption;
import recutil.timeclioption.TimeParseException;

/**
 * 番組名自動入力機能付きのrecpt1ラッパー。
 *
 * 実行時刻とチャンネルIDから番組名、 チャンネルIDからチャンネル番号を確認し、 recpt1コマンドを実行する。
 *
 * これが実行されてから指定の秒数以内に始まる番組のうち、最近のものを選択して録画ファイルのファイル名の一部とする。
 *
 * ファイルは基本的に実行者のホームディレクトリに作成される。
 *
 * @author normal
 */
public class Main {

	private static final Logger LOG = LoggerConfigurator.getCallerLogger();

	/**
	 * 番組情報を取得するクエリ。(チャンネル情報は自動的についてくる。) 特定チャンネルIDの、放送開始日時が指定時間の範囲内にある番組情報を取得する。
	 * 放送開始日時昇順ソートで取得する。
	 */
	protected static final String GET_PROGRAMME_QUERY = "SELECT p FROM Programme p WHERE (p.channelId.channelId = :channelId) AND (p.startDatetime BETWEEN :startDatetime AND :startDatetimePlusRange) ORDER BY p.startDatetime";

	private static final Range<Long> RANGE_LIMIT_SECOND = Range.between(0L, 120L);

	//toStringだとカンマとかが入るので、形式を指定。
	protected static final MessageFormat LONG_TO_STRING = new MessageFormat("{0,number,#}");

	//Date型の値は内部ではlongにより保持されているが、recpt1の録画時間はint型のため、制限をかける。
	private static final long MAX_INTEGER = Integer.MAX_VALUE;
	public static final Range<Long> MAX_INTEGER_RANGE = Range.between(0L, MAX_INTEGER);

	protected static final String DATE_PATTERN = "yyyyMMddHHmmss";

	/**
	 * ファイル名フォーマット
	 */
	protected static final MessageFormat FILENAME_FORMAT = new MessageFormat("{4}_I{0}_C{1}_D{2}_P{3}.ts");

	/**
	 * 録画コマンド
	 */
	protected static final String RECORDCOMMAND = "recpt1";
	/**
	 * 録画コマンドのオプション
	 */
	protected static final String STRIP_OPTION = "--strip";
	/**
	 * 録画コマンドのオプション
	 */
	protected static final String B25_OPTION = "--b25";

	public static final String getSep() {
		return getDefaultLineSeparator();
	}

	private static String dumpArgs(String[] args) {
		return ArrayUtils.toString(args, "引数なし。");
	}

	public static void main(String[] args) {
		try {
			//ファイル名用に自身のプロセスIDを取得。
			final String PID = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			SelectedPersistenceName.selectPersistence(PERSISTENCE.PRODUCT);
			//実行時刻(秒単位切り捨て)
			final Date nowTime;
			ZonedDateTime d = ZonedDateTime.now();
			ZonedDateTime d2 = d.truncatedTo(ChronoUnit.MINUTES);
			nowTime = Date.from(d2.toInstant());
			new Main().start(new Executor(), PID, nowTime, args);
			System.exit(0);
		} catch (Throwable ex) {
			String s = "エラー。 引数 = " + dumpArgs(args);
			System.out.println(s);
			System.out.println(ex);
			LOG.error(s, ex);
			System.exit(1);
		}
	}

	public void start(final CommandExecutor exec, final String pid, final Date nowTime, final String[] args)
			throws ParseException, IOException, InterruptedException, TimeParseException {

		if (LOG.isDebugEnabled()) {
			LOG.debug("引数 = " + dumpArgs(args));
		}

		final TimeCliOption timeOpt = new TimeCliOption(
				"録画時間オプション。ほかの録画時間オプションとは同時に使用できない。録画時間を秒単位で指定する。",
				"録画時間オプション。ほかの録画時間オプションとは同時に使用できない。録画時間を分単位で指定する。",
				"録画時間オプション。ほかの録画時間オプションとは同時に使用できない。録画時間を時間単位で指定する。");

		final Option ChannelIdOption = Option.builder("i")
				.longOpt("channelid")
				.required(true)
				.desc("チャンネルIDオプション。指定されたチャンネルIDの番組を録画する。")
				.hasArg(true)
				.type(String.class)
				.build();

		final OptionGroup durationOptionGroup = timeOpt.getTimeOptionGroup(true);

		final Option rangenOption = Option.builder("r")
				.longOpt("range")
				.required(false)
				.desc("放送開始日時範囲オプション。実行時刻を起点として、放送開始日時として検索する時間の範囲を秒単位で指定する。最大120秒。省略した場合、120秒とみなす。")
				.hasArg(true)
				.type(Long.class)
				.build();

		final Option destinationOption = Option.builder("d")
				.longOpt("destinationdirectory")
				.required(false)
				.desc("ファイル出力先のディレクトリを絶対パスで指定する。相対パスを指定した場合の結果は保証しない。省略された場合はホームディレクトリを使用する。")
				.hasArg(true)
				.type(String.class)
				.build();

		final Option recpt1PathOption = Option.builder("p")
				.longOpt("recordcommanddirectory")
				.required(false)
				.desc("recpt1のあるディレクトリを絶対パスで指定する。相対パスを指定した場合の結果は保証しない。省略された場合は実行時にコマンドをフルパスで指定しない。")
				.hasArg(true)
				.type(String.class)
				.build();

		Options opts = new Options();
		opts.addOption(ChannelIdOption);
		opts.addOptionGroup(durationOptionGroup);
		opts.addOption(rangenOption);
		opts.addOption(destinationOption);
		opts.addOption(recpt1PathOption);

		CommandLineParser parser = new DefaultParser();

		HelpFormatter help = new HelpFormatter();

		CommandLine cl;
		try {
			cl = parser.parse(opts, args);
		} catch (org.apache.commons.cli.ParseException ex) {
			help.printHelp("放送開始時刻とチャンネルIDから番組名、 チャンネルIDからチャンネル番号を確認し、 recpt1コマンドを実行する。" + getSep()
					+ "実行されてから指定の秒数以内に始まる番組のうち、最近のものをファイル名の番組名部分用に選択する。見つからない場合は空欄。" + getSep()
					+ "ファイル名=番組名(あれば)、チャンネルID、チャンネル番号、録画開始日時、このプロセスのPIDを連結したもの。" + getSep(), opts);
			System.out.println(ex);
			throw ex;
		}

		final String channelId;
		if (cl.hasOption(ChannelIdOption.getOpt())) {
			channelId = cl.getOptionValue(ChannelIdOption.getOpt());
		} else {
			//オプション未設定の場合。必須オプションなのでここには来ないはず。
			channelId = null;
		}
		if (channelId == null || "".equals(channelId)) {
			final String s = "チャンネルIDが設定されていません。";
			throw new IllegalArgumentException(s);
		}

		final long duration_second;

		duration_second = timeOpt.getValueBySecond(cl);
		if (!MAX_INTEGER_RANGE.contains(duration_second)) {
			throw new IllegalArgumentException("録画時間が大きすぎます。 下限:0 上限:" + Integer.MAX_VALUE);
		}

		final Long range;
		if (cl.hasOption(rangenOption.getOpt())) {
			range = Long.valueOf(cl.getOptionValue(rangenOption.getOpt()));
		} else {
			//オプション未設定の場合。
			LOG.info("放送開始日時範囲が設定されていません。最大値とします。");
			range = 120L;
		}
		if (!Main.RANGE_LIMIT_SECOND.contains(range)) {
			final String s = "放送開始日時範囲が正しくありません。0より小さいか、上限を超えています。値 = " + cl.getOptionValue(rangenOption.getOpt());
			throw new IllegalArgumentException(s);
		}

		final String optVal_d;
		final File destDirPath;
		if (cl.hasOption(destinationOption.getOpt())) {
			optVal_d = cl.getOptionValue(destinationOption.getOpt());
			destDirPath = relativePahtToAbsolutePath(optVal_d);

		} else {
			optVal_d = "NOT_DEFINED";
			destDirPath = new File(System.getProperty("user.home"));
		}
		if (destDirPath == null || !destDirPath.isDirectory() || !destDirPath.canWrite()) {
			final String s = "保存先ディレクトリが見つからないか、使用できません。値 = " + optVal_d;
			throw new IllegalArgumentException(s);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("保存先ディレクトリ{}", destDirPath.getAbsolutePath());
		}

		final String optVal_p;
		final File recpt1Path;
		if (cl.hasOption(recpt1PathOption.getOpt())) {
			optVal_p = cl.getOptionValue(recpt1PathOption.getOpt());
			recpt1Path = relativePahtToAbsolutePath(optVal_p);
			if (recpt1Path == null || !recpt1Path.isDirectory() || !recpt1Path.canRead()) {
				final String s = "recpt1格納先のディレクトリが見つからないか、使用できません。値 = " + optVal_p;
				throw new IllegalArgumentException(s);
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("recpt1格納先ディレクトリ{}", recpt1Path.getAbsolutePath());
			}
		} else {
			optVal_p = "NOT_DEFINED";
			recpt1Path = null;
		}

		final String recpt1Command;
		if (recpt1Path != null) {
			final File recpt1Dest = new File(recpt1Path, RECORDCOMMAND);
			if (!recpt1Dest.isFile() || !recpt1Dest.canExecute()) {
				final String s = "recpt1が見つからないか、実行できません。パス = " + recpt1Dest.getAbsolutePath();
				throw new IllegalArgumentException(s);
			}
			recpt1Command = recpt1Dest.getAbsolutePath();
			if (LOG.isDebugEnabled()) {
				LOG.debug("recpt1フルパス{}", recpt1Command);
			}
		} else {
			recpt1Command = RECORDCOMMAND;
		}

		//放送開始日時範囲末尾
		final Date rangeDate = new Date(nowTime.getTime() + (range * 1000));

		final RecordParameter param;
		try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
			EntityManager man = mk.getEntityManager();
			final TypedQuery<Programme> ql_p;
			ql_p = man.createQuery(GET_PROGRAMME_QUERY, Programme.class);
			ql_p.setParameter("channelId", channelId);
			ql_p.setParameter("startDatetime", nowTime);
			ql_p.setParameter("startDatetimePlusRange", rangeDate);
			final List<Programme> table_p = ql_p.getResultList();

			if (LOG.isDebugEnabled()) {
				StringBuilder s = new StringBuilder();
				s.append("取得された番組情報****************************************").append(getSep());
				for (Programme _p : table_p) {
					s.append(ReflectionToStringBuilder.toString(_p)).append(getSep());
				}
				s.append("取得された番組情報****************************************").append(getSep());
				LOG.debug(s.toString());
			}

			final Programme p;
			//番組が取得できたら
			if (!table_p.isEmpty()) {
				//その1件目を取得する。
				p = table_p.get(0);
			} else {
				p = null;
			}

			final List<Channel> table_c;
			//番組が取得できない場合は
			if (p == null) {
				//チャンネルを取得する。
				final TypedQuery<Channel> ql_c;
				ql_c = man.createNamedQuery("Channel.findByChannelId", Channel.class);
				ql_c.setParameter("channelId", channelId);
				table_c = ql_c.getResultList();
			} else {
				table_c = new ArrayList<>();
			}

			if (LOG.isDebugEnabled()) {
				StringBuilder s = new StringBuilder();
				s.append("取得されたチャンネル情報****************************************").append(getSep());
				for (Channel _c : table_c) {
					s.append(ReflectionToStringBuilder.toString(_c)).append(getSep());
				}
				s.append("取得されたチャンネル情報****************************************").append(getSep());
				LOG.debug(s.toString());
			}

			final Channel c;
			if (!table_c.isEmpty()) {
				c = table_c.get(0);
			} else {
				c = null;
			}

			man.close();

			if (c != null || p != null) {
			} else {
				throw new IllegalArgumentException("チャンネル情報、番組情報とも取得できませんでした。");
			}

			final Object[] params;
			if (p != null) {

				params = new Object[] { p.getChannelId().getChannelId(), p.getChannelId().getChannelNo(),
						new SimpleDateFormat(DATE_PATTERN).format(nowTime), pid, p.getTitle() };

				String ss = Main.FILENAME_FORMAT.format(params);

				param = new RecordParameter(p.getChannelId().getChannelNo(), duration_second,
						new File(destDirPath, ss).getAbsolutePath());
			} else if (c != null) {
				params = new Object[] { c.getChannelId(), c.getChannelNo(),
						new SimpleDateFormat(DATE_PATTERN).format(nowTime), pid, "", };
				param = new RecordParameter(c.getChannelNo(), duration_second,
						new File(destDirPath, Main.FILENAME_FORMAT.format(params)).getAbsolutePath());
			} else {
				param = null;
			}

			if (LOG.isDebugEnabled()) {
				LOG.debug("コマンド用情報 = " + param.toString());
			}

			CommandResult res = exec.execCommand(recpt1Command, STRIP_OPTION, B25_OPTION,
					LONG_TO_STRING.format(new Object[] { param.getPhysicalChannelNumber() }),
					LONG_TO_STRING.format(new Object[] { param.getDuration() }), param.getFileName());

			LOG.info(res.toString());

			System.out.println(res);

		}

	}

}
