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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
public class ChannelDataTest {

	private static final Logger LOG = LoggerConfigurator.getCallerLogger();

	public ChannelDataTest() {
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

	private static final String DUMMY_ID = "DUMMY_ID";
	private static final long DUMMY_CH = 100;
	private static final String DUMMY_NAME = "DUMMY_NAME";

	private ChannelData getInstance() {
		ChannelData temp = new ChannelData(DUMMY_ID, DUMMY_CH, DUMMY_NAME);
		return temp;
	}

	private static final String DUMMY_ID2 = "DUMMY_ID2";
	private static final int DUMMY_CH2 = 200;
	private static final String DUMMY_NAME2 = "DUMMY_NAME2";

	private ChannelData getInstance2() {
		ChannelData temp = new ChannelData(DUMMY_ID2, DUMMY_CH2, DUMMY_NAME2);
		return temp;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testId_Null() {
		LOG.info("testId_Null");
		@SuppressWarnings("unused")
		ChannelData temp = new ChannelData(null, DUMMY_CH, DUMMY_NAME);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testId_blank() {
		LOG.info("testId_blank");
		@SuppressWarnings("unused")
		ChannelData temp = new ChannelData("", DUMMY_CH, DUMMY_NAME);
	}

	@Test
	public void testch_Zero() {
		LOG.info("testch_Zero");
		ChannelData temp = new ChannelData(DUMMY_ID, 0, DUMMY_NAME);
		assertThat(temp, is(any(ChannelData.class)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testch_Minus() {
		LOG.info("testch_Minus");
		@SuppressWarnings("unused")
		ChannelData temp2 = new ChannelData(DUMMY_ID, -1, DUMMY_NAME);
	}

	/**
	 * Test of getBroadcastingStationName method, of class Channel.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBroadcastingStationName_Null() {
		LOG.info("testBroadcastingStationName_Null");
		@SuppressWarnings("unused")
		ChannelData temp = new ChannelData(DUMMY_ID, DUMMY_CH, null);
	}

	/**
	 * Test of getBroadcastingStationName method, of class Channel.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBroadcastingStationName_Blank() {
		LOG.info("testBroadcastingStationName_Blank");
		@SuppressWarnings("unused")
		ChannelData temp = new ChannelData(DUMMY_ID, DUMMY_CH, "");
	}

	/**
	 * Test of getId method, of class Channel.
	 */
	@Test
	public void testGetId() {
		LOG.info("getId");
		ChannelData instance = this.getInstance();
		String expResult = DUMMY_ID;
		String result = instance.getId();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getPhysicalChannelNumber method, of class Channel.
	 */
	@Test
	public void testGetPhysicalChannelNumber() {
		LOG.info("getPhysicalChannelNumber");
		ChannelData instance = this.getInstance();
		long expResult = DUMMY_CH;
		long result = instance.getPhysicalChannelNumber();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getBroadcastingStationName method, of class Channel.
	 */
	@Test
	public void testGetBroadcastingStationName() {
		LOG.info("getBroadcastingStationName");
		ChannelData instance = this.getInstance();
		String expResult = DUMMY_NAME;
		String result = instance.getBroadcastingStationName();
		assertEquals(expResult, result);
	}

	/**
	 * Test of toString method, of class Channel.
	 */
	@Test
	public void testToString() {
		LOG.info("toString");
		ChannelData instance = this.getInstance();
		String expResult = "";
		String result = instance.toString();
		assertFalse((expResult == null ? result == null : expResult.equals(result)));
	}

	/**
	 * Test of hashCode method, of class Channel.
	 */
	@Test
	public void testHashCode() {
		LOG.info("hashCode");
		ChannelData instance = this.getInstance();
		int expResult = 0;
		int result = instance.hashCode();
		assertFalse(expResult == result);
	}

	/**
	 * Test of equals method, of class Channel.
	 */
	@Test
	public void testEquals_Same() {
		LOG.info("equals_Same");
		EqualsChecker<ChannelData> ec = new EqualsChecker<>();
		ChannelData t1 = this.getInstance();
		ChannelData t2 = this.getInstance();
		ChannelData t3 = this.getInstance();
		assertTrue(ec.check(t1, t2, t3));
	}

	@Test
	public void testEquals_NotSame() {
		LOG.info("equals_NotSame");
		ChannelData t1 = this.getInstance();
		ChannelData t2 = this.getInstance2();
		assertFalse(t1.equals(t2) && t2.equals(t1));

	}

}
