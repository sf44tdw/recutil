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
package recutil.updatedb.listmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.nio.charset.Charset;
import java.text.MessageFormat;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * 指定された文字コードでEPG XMLファイルを読み込む。
 *
 * @author dosdiaopfhj
 */
public class XmlLoader {

    private final Charset charset;

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * @param charset XMLファイルの文字コード
     */
    public XmlLoader(Charset charset) {
        this.charset = charset;
    }

    public final synchronized Charset getCharset() {
        return charset;
    }

    private static final String STR_COMMON = "ファイル = {0} 文字コード={1} 読み込み処理";
    private static final MessageFormat MSG_READ_START = new MessageFormat(STR_COMMON + " 開始。");
    private static final MessageFormat MSG_READ_COMPLETE = new MessageFormat(STR_COMMON + " 完了。");

    /**
     * XMLを読み込む
     *
     * @param F XMLファイル
     * @return XMLファイルから作ったDocumentオブジェクト。
     * @author dosdiaopfhj
     */
    public synchronized Document Load(File F) {
        try {
            Object[] parameters1 = {F, getCharset()};
            LOG.info(MSG_READ_START.format(parameters1));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new XmlTvDtdResolver());
            Document document = documentBuilder.parse(new InputSource(new InputStreamReader(new FileInputStream(F), getCharset())));
            Element root = document.getDocumentElement();
            LOG.info(MSG_READ_COMPLETE.format(parameters1));
            return document;
        } catch (ParserConfigurationException | UnsupportedEncodingException | FileNotFoundException ex) {
            LOG.error("例外発生。", ex);
            return null;
        } catch (SAXException | IOException ex) {
            LOG.error("例外発生。", ex);
            return null;
        }

    }

}
