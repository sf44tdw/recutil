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
package recutil.updatedb.dateconverter;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author dosdiaopfhj
 */
public final class Converter {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

    private Converter() {
    }

    /**
     * 日時をjava.sql.Timestamp型に変換
     *
     * @param source 日時文字列
     * @param datePattern SimpleDateFormatで使用する日時のパターン
     * @return 日時の入ったTimestampオブジェクト。
     */
    public static final synchronized Date stringToDate(String source, String datePattern) {
        try {
            return new Date((new SimpleDateFormat(datePattern)).parse(source).getTime());
        } catch (ParseException ex) {
            final MessageFormat temp = new MessageFormat("日時のDate型への変換に失敗しました。文字列 = {0} 使用パターン = {1}");
            Object[] parameters = {source, datePattern};
            LOG.error(temp.format(parameters), ex);
            return null;
        }
    }

//    /**
//     * java.util.Dateをjava.sql.Dateに変換する
//     *
//     * @param utilDate java.util.Date形式の日時
//     * @return java.sql.Date形式の日時
//     */
//    public final synchronized java.sql.Date utilDateTOSqlDate(java.util.Date utilDate) {
//       
//                java.util.Calendar cal = Calendar.getInstance();
//        cal.setTime(utilDate);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
//        java.sql.Date sqlDate = new java.sql.Date(cal.getTimeInMillis());
//        log.log(Level.INFO, "LoggerConfigurator.Date={0}_Sql.Date={1}", new Object[]{utilDate.toString(), sqlDate.toString()});
//        return sqlDate;
//    }
}
