/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.executerecordcommand;

import static org.junit.Assert.*;
import static recutil.executerecordcommand.Main.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.commandexecutor.DummyExecutor;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.testdata.TestData;

/**
 *
 * @author normal
 */
public class MainTest {

	private static final Logger LOG = LoggerConfigurator.getCallerLogger();
	/**
	 * 標準出力・標準エラー出力変更管理オブジェクト.
	 */
	private ConsoleSnatcher stdout = ConsoleSnatcher.getlnstance();

	private final TestData dat = new TestData();

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	//ファイル名用に自身のプロセスIDを取得。
	private final String PID = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

	private Date nowTime = null;

	@Before
	public void setUp() {
		dat.reloadDB();
		dat.insertRecentStartProgrammes();
		nowTime = dat.getNowTime();
		//標準出力・標準エラー出力先を変更
		stdout.snatch();
	}

	@After
	public void tearDown() {
		//バッファにたまっている内容を一時退避
		String std = stdout.getOutput();
		String std_err = stdout.getErrorOutput();

		//コンソール出力に戻す
		stdout.release();

		// コンソール出力
		if (std.length() > 0) {
			LOG.info(getSep() + "--------");
			LOG.info(std);
		}
		if (std_err.length() > 0) {
			LOG.info(getSep() + "--------");
			LOG.info(std_err);
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = org.apache.commons.cli.ParseException.class)
	public void testStart1_1_1() throws Throwable {
		try {
			LOG.info("start1_1_1");
			String[] args = null;
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = org.apache.commons.cli.ParseException.class)
	public void testStart1_2_1() throws Throwable {
		try {
			LOG.info("start1_2_1");
			String[] args = { "-i", TestData.CH2_ID };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}
	//    /**
	//     * Test of start method, of class Main.
	//     */
	//    @Test(expected = org.apache.commons.cli.ParseException.class)
	//    public void testStart1_2_2() throws Throwable {
	//        try {
	//            LOG.info("start1_2_2");
	//            //カンマあり(10,000)に変換される。
	//            long x = 100022555550L;
	//            String[] args = {"-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "120"};
	//            Main instance = new Main();
	//            instance.start(new DummyExecutor(), PID, nowTime, args);
	//        } catch (Throwable ex) {
	//            LOG.error("エラー。", ex);
	//            throw ex;
	//        }
	//    }

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_4_1() throws Throwable {
		try {
			LOG.info("start1_4_1");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", "", "-s", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_1_1() throws Throwable {
		try {
			LOG.info("start1_5_1_1");
			long x = Integer.MAX_VALUE + 1;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_1_2() throws Throwable {
		try {
			LOG.info("start1_5_1_2");
			long x = -1;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_2_1() throws Throwable {
		try {
			LOG.info("start1_5_2_1");
			long x = (Integer.MAX_VALUE / 60) + 1;
			String[] args = { "-i", TestData.CH2_ID, "-m", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_2_2() throws Throwable {
		try {
			LOG.info("start1_5_2_2");
			long x = -1;
			String[] args = { "-i", TestData.CH2_ID, "-m", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_3_1() throws Throwable {
		try {
			LOG.info("start1_5_3_1");
			long x = (Integer.MAX_VALUE / 60 ^ 2) + 1;
			String[] args = { "-i", TestData.CH2_ID, "-h", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_5_3_2() throws Throwable {
		try {
			LOG.info("start1_5_3_2");
			long x = -1;
			String[] args = { "-i", TestData.CH2_ID, "-h", Long.toString(x), "-r", "60" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_6_1() throws Throwable {
		try {
			LOG.info("start1_6_1");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "-1" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_6_2() throws Throwable {
		try {
			LOG.info("start1_6_2");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "121" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main. パースで例外が起きる。
	 */
	@Test(expected = NullPointerException.class)
	public void testStart1_7_1() throws Throwable {
		try {
			LOG.info("start1_7_1");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "120", "-d", null };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_7_2() throws Throwable {
		try {
			LOG.info("start1_7_2");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "120", "-d", "" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testStart1_7_3() throws Throwable {
		try {
			LOG.info("start1_7_3");
			long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
			String[] args = { "-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "120", "-d", "wjwleuqlw" };
			Main instance = new Main();
			instance.start(new DummyExecutor(), PID, nowTime, args);
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart2_1_1() throws Throwable {
		try {
			LOG.info("start2_1_1");

			Programme exP = dat.getTestProgrammes().get(0);
			LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
			long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
			String[] args = { "-i", TestData.CH5_ID, "-s", LONG_TO_STRING.format(new Object[] { x }), "-r", "60" };
			Main instance = new Main();

			DummyExecutor de = new DummyExecutor();
			instance.start(de, PID, nowTime, args);
			final Object[] params = new Object[] { exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(),
					new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle() };
			String asT = new File(System.getProperty("user.home"), Main.FILENAME_FORMAT.format(params))
					.getAbsolutePath();
			LOG.info(asT);
			assertEquals(de.getCmd(), Main.RECORDCOMMAND);
			assertTrue(ArrayUtils.contains(de.getParam(), Main.STRIP_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Main.B25_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Integer.toString(TestData.CH5_CHNO)));
			assertTrue(ArrayUtils.contains(de.getParam(), Long.toString(x)));
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart2_1_2() throws Throwable {
		try {
			LOG.info("start2_1_2");

			Programme exP = dat.getTestProgrammes().get(0);
			LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
			long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
			String[] args = { "-i", TestData.CH5_ID, "-s", LONG_TO_STRING.format(new Object[] { x }), "-r", "120" };
			Main instance = new Main();

			DummyExecutor de = new DummyExecutor();
			instance.start(de, PID, nowTime, args);
			final Object[] params = new Object[] { exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(),
					new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle() };
			String asT = new File(System.getProperty("user.home"), Main.FILENAME_FORMAT.format(params))
					.getAbsolutePath();
			LOG.info(asT);
			assertEquals(de.getCmd(), Main.RECORDCOMMAND);
			assertTrue(ArrayUtils.contains(de.getParam(), Main.STRIP_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Main.B25_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Integer.toString(TestData.CH5_CHNO)));
			assertTrue(ArrayUtils.contains(de.getParam(), Long.toString(x)));
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}

	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart2_2_2() throws Throwable {
		try {
			LOG.info("start2_2_2");

			Programme exP = dat.getTestProgrammes().get(0);
			LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
			long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
			String[] args = { "-i", TestData.CH5_ID, "-s", LONG_TO_STRING.format(new Object[] { x }) };
			Main instance = new Main();

			DummyExecutor de = new DummyExecutor();
			instance.start(de, PID, nowTime, args);
			final Object[] params = new Object[] { exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(),
					new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle(),
					System.getProperty("user.home") };
			String asT = new File(System.getProperty("user.home"), Main.FILENAME_FORMAT.format(params))
					.getAbsolutePath();
			LOG.info(asT);
			assertEquals(de.getCmd(), Main.RECORDCOMMAND);
			assertTrue(ArrayUtils.contains(de.getParam(), Main.STRIP_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Main.B25_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Integer.toString(TestData.CH5_CHNO)));
			assertTrue(ArrayUtils.contains(de.getParam(), Long.toString(x)));
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart2_2_3() throws Throwable {
		try {
			LOG.info("start2_2_3");

			Programme exP = dat.getTestProgrammes().get(0);
			LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
			long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
			String[] args = { "-i", TestData.CH5_ID, "-s", LONG_TO_STRING.format(new Object[] { x }), "-d",
					System.getProperty("user.dir") };
			Main instance = new Main();

			DummyExecutor de = new DummyExecutor();
			instance.start(de, PID, nowTime, args);
			final Object[] params = new Object[] { exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(),
					new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle(),
					System.getProperty("user.home") };
			String asT = new File(System.getProperty("user.dir"), Main.FILENAME_FORMAT.format(params))
					.getAbsolutePath();
			LOG.info(asT);
			assertEquals(de.getCmd(), Main.RECORDCOMMAND);
			assertTrue(ArrayUtils.contains(de.getParam(), Main.STRIP_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Main.B25_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Integer.toString(TestData.CH5_CHNO)));
			assertTrue(ArrayUtils.contains(de.getParam(), Long.toString(x)));
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}

	/**
	 * Test of start method, of class Main.
	 */
	@Test
	public void testStart2_2_4() throws Throwable {
		try {
			LOG.info("start2_2_4");

			Programme exP = dat.getTestProgrammes().get(0);
			LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
			long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
			String[] args = { "-i", TestData.CH5_ID, "-s", LONG_TO_STRING.format(new Object[] { x }), "-d",
					System.getProperty("user.dir"), "-p", System.getProperty("user.dir") };
			Main instance = new Main();

			DummyExecutor de = new DummyExecutor();
			instance.start(de, PID, nowTime, args);
			final Object[] params = new Object[] { exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(),
					new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle(),
					System.getProperty("user.home") };
			String asT = new File(System.getProperty("user.dir"), Main.FILENAME_FORMAT.format(params))
					.getAbsolutePath();
			String command_exp=new File(System.getProperty("user.dir"),Main.RECORDCOMMAND).getAbsolutePath();
			LOG.info(asT);
			assertEquals(de.getCmd(), command_exp);
			assertTrue(ArrayUtils.contains(de.getParam(), Main.STRIP_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Main.B25_OPTION));
			assertTrue(ArrayUtils.contains(de.getParam(), Integer.toString(TestData.CH5_CHNO)));
			assertTrue(ArrayUtils.contains(de.getParam(), Long.toString(x)));
		} catch (Throwable ex) {
			LOG.error("エラー。", ex);
			throw ex;
		}
	}
}
