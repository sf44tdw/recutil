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
package recutil.getprogramme;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import static recutil.commmonutil.Util.getDefaultLineSeparator;
import static recutil.commmonutil.Util.parseDateToString;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.entity.comparator.PrograammeListSorter;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import recutil.dbaccessor.query.QueryString;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Programme.PARAMNAME_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.PARAMNAME_START_DATETIME;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.timeclioption.TimeCliOption;
import recutil.timeclioption.TimeParseException;

/**
 *
 * @author normal
 */
public class Main {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    protected static final String DATETIME_FORMAT = recutil.commmonutil.Util.getDbDatePattern();

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

    protected static enum OUTPUT_FORMAT_TYPE {
        DEFAULT("{0}/{1} ,チャンネルID = {2} ,物理チャンネル番号 = {3,number,#} ,番組ID = {4,number,#} ,放送開始日時 = {5} ,放送終了日時 = {6} ,番組名 = {7}"),
        TITLE_ONLY("{0}");

        private final String formatString;

        private OUTPUT_FORMAT_TYPE(String formatString) {
            this.formatString = formatString;
        }

        public String getFormatString() {
            return formatString;
        }

        public MessageFormat getFormat() {
            return new MessageFormat(formatString);
        }
    }

    protected static enum firstOnlyState {
        ALL, FIRST_ONLY;
    };

    private static enum excludeState {
        ALL, USEABLE;
    };

    private static enum paramState {
        NONE, EVENT_ID, START_DATETIME, START_DATETIME_BETWEEN;
    };

    protected static String printRes(List<Programme> target, OUTPUT_FORMAT_TYPE format, firstOnlyState firstOnly) {
        StringBuilder sb = new StringBuilder();
        final int records = target.size();
        int record = 1;
        for (Programme prg : target) {
            final Object[] parameters;
            switch (format) {
                case DEFAULT:
                    Channel id = prg.getChannelId();
                    parameters = new Object[]{record, records, id.getChannelId(), id.getChannelNo(), prg.getEventId(), parseDateToString(prg.getStartDatetime()), parseDateToString(prg.getStopDatetime()), prg.getTitle()};
                    break;
                case TITLE_ONLY:
                    parameters = new Object[]{prg.getTitle()};
                    break;
                default:
                    parameters = null;
            }
            sb.append(format.getFormat().format(parameters)).append(getSep());
            if (firstOnly == firstOnlyState.FIRST_ONLY && record == 1) {
                break;
            }
            record++;
        }
        return (sb.toString());
    }

    private Long getDuration(final CommandLine cl, Option option) {
        final Long duration;
        if (cl.hasOption(option.getOpt())) {
            String val = cl.getOptionValue(option.getOpt());
            if (LOG.isDebugEnabled()) {
                LOG.debug("オプション値{} -> ", val);
            }
            duration = Long.valueOf(val);
            if (LOG.isDebugEnabled()) {
                LOG.debug("-> 数値{}", duration);
            }
        } else {
            duration = null;
        }
        return duration;
    }

