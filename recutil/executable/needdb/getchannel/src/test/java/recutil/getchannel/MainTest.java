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
package recutil.getchannel;

import static org.junit.Assert.*;
import static recutil.commmonutil.Util.*;
import static recutil.getchannel.Main.*;

import java.text.MessageFormat;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.testdata.TestData;

/**
 *
 * @author normal
 */
public class MainTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
    private static final MessageFormat FORMAT = new MessageFormat(OUTPUT_FORMAT);
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
        // 標準出力・標準エラー出力先を変更
        stdout.snatch();
    }

    private String DEFAULT_LINE_SEPARATOR = getDefaultLineSeparator();

    @After
    public void tearDown() {
        // バッファにたまっている内容を一時退避
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        // コンソール出力に戻す
        stdout.release();

        // コンソール出力
        if (std.length() > 0) {
            LOG.info(DEFAULT_LINE_SEPARATOR + "--------");
            LOG.info(std);
        }
        if (std_err.length() > 0) {
            LOG.info(DEFAULT_LINE_SEPARATOR + "--------");
            LOG.info(std_err);
        }
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart1_1_1() throws Exception {
        LOG.info("start1_1_1");
        String[] args = {"-n", "1", "-i", "ID2"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = java.lang.NumberFormatException.class)
    public void testStart1_2_1() throws Exception {
        LOG.info("start1_2_1");
        String[] args = {"-n", "aa"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = java.lang.NumberFormatException.class)
    public void testStart1_2_2() throws Exception {
        LOG.info("start1_2_2");
        String[] args = {"-n", ""};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart1_2_3() throws Exception {
        LOG.info("start1_2_3");
        String[] args = {"-n"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart1_3_1() throws Exception {
        LOG.info("start1_3_1");
        String[] args = {"-i"};
        Main instance = new Main();
        instance.start(args);
    }

    private String getExpRes(final List<Channel> expList) {
        final StringBuilder sb = new StringBuilder();
        if (expList == null || expList.isEmpty()) {
            throw new IllegalArgumentException("想定されるチャンネル情報の一覧が取得できませんでした。");
        }
        int a = 1;
        int end = expList.size();

        for (Channel ch : expList) {
            Object[] parameters = {a++, end, ch.getChannelNo(), ch.getChannelId(), ch.getDisplayName()};
            sb.append(FORMAT.format(parameters)).append(DEFAULT_LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private String expRes_All() {
        final List<Channel> expList = dat.getAllChannelList();
        return this.getExpRes(expList);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_1_1() throws Exception {
        LOG.info("start2_1_1");
        String[] args = {};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_All(), this.stdout.getOutput());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_1_2() throws Exception {
        LOG.info("start2_1_2");
        String[] args = {"aa"};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_All(), this.stdout.getOutput());
    }

    private String expRes_TV1() {
        Object[] parameters1 = {1, 1, TestData.CH1_CHNO, TestData.CH1_ID, TestData.CH1_DN};
        String expResult = FORMAT.format(parameters1) + DEFAULT_LINE_SEPARATOR;
        return expResult;
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_2_1() throws Exception {
        LOG.info("start2_2_1");
        String[] args = {"-n", "0"};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_TV1(), this.stdout.getOutput());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_2_2() throws Exception {
        LOG.info("start2_2_2");
        String[] args = {"-i", "ID1"};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_TV1(), this.stdout.getOutput());
    }

    private String expRes_UseableAll() {
        List<Channel>expList=dat.getUseableChannelList();
        return this.getExpRes(expList);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_3_1() throws Exception {
        LOG.info("start2_3_1");
        String[] args = {"-e"};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_UseableAll(), this.stdout.getOutput());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_3_2() throws Exception {
        LOG.info("start2_3_2");
        String[] args = {"-e", "aa"};
        Main instance = new Main();
        instance.start(args);
        assertEquals(expRes_UseableAll(), this.stdout.getOutput());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_4_1() throws Exception {
        LOG.info("start2_4_1");
        String[] args = {"-e", "-n", "0"};
        Main instance = new Main();
        instance.start(args);
        String expResult = "";
        assertEquals(expResult, this.stdout.getOutput());
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart2_4_2() throws Exception {
        LOG.info("start2_4_2");
        String[] args = {"-e", "-i", "ID1"};
        Main instance = new Main();
        instance.start(args);
        String expResult = "";
        assertEquals(expResult, this.stdout.getOutput());
    }
}
