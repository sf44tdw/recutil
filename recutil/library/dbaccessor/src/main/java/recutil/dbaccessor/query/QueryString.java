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
package recutil.dbaccessor.query;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import recutil.dbaccessor.entity.Excludechannel;
import static recutil.dbaccessor.query.QueryString.Channel.PARAMNAME_CHANNEL_NO;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;

/**
 * クエリ用
 *
 * @author normal
 */
public final class QueryString {

    /**
     * 除外チャンネルテーブルの内容をリストにして取ってくる。
     *
     * @param man マネージャー
     * @return 除外チャンネルテーブルの内容
     */
    public static List<String> getExcludeChannelList(final EntityManager man) {
        //除外チャンネルテーブルの内容をとってくる。
        final CriteriaBuilder builder = man.getCriteriaBuilder();
        final CriteriaQuery<String> query_ex = builder.createQuery(String.class);
        final Root<Excludechannel> root_ex = query_ex.from(Excludechannel.class);
        query_ex.select(root_ex.get(PARAMNAME_CHANNEL_ID)).distinct(true);
        final TypedQuery<String> ql_ex;
        ql_ex = man.createQuery(query_ex);
        List<String> table_ex;
        table_ex = ql_ex.getResultList();
        return table_ex;
    }

    private QueryString() {
    }

    public final class Common {

        private Common() {
        }

        public static final String PARAMNAME_CHANNEL_ID = "channelId";

    }

    public final class Channel {

        private Channel() {
        }
        public static final String PARAMNAME_CHANNEL_NO = "channelNo";
        public static final String PARAMNAME_DISPLAY_NAME = "displayName";

    }

    public final class Programme {

        public static final String PARAMNAME_EVENT_ID = "eventId";
        public static final String PARAMNAME_START_DATETIME = "startDatetime";

       
        private Programme() {
        }

    }

}
