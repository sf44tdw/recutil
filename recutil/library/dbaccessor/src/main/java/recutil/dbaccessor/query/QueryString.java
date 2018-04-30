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

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import recutil.dbaccessor.entity.Excludechannel;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;

/**
 * クエリ用
 *
 * @author normal
 */
public final class QueryString {

    private final EntityManager man;

    /**
     * 除外チャンネルテーブルの内容をString型リストにして取ってくる。
     *
     * @return 除外チャンネルテーブルの内容
     */
    public List<String> getExcludeChannelList() {
        //除外チャンネルテーブルの内容をとってくる。
        final CriteriaBuilder builder = man.getCriteriaBuilder();
        final CriteriaQuery<String> query_ex = builder.createQuery(String.class);
        final Root<Excludechannel> root_ex = query_ex.from(Excludechannel.class);
        query_ex.select(root_ex.get(PARAMNAME_CHANNEL_ID)).distinct(true);
        final TypedQuery<String> ql_ex;
        ql_ex = man.createQuery(query_ex);
        return Collections.unmodifiableList(ql_ex.getResultList());
    }


    /**
     * @param man このクラス内で使用するマネージャー
     */
    public QueryString(EntityManager man) {
        this.man = man;
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
