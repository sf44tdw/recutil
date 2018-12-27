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

import java.io.IOException;
import org.slf4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * クラスパス上に配置したxmltv.dtdを読み込ませる。
 *
 * @author normal
 */
public class XmlTvDtdResolver implements EntityResolver {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    public static final String DTD_NAME = "xmltv.dtd";
    /**
     * @author normal
     */
    public XmlTvDtdResolver() {

    }

    /**
     * 公開識別子もしくはシステム識別子にxmltv.dtdを含んだ文字列があった場合、DocumentBuilderに対し、このクラスで指定されたファイルからxmltv.dtdを読み込ませる。
     * ファイルが指定されなかった場合はnullを返す。
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

        ClassLoader cll = java.lang.ClassLoader.getSystemClassLoader();
        LOG.info("DTDファイル ={}",cll.getResource(DTD_NAME).toString());

        if ((publicId != null && publicId.contains(DTD_NAME)) || (systemId != null && systemId.contains(DTD_NAME))) {
            LOG.trace("識別子を確認しました。");
            InputSource source = new InputSource(cll.getResourceAsStream(DTD_NAME));
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        } else {
            LOG.trace("公開識別子、システム識別子とも、{}を含む文字列ではありませんでした。",DTD_NAME);
            return null;
        }
    }
}
