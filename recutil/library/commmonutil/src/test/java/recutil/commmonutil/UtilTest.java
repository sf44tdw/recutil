/*
 * Copyright (C) 2017 normal
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
package recutil.commmonutil;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class UtilTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public UtilTest() {
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

    @Test
    public void testParseLongToDate() {
        LOG.info("testParseLongToDate");
        long x = 7997762100000L;
        assertEquals(x, Util.parseLongToDate(x).getTime());
    }

    @Test
    public void testParseDateToString() {
        LOG.info("testParseDateToString");
        String ex = "2223/06/11_01:35:00";
        Date x = new Date(7997762100000L);
        assertEquals(ex, Util.parseDateToString(x));

    }

}
