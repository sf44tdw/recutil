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

import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import static recutil.commmonutil.Util.dumpList;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;
import recutil.updatedb.common.TestData;
import recutil.updatedb.dataextractor.channel.ChannelData;
import recutil.updatedb.dataextractor.channel.ChannelDataExtractor;
import recutil.updatedb.listmaker.XmlLoader;

/**
 *
 * @author dosdiaopfhj
 */
public class ChannelDataExtractorTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public ChannelDataExtractorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        List<ChannelData> expResult = TestData.get24Chs();
        LOG.info(dumpList(expResult));
        XmlLoader loader = new XmlLoader(Const.CHARCODE);
        Document doc = loader.Load(Const.getTESTDATA_XML_1());
        ChannelDataExtractor target = new ChannelDataExtractor(doc);
        List<ChannelData> chs = target.makeList();
        LOG.info(dumpList(expResult));
        assertEquals(expResult, chs);
    }

}
