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

import static org.junit.Assert.*;
import static recutil.dbaccessor.testdata.TestData.*;

import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.testdata.TestData;

/**
 *
 * @author normal
 */
public class QueryStringTest {

    private static final boolean EXECUTE_FLAG = true;

    private static final Logger LOG = LoggerConfigurator.getlnstance().getCallerLogger();
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
    public void TestgetExcludeChannelList() throws ParseException {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = true;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        try {
            dat.reloadDB();
            LOG.info("getExcludeChannelList");
            List<String> expRes = dat.getAllExcludeChannslIdList();
            int expSize = expRes.size();
            String xx1 = recutil.commmonutil.Util.dumpList(expRes);
            LOG.info(xx1);

            EntityManagerMaker instance = getTestDbEm();
            EntityManager result = instance.getEntityManager();
            List<String> table = new QueryString(result).getExcludeChannelList();

            assertEquals(table.size(), expSize);
            String xx2 = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx2);
            int x = 0;
            for (String s1 : expRes) {
                for (String s2 : table) {
                    if (s1.equals(s2)) {
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
}
