/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.dbaccessor.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import recutil.dbaccessor.testdata.TestData;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class TempExcludechannelTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public TempExcludechannelTest() {
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
     * Test of getChannelId method, of class TempExcludechannel.
     */
    @Test
    public void testGetChannelId() {
        LOG.info("getChannelId");
        TempExcludechannel instance = new TempExcludechannel();
        String expResult = TestData.CH1_ID;
        instance.setChannelId(expResult);
        String result = instance.getChannelId();
        assertEquals(expResult, result);
    }


}
