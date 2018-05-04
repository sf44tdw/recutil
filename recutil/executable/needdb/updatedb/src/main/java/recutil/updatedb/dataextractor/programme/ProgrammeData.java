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
package recutil.updatedb.dataextractor.programme;

import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import recutil.updatedb.dataextractor.EpgData;

/**
 * 番組情報の保持を行うクラス
 *
 * @author dosdiaopfhj
 */
public final class ProgrammeData implements EpgData {

    //最初のフィルタ。
    //開始時刻
    private final java.sql.Timestamp startDatetime;

    //ある1つの番組の指定に必要な2項目。
    //チャンネルid
    private final String id;
    //番組id
    private final long eventId;

    //最終的に取り出したい情報。
    //番組名
    private final String title;

    //終了時刻
    private final java.sql.Timestamp stopDatetime;

    /**
     * 番組データ
     *
     * @param id:チャンネルID
     * @param eventId:番組ID
     * @param title:番組名
     * @param startDatetime:放送開始時刻
     * @param stopDatetime :放送終了時刻
     */
    public ProgrammeData(String id, long eventId, String title, Date startDatetime, Date stopDatetime) throws IllegalArgumentException {

        if (id != null && !"".equals(id)) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("チャンネルIDがありません。");
        }
        if (eventId >= 0) {
            this.eventId = eventId;
        } else {
            throw new IllegalArgumentException("番組IDが0未満です。");
        }
        if (title != null && !"".equals(title)) {
            this.title = title;
        } else {
            throw new IllegalArgumentException("番組名がありません。");
        }

        //下記のように、放送開始日時より前に放送終了日時をセットした番組情報が配信されてくることもあるので、一応チェックする。
        //情報: programme{チャンネルID=BS_101, 番組ID=12189, 番組名=田中先発　ＭＬＢ・アメリカ大リーグ「ヤンキース」対「ブレーブス」【二】, 放送開始日時=2015-08-29 08:30:00.0, 放送終了日時=2015-08-29 08:29:59.0}
        if (startDatetime != null && stopDatetime != null) {
            this.startDatetime = new Timestamp(startDatetime.getTime());
            this.stopDatetime = new Timestamp(stopDatetime.getTime());
            if ((this.stopDatetime.getTime() - this.startDatetime.getTime()) >= 0) {
            } else {
                throw new IllegalArgumentException("放送開始時刻が放送終了時刻より後になっています。");
            }
        } else {
            throw new IllegalArgumentException("放送開始時刻、放送終了時刻のいずれかがありません。");
        }

    }

    @Override
    public synchronized String getId() {
        return id;
    }

    public synchronized long getEventId() {
        return eventId;
    }

    public synchronized String getTitle() {
        return title;
    }

    public synchronized Date getStartDatetime() {
        return copyTimestamp(startDatetime);
    }

    public synchronized Date getStopDatetime() {
        return copyTimestamp(stopDatetime);
    }

    /**
     * このクラスが保持する放送時間の改変対策として、コピーを渡す。
     *
     * @return 放送時間のコピー。
     */
    private synchronized final Timestamp copyTimestamp(Timestamp ts) {
        return new Timestamp(ts.getTime());
    }

    @Override
    public synchronized int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 37, this);
    }

    /**
     * 保持している値がすべて等しければtrue
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
