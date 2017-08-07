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
package recutil.updatedb.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;

/**
 *
 * @author normal
 */
public final class Util {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    private Util() {
    }

    public static String docToString(Document document) {

        final String s = "";

        // Transformerインスタンスの生成
        Transformer transformer = null;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            LOG.info("eror", e);
            return s;
        }
        // Transformerの設定
        transformer.setOutputProperty("indent", "yes"); //改行指定
        transformer.setOutputProperty("encoding", Const.CHARCODE.name()); // エンコーディング

        // XMLの作成
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        try {
            transformer.transform(new DOMSource(document), new StreamResult(dataStream));
        } catch (TransformerException e) {
            LOG.info("eror", e);
            return s;
        }

        try {
            return byteStream.toString(Const.CHARCODE.name());
        } catch (UnsupportedEncodingException ex) {
            LOG.info("eror", ex);
            return s;
        }
    }

    public static boolean isSamePath(File f1, File f2) {
        return (f1.getAbsolutePath() == null ? f2.getAbsolutePath() == null : f1.getAbsolutePath().equals(f2.getAbsolutePath()));
    }

    public static boolean isSameFileList(List<File> l1, List<File> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        boolean ret = true;
        for (File f : l1) {
            ret = ret && l2.contains(f);
        }
        for (File f : l2) {
            ret = ret && l1.contains(f);
        }
        return ret;
    }
}