    public void start(String[] args) throws org.apache.commons.cli.ParseException {

        final TimeCliOption timeOpts = new TimeCliOption(
                "検索時間の範囲オプション。startdatetimeが指定されている場合のみ評価される。指定された放送開始日時の前後の検索範囲を秒単位で指定する。",
                "検索時間の範囲オプション。startdatetimeが指定されている場合のみ評価される。指定された放送開始日時の前後の検索範囲を分単位で指定する。",
                "検索時間の範囲オプション。startdatetimeが指定されている場合のみ評価される。指定された放送開始日時の前後の検索範囲を時間単位で指定する。"
        );

        final Option titleOnlyOption = Option.builder("t")
                .required(false)
                .longOpt("titleonly")
                .desc("番組のタイトルだけを表示する。")
                .hasArg(false)
                .build();

        final Option firstOnlyOption = Option.builder("f")
                .required(false)
                .longOpt("firstonly")
                .desc("結果のうち、1件目だけを表示する。")
                .hasArg(false)
                .build();

        final Option withExcludeChannelOption = Option.builder("e")
                .required(false)
                .longOpt("withoutexcludechannel")
                .desc("除外設定されたチャンネルは無視する。省略すると、除外設定されたチャンネルも検索の対象とする。")
                .hasArg(false)
                .build();

        final Option ChannelIdOption = Option.builder("i")
                .longOpt("channelid")
                .required(false)
                .desc("チャンネルIDオプション。指定されたチャンネルIDの番組情報を検索する。このオプションが無い場合は、全てのチャンネルIDを指定したことになる。")
                .hasArg(true)
                .type(String.class)
                .build();

        final Option eventIdOption = Option.builder("v")
                .longOpt("eventid")
                .required(true)
                .desc("検索条件オプション。ほかの検索条件オプションと同時には使用できない。指定された番組IDの番組を検索する。このオプションが無い場合は、全ての番組IDを指定したことになる。")
                .hasArg()
                .type(Long.class)
                .build();

        final Option startDateTimeOption = Option.builder("d")
                .longOpt("startdatetime")
                .required(true)
                .desc("検索条件オプション。ほかの検索条件オプションと同時には使用できない。指定された放送開始日時の番組を検索する。このオプションが無い場合は、全ての放送開始日時を指定したことになる。" + getSep()
                        + "形式 = " + DATETIME_FORMAT + getSep())
                .hasArg()
                .type(Date.class)
                .build();

        OptionGroup searchOpts = new OptionGroup();
        searchOpts.setRequired(false);
        searchOpts.addOption(eventIdOption);
        searchOpts.addOption(startDateTimeOption);

        OptionGroup searchRangeOpts = timeOpts.getTimeOptionGroup(false);

        Options opts = new Options();
        opts.addOption(titleOnlyOption);
        opts.addOption(firstOnlyOption);
        opts.addOption(withExcludeChannelOption);
        opts.addOption(ChannelIdOption);
        opts.addOptionGroup(searchOpts);
        opts.addOptionGroup(searchRangeOpts);

        CommandLineParser parser = new DefaultParser();

        HelpFormatter help = new HelpFormatter();

        String[] args_;
        if (args == null || args.length < 0) {
            args_ = new String[]{};
        } else {
            args_ = args;
        }

        CommandLine cl;
        try {
            cl = parser.parse(opts, args_);

        } catch (org.apache.commons.cli.ParseException ex) {
            help.printHelp("番組情報を検索する。番組情報は、 番組ID昇順、チャンネルID昇順、放送開始日時昇順でソートして表示する。", opts);
            LOG.warn("解釈不能なオプション。 {}", ReflectionToStringBuilder.toString(args), ex);
            throw ex;
        }
        final OUTPUT_FORMAT_TYPE format;
        if (cl.hasOption(titleOnlyOption.getOpt())) {
            format = OUTPUT_FORMAT_TYPE.TITLE_ONLY;
        } else {
            format = OUTPUT_FORMAT_TYPE.DEFAULT;
        }

        final firstOnlyState firstOnly;
        if (cl.hasOption(firstOnlyOption.getOpt())) {
            firstOnly = firstOnlyState.FIRST_ONLY;
        } else {
            firstOnly = firstOnlyState.ALL;
        }

        final excludeState exclude;
        if (cl.hasOption(withExcludeChannelOption.getOpt())) {
            exclude = excludeState.USEABLE;
        } else {
            exclude = excludeState.ALL;
        }

        final String channelId;
        if (cl.hasOption(ChannelIdOption.getOpt())) {
            channelId = cl.getOptionValue(ChannelIdOption.getOpt());
        } else {
            channelId = null;
        }

        final Long eventId;
        if (cl.hasOption(eventIdOption.getOpt())) {
            eventId = Long.parseLong(cl.getOptionValue(eventIdOption.getOpt()));
        } else {
            eventId = null;
        }
        final Date startDateTime;
        if (cl.hasOption(startDateTimeOption.getOpt())) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            try {
                String s = cl.getOptionValue(startDateTimeOption.getOpt());
                startDateTime = sdf.parse(s);
            } catch (java.text.ParseException ex) {
                final String s = "放送開始日時を取得できませんでした。";
                throw new IllegalArgumentException(s, ex);
            }
        } else {
            startDateTime = null;
        }

