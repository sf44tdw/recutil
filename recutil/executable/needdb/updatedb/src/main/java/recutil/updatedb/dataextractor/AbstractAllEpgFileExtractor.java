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
package recutil.updatedb.dataextractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;

import org.w3c.dom.Document;

import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * チャンネル。番組の情報を全てのXMLファイルから抽出し、リストにまとめる。
 *
 * @author dosdiaopfhj
 * @param <T> EPGDataインタフェースを実装したクラス。
 * @param <U> 情報取り出し用クラス
 */
public abstract class AbstractAllEpgFileExtractor<T extends EpgData, U extends AbstractEpgFileExtractor<T>> {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    private final List<Document> EPGXMLs;

    /**
     * @param EPGXMLs ロード済みのEPG XMLファイルのリスト
     */
    public AbstractAllEpgFileExtractor(List<Document> EPGXMLs) {
        this.EPGXMLs = EPGXMLs;
    }

    /**
     * XMLから情報を取り出すクラスを取得する処理を実装する。getAllEPGRecords()が使用する。
     *
     * @param doc ロードされたXMLファイル
     * @return 情報取り出し用クラス
     */
    protected abstract U getExtractor(Document doc);

    /**
     * 抽出された情報をソートする処理を実装する。getAllEPGRecords()が使用する。
     *
     * @param dataList ソートしたいリスト。
     */
    protected abstract void sortRersult(List<T> dataList);

    /**
     * 渡された全てのXMLから、 getExtractor()によって取得された抽出用オブジェクトによって抽出された情報を取得する。
     * 重複した情報は除外する。
     *
     * @return 取得された情報を納めた変更不可リスト。実装に従ってソートされる。
     */
    public final synchronized List<T> getAllEPGRecords() {
        List<T> temp_List1 = Collections.synchronizedList(new ArrayList<T>());
        if (LOG.isDebugEnabled()) {
            LOG.debug("ファイル数={}", this.EPGXMLs.size());
        }
        Iterator<Document> it_EPG = this.EPGXMLs.iterator();
        while (it_EPG.hasNext()) {
            Document D = it_EPG.next();
            List<T> temp_List2;
            temp_List2 = this.getExtractor(D).makeList();
            temp_List1.addAll(temp_List2);
        }
        //重複排除
        Set<T> set = new HashSet<>(temp_List1);
        List<T> temp_List3 = Collections.synchronizedList(new ArrayList<T>(set));
        this.sortRersult(temp_List3);
        return Collections.unmodifiableList(temp_List3);
    }
}
