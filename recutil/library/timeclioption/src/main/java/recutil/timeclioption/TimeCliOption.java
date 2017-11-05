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
package recutil.timeclioption;

import java.text.MessageFormat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;
import static recutil.timeclioption.TimeCliOption.OPTION_NAME.HOUR;
import static recutil.timeclioption.TimeCliOption.OPTION_NAME.MINUTE;
import static recutil.timeclioption.TimeCliOption.OPTION_NAME.SECOND;

/**
 * 時間、分、秒を指定するオプションを生成する。 オプションのdesc欄はコンストラクタで指定できる。
 *
 *
 * @author normal
 */
public final class TimeCliOption {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();    //秒の大きさで他を制限する。

    /**
     * 秒数の最大
     */
    public static final long MAX_SECOND = Long.MAX_VALUE;
    /**
     * 分数の最大
     */
    public static final long MAX_MINUTE = MAX_SECOND / 60;
    /**
     * 時間数の最大
     */
    public static final long MAX_HOUR = MAX_MINUTE / 60;

    /**
     * 秒数の範囲
     */
    public static final Range<Long> RANGE_SECOND_LIMIT = Range.between(0L, MAX_SECOND);
    /**
     * 分数の範囲
     */
    public static final Range<Long> RANGE_MINUTE_LIMIT = Range.between(0L, MAX_MINUTE);
    /**
     * 時間数の範囲
     */
    public static final Range<Long> RANGE_HOUR_LIMIT = Range.between(0L, MAX_HOUR);

    /**
     * 短縮オプションと長オプションのペア。
     */
    public static enum OPTION_NAME {
        SECOND("s", "second"),
        MINUTE("m", "minute"),
        HOUR("h", "hour");
        private final String shortOption;
        private final String longOption;

        private OPTION_NAME(String shortOption, String longOption) {
            this.shortOption = shortOption;
            this.longOption = longOption;
        }

        public String getShortOption() {
            return shortOption;
        }

        public String getLongOption() {
            return longOption;
        }

    }

    private final String secondDesc;
    private final String minuteDesc;
    private final String hourDesc;

    private Option makeTimeOption(final OPTION_NAME option, final String desc) {
        Option o = Option.builder(option.getShortOption())
                .longOpt(option.getLongOption())
                .required(true)
                .desc(desc)
                .hasArg(true)
                .type(Long.class)
                .build();
        return o;
    }

    public TimeCliOption(String secondDesc, String minuteDesc, String hourDesc) {
        this.secondDesc = secondDesc;
        this.minuteDesc = minuteDesc;
        this.hourDesc = hourDesc;
    }

    /**
     * 秒数オプションを作成する。
     *
     * @return
     */
    public Option getSecondRangeOption() {
        return makeTimeOption(SECOND, secondDesc);
    }

    /**
     * 分数オプションを作成する。
     *
     * @return
     */
    public Option getMinuteRangeOption() {
        return makeTimeOption(MINUTE, minuteDesc);
    }

    /**
     * 時間数オプションを作成する。
     *
     * @return
     */
    public Option getHourRangeOption() {
        return makeTimeOption(HOUR, hourDesc);
    }

    /**
     * 秒、分、時間を排他で受け取るオプショングループを作成する。
     *
     * @param required グループ内のオプションが存在していなければならない場合はtrue。
     * @return
     */
    public OptionGroup getTimeOptionGroup(boolean required) {
        OptionGroup opts = new OptionGroup();
        opts.setRequired(required);
        opts.addOption(getSecondRangeOption());
        opts.addOption(getMinuteRangeOption());
        opts.addOption(getHourRangeOption());
        return opts;
    }

    private Long getTimeValue(final CommandLine cl, final OPTION_NAME option) {
        final Long duration;
        if (cl.hasOption(option.getShortOption())) {
            String val = cl.getOptionValue(option.getShortOption());
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

    /**
     * このクラスが作成したオプショングループで取得した時間の数値をチェックし、秒数で返す。
     *
     * @param cl このクラスが作成したオプショングループを使用したコマンドライン
     * @return 取得した秒数。
     * @throws recutil.timeclioption.TimeParseException 秒への変換ができなかった場合。
     * @see TimeCliOption#getTimeOptionGroup
     *
     */
    public long getValueBySecond(final CommandLine cl) throws TimeParseException {
        try {
            final MessageFormat mf = new MessageFormat("時間の値が0より小さいか、上限を超えています。単位 = {0} 値 = {1}");
            final Object[] message;
            final Long secondRange = this.getTimeValue(cl, OPTION_NAME.SECOND);
            final Long minuteRange = this.getTimeValue(cl, OPTION_NAME.MINUTE);
            final Long hourRange = this.getTimeValue(cl, OPTION_NAME.HOUR);
            final Long rangeValue;
            if (hourRange != null) {
                if (!RANGE_HOUR_LIMIT.contains(hourRange)) {
                    message = new Object[]{"hour", hourRange};
                    throw new IllegalArgumentException(mf.format(message));
                }
                //2乗にするとおかしくなる。0*60^2=2と出てきたことがあった。
                rangeValue = hourRange * 60 * 60;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("hourRange = {} -> rangeValue = {}", hourRange, rangeValue);
                }
            } else if (minuteRange != null) {
                if (!RANGE_MINUTE_LIMIT.contains(minuteRange)) {
                    message = new Object[]{"minute", minuteRange};
                    throw new IllegalArgumentException(mf.format(message));
                }
                rangeValue = minuteRange * 60;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("minuteRange = {} -> rangeValue = {}", minuteRange, rangeValue);
                }
            } else if (secondRange != null) {
                if (!RANGE_SECOND_LIMIT.contains(secondRange)) {
                    message = new Object[]{"second", secondRange};
                    throw new IllegalArgumentException(mf.format(message));
                }
                rangeValue = secondRange;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("secondRange = {} -> rangeValue = {}", secondRange, rangeValue);
                }
            } else {
                throw new TimeParseException("時間を秒数に変換できませんでした。" + " 時 = " + hourRange + " 分 = " + minuteRange + " 秒 = " + secondRange);
            }
            return rangeValue;
        } catch (NumberFormatException ex) {
            throw new TimeParseException("数字以外が与えられました。", ex);
        }
    }

}