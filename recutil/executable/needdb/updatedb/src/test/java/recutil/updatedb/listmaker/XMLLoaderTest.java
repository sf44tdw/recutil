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
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;

/**
 *
 * @author dosdiaopfhj
 */
public class XMLLoaderTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public XMLLoaderTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCharset method, of class XmlLoader.
     */
    @Test
    public void testGetCharset() {
        LOG.info("getCharset");
        XmlLoader instance = new XmlLoader(Const.CHARCODE);
        String expResult = "UTF-8";
        String result = instance.getCharset().name();
        assertEquals(expResult, result);
    }

    /**
     * Test of Load method, of class XmlLoader.
     */
    @Test
    public void testLoad() {
        LOG.info("Load");
        File F = Const.getTESTDATA_XML_1();
        XmlLoader instance = new XmlLoader(Const.CHARCODE);
        Document result = null;
        result = instance.Load(F);
        assertTrue(result != null);
        String s2 = recutil.updatedb.common.util.Util.docToString(result);
        LOG.info(s2);
    }

}
