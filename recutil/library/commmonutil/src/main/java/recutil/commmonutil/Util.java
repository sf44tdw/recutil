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
package recutil.commmonutil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public final class Util {
    private Util() {
    }
    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * 日付フォーマット
     *
     * @return 日付フォーマット
     */
    public static String getDbDatePattern() {
        return "yyyy/MM/dd_HH:mm:ss";
    }

    /**
     * デフォルトの改行文字
     *
     * @return 改行文字
     */
    public static String getDefaultLineSeparator() {
        return System.getProperty("line.separator");
    }



    /**
     * 渡されたリストをダンプする。
     *
     * @param x ダンプしたいリスト
     * @return ダンプ結果の文字列
     */
    public static String dumpList(List<?> x) {
        final String DEFAULT_LINE_SEPARATOR = getDefaultLineSeparator();
        StringBuilder s = new StringBuilder();
        s.append(DEFAULT_LINE_SEPARATOR).append(DEFAULT_LINE_SEPARATOR);
        for (Object obj : x) {
            s.append(ReflectionToStringBuilder.toString(obj)).append(DEFAULT_LINE_SEPARATOR);
        }
        s.append(DEFAULT_LINE_SEPARATOR);
        return s.toString();
    }

    /**
     * <p>
     * long型⇒Date型への変換処理</p>
     *
     * @param date unixtime数値
     * @return Date型オブジェクト（変換に失敗した場合はnullを返します。）
     */
    public static Date parseLongToDate(long date) {
        try {
            Date ret = new Date(date);
//            System.out.println("D****************************************************************************** " + ret.toString());
            return ret;
        } catch (Exception ex) {
            LOG.error("日付の変換中に問題が発生しました。", ex);
            return null;
        }
    }

    /**
     * <p>
     * Date型⇒String型(yyyy/mm/dd_hh:mm:ss)への変換処理</p>
     *
     * @param date 変換前日付オブジェクト
     * @return String型オブジェクト（変換に失敗した場合はnullを返します。）
     */
    public static String parseDateToString(Date date) {
        try {
            String str;
            if (date == null) {
                str = null;
            } else {
                str = new SimpleDateFormat(Util.getDbDatePattern()).format(date);
            }
//            System.out.println("S****************************************************************************** " + str);
            return str;
        } catch (Exception ex) {
            LOG.error("日付の変換中に問題が発生しました。", ex);
            return null;
        }
    }

    /**
     * <p>
     * long型⇒Date型⇒String型(yyyy/mm/dd_hh:mm)への変換処理</p>
     *
     * @param date unixtime数値
     * @return String型オブジェクト（変換に失敗した場合はnullを返します。）
     */
    public static String parseLongToString(long date) {
//        System.out.println("L****************************************************************************** " + date);
        return parseDateToString(parseLongToDate(date));
    }
}
