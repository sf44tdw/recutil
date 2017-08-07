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

import static recutil.dbaccessor.query.QueryString.Channel.PARAMNAME_CHANNEL_NO;
import static recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID;

/**
 * クエリ用
 *
 * @author normal
 */
public final class QueryString {

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
        /**
         * 除外チャンネルID以外のチャンネルを取得
         */
        public static final String ALL_USEABLE_CHANNEL = "SELECT c FROM Channel c WHERE  NOT EXISTS (SELECT e FROM Excludechannel e WHERE e." + PARAMNAME_CHANNEL_ID + " = c." + PARAMNAME_CHANNEL_ID + ")";

        private static final String C_AND_BY_CHANNEL_ID = " AND c." + PARAMNAME_CHANNEL_ID + " = :" + PARAMNAME_CHANNEL_ID;
        private static final String C_AND_BY_CHANNEL_NO = " AND c." + PARAMNAME_CHANNEL_NO + " = :" + PARAMNAME_CHANNEL_NO;

        /**
         * 除外チャンネルID以外のチャンネルをチャンネルIDに基づいて取得
         */
        public static final String USEABLE_CHANNEL_BY_CHANNEL_ID = ALL_USEABLE_CHANNEL + C_AND_BY_CHANNEL_ID;
        /**
         * 除外チャンネルID以外のチャンネルをチャンネル番号に基づいて取得
         */
        public static final String USEABLE_CHANNEL_BY_CHANNEL_NO = ALL_USEABLE_CHANNEL + C_AND_BY_CHANNEL_NO;
    }

    public final class Programme {

        public static final String PARAMNAME_EVENT_ID = "eventId";
        public static final String PARAMNAME_START_DATETIME = "startDatetime";
        /**
         * 番組ソート用
         */
        private static final String P_ORDER_BY = " ORDER BY p.startDatetime, p.channelId.channelId";
        private static final String P_AND_BY_CHANNEL_ID = " AND p." + PARAMNAME_CHANNEL_ID + "." + PARAMNAME_CHANNEL_ID + " = :" + PARAMNAME_CHANNEL_ID;
        private static final String P_AND_BY_EVENT_ID = " AND p." + PARAMNAME_EVENT_ID + " = :" + PARAMNAME_EVENT_ID;
        private static final String P_AND_BY_START_DATETIME = " AND p." + PARAMNAME_START_DATETIME + " = :" + PARAMNAME_START_DATETIME;

        /**
         * 除外チャンネルID以外の番組を取得
         */
        public static final String ALL_USEABLE_PROGRAMME = "SELECT p FROM Programme p WHERE NOT EXISTS (SELECT e FROM Excludechannel e WHERE e." + PARAMNAME_CHANNEL_ID + " = p." + PARAMNAME_CHANNEL_ID + "." + PARAMNAME_CHANNEL_ID + ")";

        /**
         * 除外チャンネルID以外の番組を取得(開始時刻昇順、チャンネルID昇順ソート)
         */
        public static final String ALL_USEABLE_PROGRAMME_S = ALL_USEABLE_PROGRAMME + P_ORDER_BY;

        /**
         * 除外チャンネルID以外の番組を番組IDに基づいて取得
         */
        public static final String USEABLE_PROGRAMME_BY_EVENT_ID = ALL_USEABLE_PROGRAMME + P_AND_BY_EVENT_ID;

//    /**
//     * 除外チャンネルID以外の番組を番組IDに基づいて取得(開始時刻昇順、チャンネルID昇順ソート)
//     */
//    public static final String USEABLE_PROGRAMME_BY_EVENT_ID_S = USEABLE_PROGRAMME_BY_EVENT_ID + P_ORDER_BY;
        /**
         * 除外チャンネルID以外の番組をチャンネルIDに基づいて取得
         */
        public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID = ALL_USEABLE_PROGRAMME + P_AND_BY_CHANNEL_ID;

//    /**
//     * 除外チャンネルID以外の番組をチャンネルIDに基づいて取得(開始時刻昇順、チャンネルID昇順ソート)
//     */
//    public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID_S = USEABLE_PROGRAMME_BY_CHANNEL_ID + P_ORDER_BY;
        /**
         * 除外チャンネルID以外の番組をチャンネルIDと番組IDに基づいて取得
         */
        public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID = USEABLE_PROGRAMME_BY_CHANNEL_ID + P_AND_BY_EVENT_ID;

//    /**
//     * 除外チャンネルID以外の番組をチャンネルIDと番組IDに基づいて取得(開始時刻昇順、チャンネルID昇順ソート)
//     */
//    public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID_S = USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID + P_ORDER_BY;
        /**
         * 除外チャンネルID以外の番組を開始時刻に基づいて取得
         */
        public static final String USEABLE_PROGRAMME_BY_START_DATETIME = ALL_USEABLE_PROGRAMME + P_AND_BY_START_DATETIME;

//    /**
//     * 除外チャンネルID以外の番組を開始時刻に基づいて取得(開始時刻昇順、チャンネルID昇順ソート)
//     */
//    public static final String USEABLE_PROGRAMME_BY_START_DATETIME_S = USEABLE_PROGRAMME_BY_START_DATETIME + P_ORDER_BY;
        /**
         * 除外チャンネルID以外の番組をチャンネルIDと開始時刻に基づいて取得
         */
        public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME = USEABLE_PROGRAMME_BY_CHANNEL_ID + P_AND_BY_START_DATETIME;

//    /**
//     * 除外チャンネルID以外の番組をチャンネルIDと開始時刻に基づいて取得(開始時刻昇順、チャンネルID昇順ソート)
//     */
//    public static final String USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME_S = USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME + P_ORDER_BY;
        private Programme() {
        }

    }

}
