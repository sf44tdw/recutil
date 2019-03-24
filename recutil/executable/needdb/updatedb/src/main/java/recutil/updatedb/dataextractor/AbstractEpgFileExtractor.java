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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * 読み込んだEPG XMLから必要な情報を取り出す。
 *
 * @author dosdiaopfhj
 * @param <T> EPGDataインタフェースを実装したクラス。
 */
public abstract class AbstractEpgFileExtractor<T extends EpgData> {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    private final Document doc;
    private final String nodeName;

    /**
     *
     * @param doc パースされたEPGのXMLデータ
     * @param nodeName 情報を取り出したい要素のノード名
     */
    public AbstractEpgFileExtractor(Document doc, String nodeName) {
        this.doc = doc;
        this.nodeName = nodeName;

    }

    /**
     * ノードをダンプする。
     */
    private String dumpNode(Node N) {
        try {
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(N), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerException ex) {
            LOG.warn("ノードのダンプが取得できませんでした。");
            return "";
        }
    }

    /**
     * 特定の要素名のノードを全て確保し、1件ごとに実装されたdump()を呼び出してデータの抽出処理を行う。
     *
     * @author dosdiaopfhj
     * @return 抽出されたデータのリスト
     *
     */
    public final synchronized List<T> makeList() {
        List<T> records = Collections.synchronizedList(new ArrayList<T>());
        NodeList nl = doc.getElementsByTagName(this.nodeName);
        int Nodes = nl.getLength();
        for (int i = 0; i < Nodes; i++) {
            Node N = nl.item(i);
            try {
                T recoed_val = dump(N);
                records.add(recoed_val);
            } catch (IllegalArgumentException ex) {
                LOG.warn("ノードの内容に問題があるため、無視します。 ノード = {}", dumpNode(N));
            }
        }
        return records;
    }

    /**
     * 取得した要素の内容から必要なデータを1件抽出する。makeList()が使用することを前提にしているので、外部から直接使わないこと。
     *
     * @author dosdiaopfhj
     * @param N 取得した要素
     * @return 1件分のデータ。
     */
    protected abstract T dump(Node N) throws IllegalArgumentException;

//    protected final synchronized void printNode(Node node) {
//
//        System.out.print("ノード名 = " + node.getNodeName() + " "); // ノード名
//        System.out.print("ノードタイプ = " + node.getNodeType() + " "); // ノードタイプ
//        System.out.println("ノード値 = " + node.getNodeValue()); // ノード値
//
//    }
}
