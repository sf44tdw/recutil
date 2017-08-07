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
package recutil.updatedb.dataextractor.channel;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import recutil.updatedb.dataextractor.EpgData;

/**
 * チャンネル情報保持用
 *
 * @author dosdiaopfhj
 */
public final class ChannelData implements EpgData {

// チャンネルid
    private final String id;

// 物理チャンネル番号
    private final int physicalChannelNumber;

// 放送局名
    private final String broadcastingStationName;

    /**
     *
     * @param id チャンネルID
     * @param physicalChannelNumber 物理チャンネル番号
     * @param broadcastingStationName 放送局名
     */
    public ChannelData(String id, int physicalChannelNumber, String broadcastingStationName) throws IllegalArgumentException {
        if (id != null && !"".equals(id)) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("チャンネルIDがありません。");
        }
        if (physicalChannelNumber >= 0) {
            this.physicalChannelNumber = physicalChannelNumber;
        } else {
            throw new IllegalArgumentException("物理チャンネル番号が0未満です。");
        }
        if (broadcastingStationName != null && !"".equals(broadcastingStationName)) {
            this.broadcastingStationName = broadcastingStationName;
        } else {
            throw new IllegalArgumentException("放送局名がありません。");
        }

    }

    /**
     * @return チャンネルID
     */
    @Override
    public synchronized String getId() {
        return id;
    }

    /**
     * @return 物理チャンネル番号
     */
    public synchronized int getPhysicalChannelNumber() {
        return physicalChannelNumber;
    }

    /**
     * @return 放送局名
     */
    public synchronized String getBroadcastingStationName() {
        return broadcastingStationName;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 43, this);
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
