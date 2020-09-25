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

import static org.junit.Assert.*;
import static recutil.commmonutil.Util.*;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.w3c.dom.Document;

import loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;
import recutil.updatedb.common.TestData;
import recutil.updatedb.dataextractor.programme.ProgrammeData;
import recutil.updatedb.dataextractor.programme.ProgrammeDataExtractor;
import recutil.updatedb.listmaker.XmlLoader;

/**
 *
 * @author dosdiaopfhj
 */
public class ProgrammeDataExtractorTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    private final Charset CHARCODE = Const.CHARCODE;
    private final File F = Const.getTESTDATA_XML_1();

    public ProgrammeDataExtractorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {


        List<ProgrammeData> expResult = TestData.get24chProgramme();
        LOG.info(dumpList(expResult));
        XmlLoader loader = new XmlLoader(CHARCODE);
        Document doc = loader.Load(F);
        ProgrammeDataExtractor target = new ProgrammeDataExtractor(doc);
        List<ProgrammeData> prs = target.makeList();
        LOG.info(dumpList(prs));
        assertEquals(expResult, prs);
    }

}
