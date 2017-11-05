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
package recutil.dbaccessor.query;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.manager.EntityManagerMaker;
import static recutil.dbaccessor.query.QueryString.Channel.ALL_USEABLE_CHANNEL;
import static recutil.dbaccessor.query.QueryString.Channel.USEABLE_CHANNEL_BY_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Channel.USEABLE_CHANNEL_BY_CHANNEL_NO;
import static recutil.dbaccessor.query.QueryString.Programme.ALL_USEABLE_PROGRAMME;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_EVENT_ID;
import static recutil.dbaccessor.query.QueryString.Programme.USEABLE_PROGRAMME_BY_START_DATETIME;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.dbaccessor.testdata.TestData;
import static recutil.dbaccessor.testdata.TestData.getTestDbEm;

/**
 *
 * @author normal
 */
public class QueryStringTest {

    private static final boolean EXECUTE_FLAG = true;

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    final TestData dat;

    public QueryStringTest() {

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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableChannelById() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableChannelById");

            String qlString = USEABLE_CHANNEL_BY_CHANNEL_ID;
            LOG.info(qlString);

            List<String> expChannelId = new ArrayList<>();
            expChannelId.add(TestData.CH4_1_ID);

            int expSize = expChannelId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Channel> table;
            TypedQuery<Channel> ql = result.createQuery(qlString, Channel.class);
            ql.setParameter("channelId", TestData.CH4_1_ID);
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
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableChannelByNo() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableChannelByNo");

            String qlString = USEABLE_CHANNEL_BY_CHANNEL_NO;
            LOG.info(qlString);

            List<Integer> expChannelNo = new ArrayList<>();
            expChannelNo.add(TestData.CH4_CHNO);

            int expSize = expChannelNo.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Channel> table;
            TypedQuery<Channel> ql = result.createQuery(qlString, Channel.class);
            ql.setParameter("channelNo", TestData.CH4_CHNO);
            table = ql.getResultList();
            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx);

            int x = 0;
            for (Integer i : expChannelNo) {
                for (Channel c : table) {
                    if (c.getChannelNo() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void allUseableChannel() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("AllUseableChannel");

            String qlString = ALL_USEABLE_CHANNEL;
            LOG.info(qlString);

            List<String> expChannelId = dat.getAllUseableChannslIdList();

            int expSize = expChannelId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Channel> table;
            TypedQuery<Channel> ql = result.createQuery(qlString, Channel.class);
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
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableProgrammeByEventId() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableProgrammeByEventId");

            String qlString = USEABLE_PROGRAMME_BY_EVENT_ID;
            LOG.info(qlString);

            List<Integer> expEventId = new ArrayList<>();
            expEventId.add(TestData.PG1_EVENTID);

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            ql.setParameter("eventId", TestData.PG1_EVENTID);
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableProgrammeByChannelId() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableProgrammeByChannelId");

            String qlString = USEABLE_PROGRAMME_BY_CHANNEL_ID;
            LOG.info(qlString);

            List<Integer> expEventId = new ArrayList<>();
            expEventId.add(TestData.PG2_EVENTID);

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            ql.setParameter("channelId", TestData.CH3_ID);
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableProgrammeByChannelIdAndEventId() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableProgrammeByChannelIdAndEventId");

            String qlString = USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_EVENT_ID;
            LOG.info(qlString);

            List<Integer> expEventId = new ArrayList<>();
            expEventId.add(TestData.PG2_EVENTID);

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            ql.setParameter("channelId", TestData.CH3_ID);
            ql.setParameter("eventId", TestData.PG2_EVENTID);
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableProgrammeByStartDateTime() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableProgrammeByStartDateTime");

            String qlString = USEABLE_PROGRAMME_BY_START_DATETIME;
            LOG.info(qlString);

            List<Integer> expEventId = new ArrayList<>();
            expEventId.add(TestData.PG2_EVENTID);

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            ql.setParameter("startDatetime", new Date(TestData.PG2_START_TIME));
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void useableProgrammeByChannelIdAndStartDateTime() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("UseableProgrammeByChannelIdAndStartDateTime");

            String qlString = USEABLE_PROGRAMME_BY_CHANNEL_ID_AND_START_DATETIME;
            LOG.info(qlString);

            List<Integer> expEventId = new ArrayList<>();
            expEventId.add(TestData.PG2_EVENTID);

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            ql.setParameter("channelId", TestData.CH3_ID);
            ql.setParameter("startDatetime", new Date(TestData.PG2_START_TIME));
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    /**
     * Test of getEntityManager method, of class EntityManagerMaker.
     */
    @Test
    public void allUseableProgramme() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("AllUseableProgramme");

            String qlString = ALL_USEABLE_PROGRAMME;
            LOG.info(qlString);

            List<Integer> expEventId = dat.getAllUseableEventIdList();

            int expSize = expEventId.size();

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<Programme> table;
            TypedQuery<Programme> ql = result.createQuery(qlString, Programme.class);
            table = ql.getResultList();

            assertEquals(table.size(), expSize);

            String xx = recutil.commmonutil.Util.dumpList(table);
            int x = 0;
            for (Integer i : expEventId) {
                for (Programme p : table) {
                    if (p.getEventId() == i) {
                        x++;
                    }
                }
            }
            assertEquals(table.size(), x);

            LOG.info(xx);

            result.close();
            instance.close();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

}
