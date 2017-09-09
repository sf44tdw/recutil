/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.reserve;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.commandexecutor.CommandExecutor;
import recutil.commandexecutor.DummyExecutor;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.testdata.TestData;
import recutil.loggerconfigurator.LoggerConfigurator;
import static recutil.reserve.Main.getSep;

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

    public MainTest() {
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

    private final CommandExecutor exe = new DummyExecutor();

    private final String[] args_I = {"-i", TestData.CH3_ID};
    private final String[] args_V = {"-v", Integer.toString(TestData.PG2_EVENTID)};

    
    /**
     * Test of start method, of class Main.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStart1_1_1() throws Throwable {
        try {
            LOG.info("start1_1_1");
            String[] args = null;
            Main instance = new Main();
            instance.start(exe, args);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart1_1_2() throws Throwable {
        try {
            LOG.info("start1_1_2");
            String[] args = args_I;
            Main instance = new Main();
            instance.start(exe, args);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart1_1_3() throws Throwable {
        try {
            LOG.info("start1_1_3");
            String[] args = args_V;
            Main instance = new Main();
            instance.start(exe, args);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }



    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart3_1() throws Throwable {
        try {
            LOG.info("start3_1");
            String[] args = {"-i", TestData.CH6_ID, "-v", Integer.toString(TestData.PG9_EVENTID)};
            Main instance = new Main();
            instance.start(exe, args);
            String out = this.stdout.getOutput();
            assertFalse(out.contains("番組が見つかりませんでした"));
            String cmd = exe.getCmd();
            assertEquals(cmd, Main.RESERVE_COMMAND_PARAMS.RESERVE_COMMAND);
            String[] param = exe.getParam();
            assertEquals(param[0], Main.RESERVE_COMMAND_PARAMS.OPTION_TIME);
            assertEquals(param[2], Main.RESERVE_COMMAND_PARAMS.OPTION_FILE);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

}
