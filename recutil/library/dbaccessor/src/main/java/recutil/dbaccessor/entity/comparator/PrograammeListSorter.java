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

import java.util.Collections;
import java.util.List;
import recutil.dbaccessor.entity.Programme;

/**
 *
 * @author normal
 */
public final class PrograammeListSorter {

    private PrograammeListSorter() {
    }
    
        /**
     * 結果を、 番組ID昇順、 チャンネルID昇順、 放送開始日時昇順、 でソートする。
     *
     * @param target ソート対象。(DBから取得した番組情報)
     */
    public static void sortRes(List<Programme> target) {
        //        LOG.debug(recutil.dbaccessor.util.Util.dumpList(table));
        Collections.sort(target,
                new ProgrammeComparator_AscendingEventId());

//        LOG.debug(recutil.dbaccessor.util.Util.dumpList(table));
        Collections.sort(target,
                new ProgrammeComparator_AscendingByChannelId());

//        LOG.debug(recutil.dbaccessor.util.Util.dumpList(table));
        Collections.sort(target,
                new ProgrammeComparator_AscendingByStartDatetime());
        //        LOG.debug(recutil.dbaccessor.util.Util.dumpList(table));
    }
}
