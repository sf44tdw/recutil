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
package recutil.updatedb.dataextractor.programme;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.EqualsChecker;

/**
 *
 * @author normal
 */
public class ProgrammeDataTest {

    public ProgrammeDataTest() {
    }

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

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

    //チャンネルid
    private static final String ID = "DUMMY_ID";
    //番組id
    private static final long EVENT_ID = 33;
    //番組名
    private static final String TITLE = "DUMMY_TITLE";
    //開始時刻
    private static final Date START_DATETIME = new Timestamp(5348734846485336464L);
    //終了時刻
    private static final Date STOP_DATETIME = new Timestamp(6348734846485336464L);

    private ProgrammeData getInstance() {
        ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, TITLE, START_DATETIME, STOP_DATETIME);
        return temp;
    }

    //チャンネルid
    private static final String ID2 = "DUMMY_ID";
    //番組id
    private static final int EVENT_ID2 = 33;
    //番組名
    private static final String TITLE2 = "DUMMY_TITLE";
    //開始時刻
    private static final java.sql.Timestamp START_DATETIME2 = new Timestamp(8348734846485336464L);
    //終了時刻
    private static final java.sql.Timestamp STOP_DATETIME2 = new Timestamp(8358734846485336464L);

    private ProgrammeData getInstance2() {
        ProgrammeData temp = new ProgrammeData(ID2, EVENT_ID2, TITLE2, START_DATETIME2, STOP_DATETIME2);
        return temp;
    }

    @Test(expected = IllegalArgumentException.class)
    public void id_Null() {
        LOG.info("id_Null");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(null, EVENT_ID, TITLE, START_DATETIME, STOP_DATETIME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void id_Blank() {
        LOG.info("id_Blank");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData("", EVENT_ID, TITLE, START_DATETIME, STOP_DATETIME);
    }

    @Test
    public void event_Zero() {
        LOG.info("event_Zero");
		ProgrammeData temp = new ProgrammeData(ID, 0, TITLE, START_DATETIME, STOP_DATETIME);
		assertThat(temp, is(any(ProgrammeData.class)));

    }

    @Test(expected = IllegalArgumentException.class)
    public void event_Minus() {
        LOG.info("event_Minus");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, -1, TITLE, START_DATETIME, STOP_DATETIME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void title_Null() {
        LOG.info("title_Null");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, null, START_DATETIME, STOP_DATETIME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void title_Blank() {
        LOG.info("title_Blank");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, "", START_DATETIME, STOP_DATETIME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDatetime_Null() {
        LOG.info("startDatetime_Null");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, TITLE, null, STOP_DATETIME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopDatetime_Null() {
        LOG.info("stopDatetime_Null");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, TITLE, START_DATETIME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void allDatetime_Null() {
        LOG.info("allDatetime_Null");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, TITLE, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void Datetime_StopBeforeStart() {
        LOG.info("Datetime_StopBeforeStart");
        @SuppressWarnings("unused")
		ProgrammeData temp = new ProgrammeData(ID, EVENT_ID, TITLE, STOP_DATETIME, START_DATETIME);
    }

    /**
     * Test of getId method, of class Programme.
     */
    @Test
    public void testGetId() {
        LOG.info("getId");
        ProgrammeData instance = this.getInstance();
        String expResult = ID;
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventId method, of class Programme.
     */
    @Test
    public void testGetEventId() {
        LOG.info("getEventId");
        ProgrammeData instance = this.getInstance();
        long expResult = EVENT_ID;
        long result = instance.getEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTitle method, of class Programme.
     */
    @Test
    public void testGetTitle() {
        LOG.info("getTitle");
        ProgrammeData instance = this.getInstance();
        String expResult = TITLE;
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartDatetime method, of class Programme.
     */
    @Test
    public void testGetStartDatetime() {
        LOG.info("getStartDatetime");
        ProgrammeData instance = this.getInstance();
        Date expResult = START_DATETIME;
        Date result = instance.getStartDatetime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStopDatetime method, of class Programme.
     */
    @Test
    public void testGetStopDatetime() {
        LOG.info("getStopDatetime");
        ProgrammeData instance = this.getInstance();
        Date expResult = STOP_DATETIME;
        Date result = instance.getStopDatetime();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Programme.
     */
    @Test
    public void testHashCode() {
        LOG.info("hashCode");
        ProgrammeData instance = this.getInstance();
        int expResult = 0;
        int result = instance.hashCode();
        assertFalse(expResult == result);
    }

    /**
     * Test of equals method, of class Config.
     */
    @Test
    public void testEquals_Same() {
        LOG.info("equals_Same");
        EqualsChecker<ProgrammeData> ec = new EqualsChecker<>();
        ProgrammeData instance1 = this.getInstance();
        ProgrammeData instance2 = this.getInstance();
        ProgrammeData instance3 = this.getInstance();
        assertTrue(ec.check(instance1, instance2, instance3));
    }

    /**
     * Test of equals method, of class Config.
     */
    @Test
    public void testEquals_NotSame() {
        LOG.info("equals_Notsame");
        ProgrammeData instance1 = this.getInstance();
        ProgrammeData instance2 = this.getInstance2();
        assertFalse(instance1.equals(instance2));
        assertFalse(instance2.equals(instance1));
    }

    /**
     * Test of toString method, of class Programme.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        ProgrammeData instance = this.getInstance();
        String expResult = "";
        String result = instance.toString();
        assertFalse(expResult == result);
    }

}
