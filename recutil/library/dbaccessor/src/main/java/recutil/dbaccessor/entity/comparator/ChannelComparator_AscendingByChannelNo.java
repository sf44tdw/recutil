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
package recutil.dbaccessor.entity.comparator;

import java.util.Comparator;
import recutil.dbaccessor.entity.Channel;

/**
 * 局情報のチャンネル番号による昇順ソート用
 * @author normal
 */
public class ChannelComparator_AscendingByChannelNo implements Comparator<Channel> {

    @Override
    public int compare(Channel o1, Channel o2) {
        long no1 = o1.getChannelNo();
        long no2 = o2.getChannelNo();
        if (no1 > no2) {
            return 1;
        } else if (no1 == no2) {
            return 0;
        } else {
            return -1;
        }
    }

}
