/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.executerecordcommand;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.commandexecutor.DummyExecutor;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.testdata.TestData;
import static recutil.executerecordcommand.Main.DATE_PATTERN;
import static recutil.executerecordcommand.Main.getSep;
import recutil.loggerconfigurator.LoggerConfigurator;

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

    private static final String PG_AFTER_60 = "AFTER_60_SECOND";
    private static final String PG_AFTER_120 = "AFTER_120_SECOND";
    private static final String PG_AFTER_180 = "AFTER_180_SECOND";

    private static final long ONE_MINUTE_IN_MILLIS = (60L * 1000L);

    private Date nowTime = null;

    //ファイル名用に自身のプロセスIDを取得。
    private final String PID = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

    private List<Programme> getTestProgrammes() {
        dat.make();
        List<Programme> programmeList = new ArrayList<>();

        ZonedDateTime d = ZonedDateTime.now();
        ZonedDateTime d2 = d.truncatedTo(ChronoUnit.MINUTES);
        nowTime = Date.from(d2.toInstant());

        final long pp = (30L * 1000L);

        int id = 1000000;

//今から1分後に始まる番組。
        final Date after60sec = new Date((nowTime.getTime() + ONE_MINUTE_IN_MILLIS));
        final Programme p60 = dat.getPg(dat.getCh5(), id++, PG_AFTER_60, after60sec, new Date(after60sec.getTime() + pp));
        programmeList.add(p60);

//同、2分後。
        final Date after120sec = new Date((after60sec.getTime() + ONE_MINUTE_IN_MILLIS));
        final Programme p120 = dat.getPg(dat.getCh5(), id++, PG_AFTER_120, after120sec, new Date(after120sec.getTime() + pp));
        programmeList.add(p120);

        //同、3分後。
        final Date after180sec = new Date((after120sec.getTime() + ONE_MINUTE_IN_MILLIS));
        final Programme p180 = dat.getPg(dat.getCh5(), id++, PG_AFTER_180, after180sec, new Date(after180sec.getTime() + pp));
        programmeList.add(p180);

        return programmeList;
    }

    @Before
    public void setUp() {
        dat.reloadDB();

        try (EntityManagerMaker mk = new EntityManagerMaker()) {
            EntityManager man = mk.getEntityManager();
            EntityTransaction trans = man.getTransaction();
            trans.begin();
            LOG.info("番組登録トランザクション開始。");
            dat.insertPgs(man, this.getTestProgrammes());
            trans.commit();
            LOG.info("除外チャンネル登録トランザクションコミット。");
            man.close();
        }

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
            String[] args = {"-i", TestData.CH2_ID};
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
    public void testStart1_4_1() throws Throwable {
        try {
            LOG.info("start1_4_1");
            long x = (TestData.PG1_STOP_TIME - TestData.PG1_START_TIME) / 1000;
            String[] args = {"-i", "", "-s", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-m", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-m", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-h", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-h", Long.toString(x), "-r", "60"};
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
            String[] args = {"-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "-1"};
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
            String[] args = {"-i", TestData.CH2_ID, "-s", Long.toString(x), "-r", "121"};
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

            Programme exP = this.getTestProgrammes().get(0);
            LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
            long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
            String[] args = {"-i", TestData.CH5_ID, "-s", Long.toString(x), "-r", "60"};
            Main instance = new Main();

            DummyExecutor de = new DummyExecutor();
            instance.start(de, PID, nowTime, args);
            final Object[] params = new Object[]{exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(), new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle()};
            String asT = new File (System.getProperty("user.home"),Main.FILENAME_FORMAT.format(params)).getAbsolutePath();
            LOG.info(asT);
            assertTrue(ArrayUtils.contains(de.getParam(), asT));
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

            Programme exP = this.getTestProgrammes().get(0);
            LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
            long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
            String[] args = {"-i", TestData.CH5_ID, "-s", Long.toString(x), "-r", "120"};
            Main instance = new Main();

            DummyExecutor de = new DummyExecutor();
            instance.start(de, PID, nowTime, args);
            final Object[] params = new Object[]{exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(), new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle()};
            String asT = new File (System.getProperty("user.home"),Main.FILENAME_FORMAT.format(params)).getAbsolutePath();
            LOG.info(asT);
            assertTrue(ArrayUtils.contains(de.getParam(), asT));
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

            Programme exP = this.getTestProgrammes().get(0);
            LOG.info(ReflectionToStringBuilder.reflectionToString(exP));
            long x = (exP.getStopDatetime().getTime() - exP.getStartDatetime().getTime()) / 1000;
            String[] args = {"-i", TestData.CH5_ID, "-s", Long.toString(x)};
            Main instance = new Main();

            DummyExecutor de = new DummyExecutor();
            instance.start(de, PID, nowTime, args);
            final Object[] params = new Object[]{exP.getChannelId().getChannelId(), exP.getChannelId().getChannelNo(), new SimpleDateFormat(DATE_PATTERN).format(nowTime), PID, exP.getTitle(),System.getProperty("user.home")};
            String asT = new File (System.getProperty("user.home"),Main.FILENAME_FORMAT.format(params)).getAbsolutePath();
            LOG.info(asT);
            assertTrue(ArrayUtils.contains(de.getParam(), asT));
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
}
