/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.getprogramme;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static recutil.commmonutil.Util.*;
import static recutil.getprogramme.Main.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.entity.comparator.PrograammeListSorter;
import recutil.dbaccessor.testdata.TestData;
import recutil.getprogramme.Main.OUTPUT_FORMAT_TYPE;

/**
 *
 * @author normal
 */
public class MainTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
    /**
     * 標準出力・標準エラー出力変更管理オブジェクト.
     */
    private ConsoleSnatcher stdout = ConsoleSnatcher.getlnstance();

    private final TestData dat = new TestData();

    private final Programme PG1;
//    private final Programme PG2;
    private final Programme PG4;

    public MainTest() {
        synchronized (dat) {
            dat.make();
            PG1 = dat.getPg1();
//            PG2 = dat.getPg2();
            PG4 = dat.getPg4();
            dat.make();
        }

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dat.reloadDB();
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
    @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
    public void testStart1_1_1() throws Exception {
        LOG.info("start1_1_1");
        String[] args = {"-e", "-i", TestData.CH3_ID, "-v", Integer.toString(TestData.PG2_EVENTID), "-d", parseLongToString(TestData.PG2_START_TIME)};
        try {
            Main instance = new Main();
            instance.start(args);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
    public void testStart1_1_2() throws Exception {
        LOG.info("start1_1_2");

        String[] args = new String[]{"-i", TestData.CH3_ID, "-v", Integer.toString(TestData.PG2_EVENTID), "-d", parseLongToString(TestData.PG2_START_TIME)};
        try {
            Main instance = new Main();
            instance.start(args);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.AlreadySelectedException.class)
    public void testStart1_1_3() throws Exception {
        LOG.info("start1_1_3");
        String[] args = {"-v", Integer.toString(TestData.PG2_EVENTID), "-d", parseLongToString(TestData.PG2_START_TIME)};
        try {
            Main instance = new Main();
            instance.start(args);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = java.lang.NumberFormatException.class)
    public void testStart1_2_1() throws Exception {
        LOG.info("start1_2_1");
        String[] args = {"-v", "aa"};
        try {
            Main instance = new Main();
            instance.start(args);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStart1_2_2() throws Exception {
        LOG.info("start1_2_2");
        String[] args = {"-d", "sdf3we"};
        try {
            Main instance = new Main();
            instance.start(args);
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    private String getExpRes_NotOnlyTitle_NotFirstOnly(List<Programme> expProg) {
        PrograammeListSorter.sortRes(expProg);
        return Main.printRes(expProg, OUTPUT_FORMAT_TYPE.DEFAULT, Main.firstOnlyState.ALL);
    }

    private String getExpRes_OnlyTitle_FirstOnly(List<Programme> expProg) {
        PrograammeListSorter.sortRes(expProg);
        return Main.printRes(expProg, OUTPUT_FORMAT_TYPE.TITLE_ONLY, Main.firstOnlyState.FIRST_ONLY);
    }

    private String getAllPG() {
        return getExpRes_NotOnlyTitle_NotFirstOnly(dat.getAllProgrammeList());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_1() throws Exception {
        LOG.info("start2_1");
        String[] args = null;
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = getAllPG();

        LOG.info("*****************************************************");
        LOG.info(std);
        LOG.info("{}", std.length());
        LOG.info("*****************************************************");
        LOG.info(std_err);
        LOG.info("*****************************************************");
        LOG.info(exp);
        LOG.info("{}", exp.length());
        LOG.info("*****************************************************");
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));

    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_2() throws Exception {
        LOG.info("start2_2");
        String[] args = {};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = getAllPG();
        LOG.info("*****************************************************");
        LOG.info(std);
        LOG.info("{}", std.length());
        LOG.info("*****************************************************");
        LOG.info(std_err);
        LOG.info("*****************************************************");
        LOG.info(exp);
        LOG.info("{}", exp.length());
        LOG.info("*****************************************************");

        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));

    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_3() throws Exception {
        LOG.info("start2_3");
        String[] args = {"-t", "-f"};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = this.getExpRes_OnlyTitle_FirstOnly(dat.getAllProgrammeList());

        LOG.info("*****************************************************");
        LOG.info(std);
        LOG.info("{}", std.length());
        LOG.info("*****************************************************");
        LOG.info(std_err);
        LOG.info("*****************************************************");
        LOG.info(exp);
        LOG.info("{}", exp.length());
        LOG.info("*****************************************************");
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));

    }

    private String getNotExcludedPG() {
        List<Programme> exp = dat.getUseableProgrammeList();
        return getExpRes_NotOnlyTitle_NotFirstOnly(exp);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart3_1() throws Exception {
        LOG.info("start3_1");
        String[] args = {"-e"};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = getNotExcludedPG();
        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }

    private String getPG1_4() {
        List<Programme> exp = new ArrayList<>();
        exp.add(this.PG1);
        exp.add(PG4);
        return getExpRes_NotOnlyTitle_NotFirstOnly(exp);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart4_1() throws Exception {
        LOG.info("start4_1");
        String[] args = {"-i", TestData.CH2_ID};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = getPG1_4();
        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }

    private String getPG4() {
        List<Programme> exp = new ArrayList<>();
        exp.add(PG4);
        return getExpRes_NotOnlyTitle_NotFirstOnly(exp);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart5_1_1() throws Exception {
        LOG.info("start5_1_1");
        String[] args = {"-v", Integer.toString(TestData.PG4_EVENTID)};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();
        String exp = getPG4();
        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart5_2_1() throws ParseException {
        LOG.info("start5_2_1");
        String[] args = {"-d", parseLongToString(TestData.PG4_START_TIME)};
        try {
            Main instance = new Main();
            instance.start(args);
            String std = stdout.getOutput();
            String std_err = stdout.getErrorOutput();
            String exp = getPG4();
            LOG.info("std={}", std);
            LOG.info("exp={}", exp);
            assertEquals(std, exp);
            assertThat(std_err.length(), is(0));
        } catch (Throwable ex) {
            LOG.error("エラー。 引数 = " + ArrayUtils.toString(args, "引数なし。"), ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart5_3_1() throws Exception {
        LOG.info("start5_3_1");

        dat.insertRecentStartProgrammes();

        String[] args = {"-d", parseLongToString(dat.getP180().getStartDatetime().getTime()), "-s", "60"};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        List<Programme> expl = new ArrayList<>();
        expl.add(dat.getP120());
        expl.add(dat.getP180());
        expl.add(dat.getP240());
        String exp = getExpRes_NotOnlyTitle_NotFirstOnly(expl);


        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }
    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart5_3_2() throws Exception {
        LOG.info("start5_3_2");

        dat.insertRecentStartProgrammes();

        String[] args = {"-d", parseLongToString(dat.getP1800().getStartDatetime().getTime()), "-m", "10"};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        List<Programme> expl = new ArrayList<>();
        expl.add(dat.getP1200());
        expl.add(dat.getP1800());
        expl.add(dat.getP2400());
        String exp = getExpRes_NotOnlyTitle_NotFirstOnly(expl);


        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }

        /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart5_3_3() throws Exception {
        LOG.info("start5_3_3");

        dat.insertRecentStartProgrammes();

        String[] args = {"-d", parseLongToString(dat.getP18000().getStartDatetime().getTime()), "-h", "1"};
        Main instance = new Main();
        instance.start(args);
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        List<Programme> expl = new ArrayList<>();
        expl.add(dat.getP14400());
        expl.add(dat.getP18000());
        expl.add(dat.getP21600());
        String exp = getExpRes_NotOnlyTitle_NotFirstOnly(expl);


        LOG.info("std={}", std);
        LOG.info("exp={}", exp);
        assertEquals(std, exp);
        assertThat(std_err.length(), is(0));
    }
}
