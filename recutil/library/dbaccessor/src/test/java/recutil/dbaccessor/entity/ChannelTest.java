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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.testdata.TestData;
import static recutil.dbaccessor.testdata.TestData.getTestDbEm;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class ChannelTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    private final TestData dat;

    public ChannelTest() {
        dat = new TestData();
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void testfindAll() throws ParseException {
        LOG.info("findAll");

        List<String> expChannelId = dat.getAllChannslIdList();
        final int expSize = expChannelId.size();

        EntityManagerMaker instance = getTestDbEm();
        EntityManager result = instance.getEntityManager();
        List<Channel> table;
        TypedQuery<Channel> ql = result.createNamedQuery("Channel.findAll", Channel.class);
        table = ql.getResultList();
        assertEquals(table.size(), expSize);

        String xx = recutil.commmonutil.Util.dumpList(table);
        LOG.info(xx);

        int x = 0;
        for (String i : expChannelId) {
            for (Channel c : table) {
                if (c.getChannelId() == null ? i == null : c.getChannelId().equals(i)) {
                    x++;
                }
            }
        }
        assertEquals(table.size(), x);

        result.close();
        instance.close();
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void testfindByAllParams() throws ParseException {
        LOG.info("findByAllParams");

        List<String> expChannelId = new ArrayList<>();
        expChannelId.add(TestData.CH1_ID);

        final int expSize = 1;

        EntityManagerMaker instance = getTestDbEm();
        EntityManager result = instance.getEntityManager();
        List<Channel> table;
        TypedQuery<Channel> ql = result.createQuery("SELECT c FROM Channel c WHERE c.channelId = :channelId AND c.channelNo = :channelNo AND c.displayName = :displayName", Channel.class);
        ql.setParameter(recutil.dbaccessor.query.QueryString.Common.PARAMNAME_CHANNEL_ID, TestData.CH1_ID);
        ql.setParameter(recutil.dbaccessor.query.QueryString.Channel.PARAMNAME_CHANNEL_NO, TestData.CH1_CHNO);
        ql.setParameter(recutil.dbaccessor.query.QueryString.Channel.PARAMNAME_DISPLAY_NAME, TestData.CH1_DN);

        table = ql.getResultList();
        assertEquals(table.size(), expSize);

        String xx = recutil.commmonutil.Util.dumpList(table);
        LOG.info(xx);

        int x = 0;
        for (String i : expChannelId) {
            for (Channel c : table) {
                if (c.getChannelId() == null ? i == null : c.getChannelId().equals(i)) {
                    x++;
                }
            }
        }
        assertEquals(table.size(), x);

        result.close();
        instance.close();
    }

}
