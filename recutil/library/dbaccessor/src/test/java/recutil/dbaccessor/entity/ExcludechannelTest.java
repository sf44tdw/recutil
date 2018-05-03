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
package recutil.dbaccessor.entity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.dbaccessor.testdata.TestData;
import static recutil.dbaccessor.testdata.TestData.getTestDbEm;

/**
 *
 * @author normal
 */
public class ExcludechannelTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public ExcludechannelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private final TestData dat = new TestData();

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getChannelId method, of class Excludechannel.
     */
    @Test
    public void testGetChannelId() {
        LOG.info("getChannelId");
        Excludechannel instance = new Excludechannel();
        String expResult = TestData.CH1_ID;
        instance.setChannelId(expResult);
        String result = instance.getChannelId();
        assertEquals(expResult, result);
    }

    //INSERTできないことが確認できればよい。
    @Test
    public void testDummyId() {
        LOG.info("dummyId");
        dat.reloadDB();

        final String DUMMY_ID = "DUMMY_1234@@@";

        Excludechannel instance = new Excludechannel();
        instance.setChannelId(DUMMY_ID);

        try (EntityManagerMaker mk = getTestDbEm()) {
            EntityManager man = mk.getEntityManager();
            man.persist(instance);
        }

        try (EntityManagerMaker mk = getTestDbEm()) {
            EntityManager man = mk.getEntityManager();
            List<Excludechannel> table;
            TypedQuery<Excludechannel> ql = man.createQuery("SELECT e FROM Excludechannel e WHERE e.channelId = :channelId", Excludechannel.class);
            ql.setParameter(recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID, DUMMY_ID);
            table = ql.getResultList();
            assertEquals(table.size(), 0);
        }
    }

}
