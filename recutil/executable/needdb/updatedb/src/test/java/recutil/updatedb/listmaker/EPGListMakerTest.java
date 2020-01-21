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

import static org.junit.Assert.*;
import static recutil.updatedb.common.util.Util.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;

/**
 *
 * @author normal
 */
public class EPGListMakerTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public EPGListMakerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }



    /**
     * Documentの称号は文字列に直してからやっている。
     */
    @Test
    public void testGetEpgList() {
        LOG.info("seek");
        EpgListMaker instance = new EpgListMaker(Const.getXMLTESTDATADIR(), Const.CHARCODE);
        List<Document> result = instance.getEpgList();

        List<Document> expResult = new ArrayList<>();
        XmlLoader load = new XmlLoader(Const.CHARCODE);
        expResult.add(load.Load(Const.getTESTDATA_XML_1()));
        expResult.add(load.Load(Const.getTESTDATA_XML_2()));

        List<String> rstl = new ArrayList<>();
        for (Document doc : result) {
            rstl.add(docToString(doc));
        }
        List<String> exRstl = new ArrayList<>();
        for (Document doc : expResult) {
            exRstl.add(docToString(doc));
        }

//        LOG.info(dumpList(rstl));

        assertTrue(rstl.containsAll(exRstl));

    }

}
