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
package recutil.reservecommon;

import static org.junit.Assert.*;
import static recutil.reservecommon.AtExecutor.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.commandexecutor.CommandResult;
import recutil.commandexecutor.DummyExecutor;

/**
 *
 * @author normal
 */
public class AtExecutorTest {

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();

    public AtExecutorTest() {
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

    private static final String SIGN = "DUMMY_SIG";
    private static final String COMMAND = "DUMMY_COMMAND -D";

    /**
     * Test of executeAt method, of class AtExecutor.
     */
    @Test
    public void testExecuteAt_1_1() throws Exception {
        LOG.info("executeAt_1_1");
        Date reserveDateTime = new Date(System.currentTimeMillis() + 180000L);
        String sign = SIGN;
        String targetCommand = COMMAND;

        DummyExecutor de = new DummyExecutor();
        AtExecutor instance = new AtExecutor(de);
        CommandResult result = instance.executeAt(reserveDateTime, sign, targetCommand);

        //final MessageFormat f = new MessageFormat(DummyExecutor.DUMMY_STDOUT_MESSAGE_FORMAT);

        LOG.info(result.toString());

        final String[] Params = de.getParam();

        LOG.info(ArrayUtils.toString(Params));

        final String[] Params__withoutTempFile = Arrays.copyOf(Params, 3);

        LOG.info(ArrayUtils.toString(Params__withoutTempFile));

        final String[] extParams_withoutTempFile = {AtExecutor.RESERVE_COMMAND_PARAMS.OPTION_TIME, new SimpleDateFormat(DATE_FORMAT).format(reserveDateTime), AtExecutor.RESERVE_COMMAND_PARAMS.OPTION_FILE};

        LOG.info(ArrayUtils.toString(extParams_withoutTempFile));

        assertEquals(de.getParam().length, 4);
        Assert.assertArrayEquals(Params__withoutTempFile,extParams_withoutTempFile);
        assertEquals(DummyExecutor.DUMMY_STDERR_MESSAGE, result.getStandardError());
        assertEquals(DummyExecutor.DUMMY_STDERR_MESSAGE, result.getStandardError());
        assertEquals(DummyExecutor.DUMMY_RETURN_CODE, result.getReturnCode());
    }

}
