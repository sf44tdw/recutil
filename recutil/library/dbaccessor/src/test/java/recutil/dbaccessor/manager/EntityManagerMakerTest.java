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
package recutil.dbaccessor.manager;

import java.text.ParseException;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import recutil.dbaccessor.entity.Channel;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.dbaccessor.testdata.TestData;

/**
 *
 * @author normal
 */
public class EntityManagerMakerTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public EntityManagerMakerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        final TestData dat = new TestData();
        dat.reloadDB();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @org.junit.Test
    public void testGetEntityManager() throws ParseException {
        LOG.info("getEntityManager");

        EntityManagerMaker instance = new EntityManagerMaker();
        EntityManager result = instance.getEntityManager();
        List<Channel> table = result
                .createNamedQuery("Channel.findByChannelNo", Channel.class)
                .setParameter("channelNo", TestData.CH2_CHNO)
                .getResultList();

        if (table.size() > 0) {
            int expSize = 1;
            String ChannelId = TestData.CH2_ID;
            int ChannelNo = TestData.CH2_CHNO;
            String DisplayName = TestData.CH2_DN;

            assertEquals(table.size(), expSize);
            Channel res = table.iterator().next();
            assertEquals(res.getChannelId(), ChannelId);
            assertEquals(res.getChannelNo(), ChannelNo);
            assertEquals(res.getDisplayName(), DisplayName);
            result.close();
            instance.close();
        }
    }

}