        final Date startDateTimeRange_s;
        final Date startDateTimeRange_e;
        if (startDateTime == null) {
            startDateTimeRange_s = null;
            startDateTimeRange_e = null;
        } else {
            Date t_startDateTimeRange_s;
            Date t_startDateTimeRange_e;
            try {
                final Long rangeValue = timeOpts.getValueBySecond(cl);
                t_startDateTimeRange_s = new Date(startDateTime.getTime() - (rangeValue * 1000));
                t_startDateTimeRange_e = new Date(startDateTime.getTime() + (rangeValue * 1000));
            } catch (TimeParseException e) {
                t_startDateTimeRange_s = null;
                t_startDateTimeRange_e = null;
                LOG.warn("", e);
            }
            startDateTimeRange_s = t_startDateTimeRange_s;
            startDateTimeRange_e = t_startDateTimeRange_e;
        }

        final paramState pState;
        if (eventId != null) {
            pState = paramState.EVENT_ID;
        } else if (startDateTime != null) {
            if (startDateTimeRange_s == null && startDateTimeRange_e == null) {
                pState = paramState.START_DATETIME;
            } else {
                pState = paramState.START_DATETIME_BETWEEN;
            }
        } else {
            pState = paramState.NONE;
        }

        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();

            final EntityTransaction trans = man.getTransaction();
            trans.begin();

            final CriteriaBuilder builder = man.getCriteriaBuilder();
            final CriteriaQuery<Programme> query = builder.createQuery(Programme.class);
            final Root<Programme> root = query.from(Programme.class);

            final List<Predicate> where = new ArrayList<>();

            final TypedQuery<Programme> ql;

            //除外チャンネルテーブルをサブクエリで取る方法が不明なので、別々に持ってきて突合せる。
            //一応方法はあるが、チャンネルテーブルの時と同様に除外テーブルのサブクエリと突き合せようとしたところ、生成されたSQLに問題があった。(番組テーブルのチャンネルIDをチャンネルテーブル経由で参照しようとしている。)
            //SELECT t0.ID, t0.EVENT_ID, t0.START_DATETIME, t0.STOP_DATETIME, t0.TITLE, t0.CHANNEL_ID FROM EPG_TEST.PROGRAMME t0 WHERE NOT EXISTS (SELECT t1.CHANNEL_ID FROM EPG_TEST.EXCLUDECHANNEL t1 WHERE (t0.CHANNEL_ID = t1.CHANNEL_ID.CHANNEL.CHANNEL_ID)) 
            if (exclude == excludeState.USEABLE) {
                //除外チャンネルテーブルの内容をとってくる。

                List<String> table_ex;
                table_ex = new QueryString(man).getExcludeChannelList();

                //突合せ条件に加える。
                Expression<String> exp = root.get(PARAMNAME_CHANNEL_ID);
                Predicate predicate_ex = exp.in(table_ex).not();
                where.add(predicate_ex);
            }

            if (channelId != null) {
                where.add(builder.equal(root.get(PARAMNAME_CHANNEL_ID).get(PARAMNAME_CHANNEL_ID), channelId));
            }

            switch (pState) {
                case EVENT_ID:
                    where.add(builder.equal(root.get(PARAMNAME_EVENT_ID), eventId));
                    break;
                case START_DATETIME:
                    where.add(builder.equal(root.get(PARAMNAME_START_DATETIME), startDateTime));
                    break;
                case START_DATETIME_BETWEEN:
                    where.add(builder.between(root.<Date>get(PARAMNAME_START_DATETIME), startDateTimeRange_s, startDateTimeRange_e));
                    break;
            }
            query.where(where.toArray(new Predicate[where.size()]));
            ql = man.createQuery(query);

            final List<Programme> table = ql.getResultList();

            PrograammeListSorter.sortRes(table);

            System.out.print(Main.printRes(table, format, firstOnly));

            trans.commit();
            man.close();

        }

    }
}
