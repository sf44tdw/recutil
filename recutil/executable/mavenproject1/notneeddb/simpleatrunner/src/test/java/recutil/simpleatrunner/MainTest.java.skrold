/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.simpleatrunner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.commandexecutor.CommandExecutor;
import recutil.commandexecutor.DummyExecutor;

/**
 *
 * @author normal
 */
public class MainTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart() throws Exception {
        LOG.info("start");
        CommandExecutor executor = new DummyExecutor();
        String[] args = {"-f", "test/test.csv"};
        Main instance = new Main();
        instance.start(executor, args);
    }

}
