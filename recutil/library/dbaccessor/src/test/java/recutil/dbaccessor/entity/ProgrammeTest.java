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
package recutil.dbaccessor.entity;

import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class ProgrammeTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public ProgrammeTest() {
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
     * Test of getId method, of class Programme.
     */
    @Test
    public void testGetId() {
        LOG.info("getId");
        Programme instance = new Programme();
        Long expResult = 1234567890L;
        instance.setId(expResult);
        Long result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getEventId method, of class Programme.
     */
    @Test
    public void testGetEventId() {
        LOG.info("getEventId");
        Programme instance = new Programme();
        int expResult = 0;
        instance.setEventId(expResult);
        long result = instance.getEventId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTitle method, of class Programme.
     */
    @Test
    public void testGetTitle() {
        LOG.info("getTitle");
        Programme instance = new Programme();
        String expResult = "DUMMY_TITLE";
        instance.setTitle(expResult);
        String result = instance.getTitle();
        assertEquals(expResult, result);
    }

    private void testTime1(Programme instance, BiConsumer<Programme, Date> setter, Function<Programme, Date> getter) {
        Date expResult = new Date(1470700800000L);
        setter.accept(instance, expResult);
        Date result1 = getter.apply(instance);
        assertEquals(expResult, result1);
        result1.setTime(1470805200000L);
        Date result2 = getter.apply(instance);
        assertNotEquals(result1, result2);
    }

    /**
     * Test of getStartDatetime method, of class Programme.
     */
    @Test
    public void testGetStartDatetime1() {
        LOG.info("getStartDatetime1");
        Programme instance = new Programme();
        BiConsumer<Programme, Date> setter = (programme, startDateTime) -> programme.setStartDatetime(startDateTime);
        Function<Programme, Date> getter = (programme) -> {
            return programme.getStartDatetime();
        };
        this.testTime1(instance, setter, getter);
    }

    /**
     * Test of getStartDatetime method, of class Programme.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStarStoptDatetime1() {
        LOG.info("StartStopDatetime1");
        Programme instance = new Programme();
        Date start = new Date(1470700800000L);
        assertNull(instance.getStartDatetime());
        assertNull(instance.getStopDatetime());
        instance.setStartDatetime(start);
        instance.setStopDatetime(start);
    }

    /**
     * Test of getStartDatetime method, of class Programme.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStarStoptDatetime2() {
        LOG.info("StartStopDatetime2");
        Programme instance = new Programme();
        Date start = new Date(1470700800000L);
        Date stop = new Date(start.getTime() - 1);
        assertNull(instance.getStartDatetime());
        assertNull(instance.getStopDatetime());
        instance.setStartDatetime(start);
        instance.setStopDatetime(stop);
    }

    /**
     * Test of getStartDatetime method, of class Programme.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStarStoptDatetime3() {
        LOG.info("StartStopDatetime3");
        Programme instance = new Programme();
        Date start = new Date(1470700800000L);
        Date stop = new Date(start.getTime() - 1);
        assertNull(instance.getStartDatetime());
        assertNull(instance.getStopDatetime());
        instance.setStopDatetime(stop);
        instance.setStartDatetime(start);
    }

    /**
     * Test of getStopDatetime method, of class Programme.
     */
    @Test
    public void testGetStopDatetime1() {
        LOG.info("getStopDatetime1");
        Programme instance = new Programme();
        BiConsumer<Programme, Date> setter = (programme, stopDateTime) -> programme.setStopDatetime(stopDateTime);
        Function<Programme, Date> getter = (programme) -> {
            return programme.getStopDatetime();
        };
        this.testTime1(instance, setter, getter);
    }

    /**
     * Test of getChannelId method, of class Programme.
     */
    @Test
    public void testGetChannelId() {
        LOG.info("getChannelId");
        Programme instance = new Programme();
        Channel expResult = new Channel();
        expResult.setChannelId("DUMMY_ID");
        expResult.setChannelNo(100);
        expResult.setDisplayName("DUMMY_NAME");
        instance.setChannelId(expResult);
        Channel result = instance.getChannelId();
        assertEquals(expResult, result);
    }

}
