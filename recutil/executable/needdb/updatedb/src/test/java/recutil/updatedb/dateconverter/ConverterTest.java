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
package recutil.updatedb.dateconverter;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class ConverterTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

    public ConverterTest() {
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
     * Test of stringTOSqlDate method, of class Converter.
     */
    @Test
    public void testStringTOSqlDate1() {
        LOG.info("stringTOSqlDate1");
        String source = "2014-12-29 12:00";
        String datePattern = "yyyy-MM-dd HH:mm";
        Date expResult = new Date(1419822000000L);
        Date result = Converter.stringToDate(source, datePattern);
        assertEquals(expResult, result);
    }

    /**
     * Test of stringTOSqlDate method, of class Converter.
     */
    @Test
    public void testStringTOSqlDate2() {
        LOG.info("stringTOSqlDate2");
        String source = "2014-12-29 12:00";
        String datePattern = "yyyy-MM-dd HH:mm:ss";
        Date result = Converter.stringToDate(source, datePattern);
        assertNull(result);
    }

    /**
     * Test of stringTOSqlDate method, of class Converter.
     */
    @Test
    public void testStringTOSqlDate3() {
        LOG.info("stringTOSqlDate3");
        //12月32日にすると翌年の1月1日になる。
        String source = "2014-12-aa 12:00";
        String datePattern = "yyyy-MM-dd HH:mm";
        Date result = Converter.stringToDate(source, datePattern);
        assertNull(result);
    }
}
