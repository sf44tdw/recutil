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
package recutil.excludechannel;

import static org.junit.Assert.*;
import static recutil.commmonutil.Util.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.consolesnatcher.ConsoleSnatcher;
import recutil.dbaccessor.entity.TempExcludechannel;
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
    @Test
    public void testStart_01() throws Exception {
        LOG.info("start_01");
        String[] args = {"-d"};
        LOG.info("before");
        final List<TempExcludechannel> res0 = TestData.dumpTempExcludechannelTable();
        assertEquals(res0.size(), dat.getTempExcludechannellList().size());
        Main instance = new Main();
        instance.start(args);
        LOG.info("after");
        final List<TempExcludechannel> res = TestData.dumpTempExcludechannelTable();
        assertTrue(res.isEmpty());

    }

    @Test
    public void testStart_02() throws Exception {
        String[] args = {"-u", TestData.CH4_1_ID, TestData.CH3_ID, TestData.CH4_2_ID};

        LOG.info("start_02");

        String[] ids = ArrayUtils.subarray(args, 1, args.length);

        Set<String> vals = new HashSet<>();
        vals.addAll(Arrays.asList(ids));

        TestData.dumpChannelTable();
        LOG.info("before");

        List<TempExcludechannel> res0 = TestData.dumpTempExcludechannelTable();
        assertEquals(res0.size(), dat.getTempExcludechannellList().size());
        res0 = null;

        Main instance = new Main();
        instance.start(args);
        LOG.info("after");
        final List<TempExcludechannel> res = TestData.dumpTempExcludechannelTable();

        final int expSize = vals.size();

        assertEquals(res.size(), expSize);

        for (TempExcludechannel exc : res) {
            assertTrue(vals.contains(exc.getChannelId()));
        }

    }
}
