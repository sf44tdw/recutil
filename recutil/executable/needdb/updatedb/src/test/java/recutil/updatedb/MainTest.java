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
package recutil.updatedb;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.loggerconfigurator.LoggerConfigurator;
import recutil.updatedb.common.Const;
import recutil.updatedb.dataextractor.channel.AllChannelDataExtractor;
import recutil.updatedb.dataextractor.channel.ChannelData;
import recutil.updatedb.dataextractor.programme.AllProgrammeDataExtractor;
import recutil.updatedb.dataextractor.programme.ProgrammeData;
import recutil.updatedb.listmaker.EpgListMaker;

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
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart01_01() throws Exception {
        LOG.info("start01_01");
        String[] args = {"-d"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = Error.class)
    public void testStart01_02() throws Exception {
        LOG.info("start01_02");
        String[] args = {"-d", "6ufgyhkouiou"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart02_01() throws Exception {
        LOG.info("start02_01");
        String[] args = {"-c"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.MissingOptionException.class)
    public void testStart02_02() throws Exception {
        LOG.info("start02_02");
        String[] args = {"-c", "dweswliiksd"};
        Main instance = new Main();
        instance.start(args);
    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = org.apache.commons.cli.ParseException.class)
    public void testStart03_01() throws Exception {
        LOG.info("start03_01");
        String[] args = {"-d", "-c"};
        Main instance = new Main();
        instance.start(args);
    }

    private class dataChecker {

        final List<ChannelData> chs;
        final List<ProgrammeData> ps;

        public dataChecker(File targetDir, Charset charset) {
            EpgListMaker elm = new EpgListMaker(targetDir, charset);
            chs = new AllChannelDataExtractor(elm.getEpgList()).getAllEPGRecords();
            ps = new AllProgrammeDataExtractor(elm.getEpgList()).getAllEPGRecords();
        }

        /**
         * もとのファイルのチャンネル情報がテーブルに全部入っていれば問題なし。
         */
        private boolean checkChannelTable(EntityManager em) {
            boolean ret = true;
            final TypedQuery<recutil.dbaccessor.entity.Channel> ql = em.createNamedQuery("Channel.findByAllParams", recutil.dbaccessor.entity.Channel.class);
            for (ChannelData ch : chs) {
                LOG.info("ファイルから {}", ch.toString());
                ql.setParameter("channelId", ch.getId());
                ql.setParameter("channelNo", ch.getPhysicalChannelNumber());
                ql.setParameter("displayName", ch.getBroadcastingStationName());
                List<recutil.dbaccessor.entity.Channel> res = ql.getResultList();
                if (res.size() > 0) {
                    ret = ret && true;
                    LOG.info("登録あり。");
                    recutil.commmonutil.Util.dumpList(res);
                } else {
                    ret = false;
                    LOG.info("登録なし。");
                }
            }
            return ret;
        }

        /**
         * もとのファイルの番組情報がテーブルに全部入っていれば問題なし。
         */
        private boolean chechProgrammeTable(EntityManager em) {
            boolean ret = true;
            final TypedQuery<recutil.dbaccessor.entity.Programme> ql = em.createNamedQuery("Programme.findByAllParams", recutil.dbaccessor.entity.Programme.class);
            for (ProgrammeData pg : ps) {
                LOG.info("ファイルから {}", pg.toString());
                ql.setParameter("eventId", pg.getEventId());
                ql.setParameter("title", pg.getTitle());
                ql.setParameter("startDatetime", pg.getStartDatetime());
                ql.setParameter("stopDatetime", pg.getStopDatetime());
                ql.setParameter("channelId", pg.getId());
                List<recutil.dbaccessor.entity.Programme> res = ql.getResultList();
                if (res.size() > 0) {
                    ret = ret && true;
                    LOG.info("登録あり。");
                    recutil.commmonutil.Util.dumpList(res);
                } else {
                    ret = false;
                    LOG.info("登録なし。");
                }
            }
            return ret;
        }

        public boolean check() {
            boolean ret = true;
            try (EntityManagerMaker emm = new EntityManagerMaker()) {
                final EntityManager em = emm.getEntityManager();

                ret = ret && this.checkChannelTable(em);

                ret = ret && this.chechProgrammeTable(em);

                em.close();
            }
            return ret;
        }

    }

    /**
     * Test of start method, of class Main.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStart04_01() throws Exception {
        LOG.info("start04_01");
        String[] args = {"-d", Const.getXMLTESTDATADIR().getAbsolutePath(), "-c", "ssdij2sp"};
        Main instance = new Main();
        instance.start(args);

    }

    /**
     * Test of start method, of class Main.
     */
    @Test
    public void testStart04_02() throws Throwable{
        try {
            LOG.info("start04_02");
            String[] args = {"-d", Const.getXMLTESTDATADIR().getAbsolutePath(), "-c", "UTF-8"};
            Main instance = new Main();
            instance.start(args);
            assertTrue(new dataChecker(Const.getXMLTESTDATADIR(), Charset.forName("UTF-8")).check());
        } catch (Throwable ex) {
            LOG.error("エラーが発生しました。", ex);
            throw ex;
        }
    }

}
