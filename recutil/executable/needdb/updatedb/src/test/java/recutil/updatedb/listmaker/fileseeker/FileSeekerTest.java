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
package recutil.updatedb.listmaker.fileseeker;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;
import recutil.updatedb.common.util.Util;
import recutil.updatedb.listmaker.XmlSuffix;

/**
 *
 * @author normal
 */
public class FileSeekerTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public FileSeekerTest() {
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
     * Test of isRecursive method, of class FileSeeker.
     */
    @Test
    public void testIsRecursive() {
        LOG.info("isRecursive");
        FileSeeker instance = new FileSeeker(Const.getXMLTESTDATADIR(), XmlSuffix.getFilter());
        assertEquals(true, instance.isRecursive());
    }

    /**
     * Test of setRecursive method, of class FileSeeker.
     */
    @Test
    public void testSetRecursive() {
        LOG.info("setRecursive");
        FileSeeker instance = new FileSeeker(Const.getXMLTESTDATADIR(), XmlSuffix.getFilter());
        assertEquals(true, instance.isRecursive());
        instance.setRecursive(false);
        assertEquals(false, instance.isRecursive());
    }

    /**
     * Test of seek method, of class FileSeeker.
     */
    @Test
    public void testSeek_rec() {
        LOG.info("seek_rec");

        //File testDataDir = Const.getTESTDATADIR();
        //File recursiveDirConst = Const.getXMLTESTDATADIR_RECURSIVE();

        FileSeeker instance = new FileSeeker(Const.getXMLTESTDATADIR(), XmlSuffix.getFilter());
        instance.setRecursive(true);
        assertEquals(true, instance.isRecursive());
        List<File> expResult = new ArrayList<>();
        expResult.add(Const.getTESTDATA_XML_1());
        expResult.add(Const.getTESTDATA_XML_2());
        expResult.add(Const.getTESTDATA_XML_3());

        List<File> result = instance.seek();

        assertTrue(Util.isSameFileList(expResult, result));
    }

    /**
     * Test of seek method, of class FileSeeker.
     */
    @Test
    public void testSeek_notrec() {
        LOG.info("seek_notrec");

        //File testDataDir = Const.getTESTDATADIR();
        //File recursiveDirConst = Const.getXMLTESTDATADIR_RECURSIVE();

        FileSeeker instance = new FileSeeker(Const.getXMLTESTDATADIR(), XmlSuffix.getFilter());
        instance.setRecursive(false);
        assertEquals(false, instance.isRecursive());
        List<File> expResult = new ArrayList<>();
        expResult.add(Const.getTESTDATA_XML_1());
        expResult.add(Const.getTESTDATA_XML_2());

        List<File> result = instance.seek();

        assertTrue(Util.isSameFileList(expResult, result));
    }

}
