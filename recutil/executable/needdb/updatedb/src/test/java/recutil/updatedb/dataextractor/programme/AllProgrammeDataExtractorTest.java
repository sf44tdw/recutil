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
package recutil.updatedb.dataextractor.programme;

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
public class AllProgrammeDataExtractorTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    private final Charset CHARCODE = Const.CHARCODE;
    private final File F1 = Const.getTESTDATA_XML_1();
    private final File F2 = Const.getTESTDATA_XML_2();
    private final XmlLoader loader = new XmlLoader(CHARCODE);
    private final Document doc1 = loader.Load(F1);
    private final Document doc2 = loader.Load(F2);

    public AllProgrammeDataExtractorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private void sortRersult(List<ProgrammeData> dataList) {
        dataList.sort((a, b) -> a.getStartDatetime().compareTo(b.getStartDatetime()));
        dataList.sort((a, b) -> Long.compare(a.getEventId(), b.getEventId()));
        dataList.sort((a, b) -> a.getId().compareTo(b.getId()));
    }

    @Test
    public void testSomeMethod1() {
        List<Document> docs = Collections.synchronizedList(new ArrayList<>());
        docs.add(doc1);
        docs.add(doc2);

        List<ProgrammeData> exp24 = TestData.get24chProgramme();
        List<ProgrammeData> exp22 = TestData.get22chProgramme();
        List<ProgrammeData> _expResult = new ArrayList<>();
        _expResult.addAll(exp24);
        _expResult.addAll(exp22);
        Set<ProgrammeData> set = new HashSet<>(_expResult);
        List<ProgrammeData> expResult = new ArrayList<>(set);
        this.sortRersult(expResult);
        LOG.info("想定");
        LOG.info(dumpList(expResult));
        LOG.info("想定");

        AllProgrammeDataExtractor target = new AllProgrammeDataExtractor(docs);
        List<ProgrammeData> prs = target.getAllEPGRecords();
        LOG.info("実際");
        LOG.info(dumpList(prs));
        LOG.info("実際");

        assertEquals(expResult, prs);
    }

    @Test
    public void testSomeMethod2_dedup() {
        List<Document> docs = Collections.synchronizedList(new ArrayList<>());
        docs.add(doc1);
        docs.add(doc1);
        docs.add(doc2);
        docs.add(doc2);

        List<ProgrammeData> exp24 = TestData.get24chProgramme();
        List<ProgrammeData> exp22 = TestData.get22chProgramme();
        List<ProgrammeData> _expResult = new ArrayList<>();
        _expResult.addAll(exp24);
        _expResult.addAll(exp22);
        Set<ProgrammeData> set = new HashSet<>(_expResult);
        List<ProgrammeData> expResult = new ArrayList<>(set);
        this.sortRersult(expResult);
        LOG.info("想定");
        LOG.info(dumpList(expResult));
        LOG.info("想定");

        AllProgrammeDataExtractor target = new AllProgrammeDataExtractor(docs);
        List<ProgrammeData> prs = target.getAllEPGRecords();
        LOG.info("実際");
        LOG.info(dumpList(prs));
        LOG.info("実際");

        assertEquals(expResult, prs);
    }
}
