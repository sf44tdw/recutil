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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import static recutil.commmonutil.Util.parseDateToString;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.entity.comparator.PrograammeListSorter;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Programme.ALL_USEABLE_PROGRAMME;
import static recutil.dbaccessor.query.QueryString.Programme.PARAMNAME_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.PARAMNAME_START_DATETIME;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_START_DATETIME;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class Main {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    protected static final String DATETIME_FORMAT = recutil.commmonutil.Util.getDbDatePattern();

    public static final String getSep() {
        return System.getProperty("line.separator");
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
        NONE, EVENT_ID, START_DATETIME;
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

    public void start(String[] args) throws org.apache.commons.cli.ParseException {

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
                .type(Integer.class)
                .build();

        final Option startDateTimeOption = Option.builder("s")
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

        Options opts = new Options();
        opts.addOption(titleOnlyOption);
        opts.addOption(firstOnlyOption);
        opts.addOption(withExcludeChannelOption);
        opts.addOption(ChannelIdOption);
        opts.addOptionGroup(searchOpts);
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

        final Integer eventId;
        if (cl.hasOption(eventIdOption.getOpt())) {
            eventId = Integer.valueOf(cl.getOptionValue(eventIdOption.getOpt()));
        } else {
            eventId = null;
        }
        final Date startDateTime;
        if (cl.hasOption(startDateTimeOption.getOpt())) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
            try {
                startDateTime = sdf.parse(cl.getOptionValue(startDateTimeOption.getOpt()));
            } catch (java.text.ParseException ex) {
                final String s = "放送開始日時を取得できませんでした。";
                throw new IllegalArgumentException(s, ex);
            }
        } else {
            startDateTime = null;
        }

        final paramState pState;
        if (eventId != null) {
            pState = paramState.EVENT_ID;
        } else if (startDateTime != null) {
            pState = paramState.START_DATETIME;
        } else {
            pState = paramState.NONE;
        }

        final String PROP_ERROR = "適切なクエリを見つけられません。";

        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();

            final TypedQuery<Programme> ql;

            switch (exclude) {
                case USEABLE:
                    if (channelId != null) {
                        switch (pState) {
                            case EVENT_ID:
                                ql = man.createQuery(USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID, Programme.class);
                                ql.setParameter(PARAMNAME_EVENT_ID, eventId);
                                break;
                            case START_DATETIME:
                                ql = man.createQuery(USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME, Programme.class);
                                ql.setParameter(PARAMNAME_START_DATETIME, startDateTime);
                                break;
                            case NONE:
                                ql = man.createQuery(USEABLE_PROGRAMME_BY_CHANNEL_ID, Programme.class);
                                break;
                            default:
                                ql = null;
                                LOG.error(PROP_ERROR, ReflectionToStringBuilder.toString(args));

                        }
                        if (ql != null) {
                            ql.setParameter(PARAMNAME_CHANNEL_ID, channelId);
                        }
                    } else {
                        switch (pState) {
                            case EVENT_ID:
                                ql = man.createQuery(USEABLE_PROGRAMME_BY_EVENT_ID, Programme.class);
                                ql.setParameter(PARAMNAME_EVENT_ID, eventId);
                                break;
                            case START_DATETIME:
                                ql = man.createQuery(USEABLE_PROGRAMME_BY_START_DATETIME, Programme.class);
                                ql.setParameter(PARAMNAME_START_DATETIME, startDateTime);
                                break;
                            case NONE:
                                ql = man.createQuery(ALL_USEABLE_PROGRAMME, Programme.class);
                                break;
                            default:
                                ql = null;
                                LOG.error(PROP_ERROR, ReflectionToStringBuilder.toString(args));

                        }
                    }
                    break;
                case ALL:
                    if (channelId != null) {
                        switch (pState) {
                            case EVENT_ID:
                                ql = man.createNamedQuery("Programme.findByChannelIdAndEventId", Programme.class);
                                ql.setParameter(PARAMNAME_EVENT_ID, eventId);
                                break;
                            case START_DATETIME:
                                ql = man.createNamedQuery("Programme.findByChannelIdAndStartDatetime", Programme.class);
                                ql.setParameter(PARAMNAME_START_DATETIME, startDateTime);
                                break;
                            case NONE:
                                ql = man.createNamedQuery("Programme.findByChannelId", Programme.class);
                                break;
                            default:
                                ql = null;
                                LOG.error(PROP_ERROR, ReflectionToStringBuilder.toString(args));
                        }
                        if (ql != null) {
                            ql.setParameter(PARAMNAME_CHANNEL_ID, channelId);
                        }
                    } else {
                        switch (pState) {
                            case EVENT_ID:
                                ql = man.createNamedQuery("Programme.findByEventId", Programme.class);
                                ql.setParameter(PARAMNAME_EVENT_ID, eventId);
                                break;
                            case START_DATETIME:
                                ql = man.createNamedQuery("Programme.findByStartDatetime", Programme.class);
                                ql.setParameter(PARAMNAME_START_DATETIME, startDateTime);
                                break;
                            case NONE:
                                ql = man.createNamedQuery("Programme.findAll", Programme.class);
                                break;
                            default:
                                ql = null;
                                LOG.error(PROP_ERROR, ReflectionToStringBuilder.toString(args));
                        }
                    }
                    break;
                default:
                    ql = null;
                    LOG.error(PROP_ERROR, ReflectionToStringBuilder.toString(args));
            }

            if (ql != null) {
                final List<Programme> table = ql.getResultList();

                PrograammeListSorter.sortRes(table);

                System.out.print(Main.printRes(table, format, firstOnly));

            }
            man.close();
        }

    }
}
