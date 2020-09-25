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

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import recutil.commmonutil.Util.REPLACE_PAIR;

/**
 *
 * @author normal
 */
public class UtilTest {

	private static final Logger LOG = loggerconfigurator.LoggerConfigurator.getlnstance().getCallerLogger();

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

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_1() throws Throwable {
		try {
			LOG.info("start3_1_1");

			String src = " a bあ　い　";
			String dest = Util.quoteString(src);
			assertEquals(dest, "_a_bあ_い_");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_2() throws Throwable {
		try {
			LOG.info("start3_1_2");

			String src = "\"a\"b\"";
			String dest = Util.quoteString(src);
			assertEquals(dest, "_a_b_");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_3() throws Throwable {
		try {
			LOG.info("start3_1_3");

			String src = "\'a\'b\'";
			String dest = Util.quoteString(src);
			assertEquals(dest, "_a_b_");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_4() throws Throwable {
		try {
			LOG.info("start3_1_4");

			String src = "a”b";
			String dest = Util.quoteString(src);
			assertEquals(dest, "a_b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_5() throws Throwable {
		try {
			LOG.info("start3_1_5");

			String src = "a“b";
			String dest = Util.quoteString(src);
			assertEquals(dest, "a_b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_6() throws Throwable {
		try {
			LOG.info("start3_1_6");

			String src = "a’b";
			String dest = Util.quoteString(src);
			assertEquals(dest, "a_b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_7() throws Throwable {
		try {
			LOG.info("start3_1_7");

			String src = "a'b";
			String dest = Util.quoteString(src);
			assertEquals(dest, "a_b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_1_8() throws Throwable {
		try {
			LOG.info("start3_1_8");

			String src = "a‘b";
			String dest = Util.quoteString(src);
			assertEquals(dest, "a_b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_1() throws Throwable {
		try {
			LOG.info("start3_2_1");
			REPLACE_PAIR pair = REPLACE_PAIR.WAVE_DASH;
			String src = "a〜ba～b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "ba" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_2() throws Throwable {
		try {
			LOG.info("start3_2_2");
			REPLACE_PAIR pair = REPLACE_PAIR.ZENKAKU_MINUS;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_3() throws Throwable {
		try {
			LOG.info("start3_2_3");
			REPLACE_PAIR pair = REPLACE_PAIR.CENT;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_4() throws Throwable {
		try {
			LOG.info("start3_2_4");
			REPLACE_PAIR pair = REPLACE_PAIR.POUND;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_5() throws Throwable {
		try {
			LOG.info("start3_2_5");
			REPLACE_PAIR pair = REPLACE_PAIR.NOT;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_6() throws Throwable {
		try {
			LOG.info("start3_2_6");
			REPLACE_PAIR pair = REPLACE_PAIR.DASH;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_7() throws Throwable {
		try {
			LOG.info("start3_2_7");
			REPLACE_PAIR pair = REPLACE_PAIR.PARALLEL;
			String src = "a" + pair.getSrc() + "b";
			String dest = Util.replaceProhibitedCharacter(src);
			assertEquals(dest, "a" + pair.getDest() + "b");
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart3_2_8() throws Throwable {
		try {
			LOG.info("start3_2_8");
			StringBuilder srcs = new StringBuilder();
			StringBuilder dests = new StringBuilder();

			List<REPLACE_PAIR> rpList = new ArrayList<>();
			rpList.add(REPLACE_PAIR.ZENKAKU_MINUS);
			rpList.add(REPLACE_PAIR.CENT);
			rpList.add(REPLACE_PAIR.POUND);
			rpList.add(REPLACE_PAIR.NOT);
			rpList.add(REPLACE_PAIR.DASH);
			rpList.add(REPLACE_PAIR.PARALLEL);

			for (REPLACE_PAIR pair : rpList) {
				srcs.append(pair.getSrc());
				dests.append(pair.getDest());
			}

			srcs.append("〜～");
			dests.append(REPLACE_PAIR.WAVE_DASH.getDest());
			dests.append(REPLACE_PAIR.WAVE_DASH.getDest());

			String srcs_s = new String(srcs);
			String dests_s = new String(dests);
			String dest = Util.replaceProhibitedCharacter(srcs_s);
			assertEquals(dests_s, dest);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	@Test
	public void testStartRelativePahtToAbsolutePath_1_1() {
		LOG.info("testStartRelativePahtToAbsolutePath_1_1");
		final File pathF = Util.relativePahtToAbsolutePath("notfoundpath");
		assertNull(pathF);
	}

	@Test
	public void testStartRelativePahtToAbsolutePath_1_2() {
		LOG.info("testStartRelativePahtToAbsolutePath_1_2");
		final File pathF = Util.relativePahtToAbsolutePath("");
		assertNull(pathF);
	}

	@Test
	public void testStartRelativePahtToAbsolutePath_1_3() {
		LOG.info("testStartRelativePahtToAbsolutePath_1_3");
		final File pathF = Util.relativePahtToAbsolutePath(null);
		assertNull(pathF);
	}

	@Test
	public void testStartRelativePahtToAbsolutePath_2() {
		LOG.info("testStartRelativePahtToAbsolutePath_2");
		final String fileName="test_dummy";
		final String userDir=System.getProperty("user.dir");

		final String fullPath_exp=new File(userDir,fileName).getAbsolutePath();
		final String fullPath=Util.relativePahtToAbsolutePath(fileName).getAbsolutePath();
		assertEquals(fullPath_exp, fullPath);
	}

}
