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
package recutil.updatedb.dataextractor.channel;

import static org.junit.Assert.*;
import static recutil.commmonutil.Util.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;
import recutil.updatedb.common.TestData;
import recutil.updatedb.listmaker.XmlLoader;

/**
 *
 * @author dosdiaopfhj
 */
public class AllChannelDataExtractorTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

    private final Charset CHARCODE = Const.CHARCODE;
    private final File F1 = Const.getTESTDATA_XML_1();
    private final File F2 = Const.getTESTDATA_XML_2();
    private final XmlLoader loader = new XmlLoader(CHARCODE);
    private final Document doc1 = loader.Load(F1);
    private final Document doc2 = loader.Load(F2);
    private final List<ChannelData> exp24 = TestData.get24Chs();
    private final List<ChannelData> exp22 = TestData.get22Chs();

    public AllChannelDataExtractorTest() {

    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    private void sortRersult(List<ChannelData> src) {
        src.sort((a, b) -> a.getId().compareTo(b.getId()));
    }

    @Test
    public void testSomeMethod1() {
        List<Document> docs = Collections.synchronizedList(new ArrayList<>());
        docs.add(doc1);
        docs.add(doc2);

        List<ChannelData> _expResult = new ArrayList<>();
        _expResult.addAll(exp24);
        _expResult.addAll(exp22);
        Set<ChannelData> set = new HashSet<>(_expResult);
        List<ChannelData> expResult = new ArrayList<>(set);
        this.sortRersult(expResult);
        LOG.info("想定");
        LOG.info(dumpList(expResult));
        LOG.info("想定");

        AllChannelDataExtractor target = new AllChannelDataExtractor(docs);
        List<ChannelData> chs = target.getAllEPGRecords();
        LOG.info("実際");
        LOG.info(dumpList(chs));
        LOG.info("実際");
        assertEquals(expResult, chs);
    }

    @Test
    public void testSomeMethod2_dedup() {
        List<Document> docs = Collections.synchronizedList(new ArrayList<>());
        docs.add(doc1);
        docs.add(doc1);
        docs.add(doc2);
        docs.add(doc2);

        List<ChannelData> _expResult = new ArrayList<>();
        _expResult.addAll(exp24);
        _expResult.addAll(exp22);
        Set<ChannelData> set = new HashSet<>(_expResult);
        List<ChannelData> expResult = new ArrayList<>(set);
        this.sortRersult(expResult);
        LOG.info("想定");
        LOG.info(dumpList(expResult));
        LOG.info("想定");

        AllChannelDataExtractor target = new AllChannelDataExtractor(docs);
        List<ChannelData> chs = target.getAllEPGRecords();
        LOG.info("実際");
        LOG.info(dumpList(chs));
        LOG.info("実際");

        assertEquals(expResult, chs);
    }
}
