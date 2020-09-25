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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * コマンドの実行に必要なパラメータの保持を行う。
 *
 * @author dosdiaopfhj
 */
public final class RecordParameter {

    //private static final Logger log = LoggerConfigurator.getlnstance().getCallerLogger();

    //物理チャンネル番号
    private final long physicalChannelNumber;

    //終了時刻
    private final long duration;

    //ファイル名
    private final String fileName;

    /**
     * 録画コマンドに渡すパラメータ
     *
     * @param physicalChannelNumber 物理チャンネル番号
     * @param duration 録画時間(秒)
     * @param fileName 録画ファイル名
     */
    public RecordParameter(long physicalChannelNumber, long duration, String fileName) {
        this.physicalChannelNumber = physicalChannelNumber;
        this.duration = duration;
        this.fileName = fileName;
    }

    public synchronized long getPhysicalChannelNumber() {
        return physicalChannelNumber;
    }

    /**
     * 番組の放送時間を秒数で取得する。
     *
     * @return 放送時間の秒数。
     */
    public long getDuration() {
        return duration;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 1489, this);
    }

    /**
     * 保持している値がすべて等しければtrue
     *
     * @return
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
