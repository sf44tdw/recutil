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
package recutil.dbaccessor.testdata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Excludechannel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.PERSISTENCE;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * テストデータ。 別にするとdbaccessorとの相互依存が発生するので同梱している。
 *
 * @author normal
 */
public final class TestData {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public static synchronized EntityManagerMaker getTestDbEm() {
               return new EntityManagerMaker(SelectedPersistenceName.getInstance());
    }
    
    public static final String CH1_ID = "ID1";
    public static final String CH2_ID = "ID2";
    public static final String CH3_ID = "ID3";
    public static final String CH4_1_ID = "ID4-1";
    public static final String CH4_2_ID = "ID4-2";
    public static final String CH5_ID = "ID5";
    public static final String CH6_ID = "ID6";

    public static final int CH1_CHNO = 0;
    public static final int CH2_CHNO = 1;
    public static final int CH3_CHNO = 2;
    public static final int CH4_CHNO = 3;
    public static final int CH5_CHNO = 5;
    public static final int CH6_CHNO = 6;

    public static final String CH1_DN = "TV1";
    public static final String CH2_DN = "TV2";
    public static final String CH3_DN = "TV3";
    public static final String CH4_1_DN = "TV4-1";
    public static final String CH4_2_DN = "TV4-2";
    public static final String CH5_DN = "TV5";
    public static final String CH6_DN = "TV6";

    private List<Channel> channelList;

    public static final int PG1_EVENTID = 100;
    public static final String PG1_TITLE = "DUMMY_TITLE1";
    public static final long PG1_START_TIME = 1470700800000L;
    public static final long PG1_STOP_TIME = 1470704400000L;

    public static final int PG2_EVENTID = 30000;
    public static final String PG2_TITLE = "DUMMY_TITLE2";
    public static final long PG2_START_TIME = 1470805200000L;
    public static final long PG2_STOP_TIME = 1470808800000L;

    public static final int PG3_EVENTID = 4000;
    public static final String PG3_TITLE = "EXCLUDED_TITLE2";

    public static final int PG4_EVENTID = 5000;
    public static final String PG4_TITLE = "DUMMY_TITLE3";
    public static final long PG4_START_TIME = 1001959800000L;
    public static final long PG4_STOP_TIME = 1001962200000L;

    public static final int PG9_EVENTID = 90000;
    public static final String PG9_TITLE = "DUMMY_TITLE9";
    public static final long PG9_START_TIME = 7997762100000L;
    public static final long PG9_STOP_TIME = 7997769300000L;

    private List<Programme> programmeList;

    private List<Excludechannel> excludechannellList;

    public TestData() {
        SelectedPersistenceName.selectPersistence(PERSISTENCE.TEST);
    }

    private Channel ch1 = null;

    public Channel getCh1() {
        if (this.ch1 == null) {
            ch1 = this.getCh(CH1_ID, CH1_CHNO, CH1_DN);
        }
        return ch1;
    }

    private Channel ch2 = null;

    public Channel getCh2() {
        if (this.ch2 == null) {
            ch2 = this.getCh(CH2_ID, CH2_CHNO, CH2_DN);
        }
        return ch2;
    }

    private Channel ch3 = null;

    public Channel getCh3() {
        if (this.ch3 == null) {
            ch3 = this.getCh(CH3_ID, CH3_CHNO, CH3_DN);
        }
        return ch3;
    }

    private Channel ch4_1 = null;

    public Channel getCh4_1() {
        if (this.ch4_1 == null) {
            ch4_1 = this.getCh(CH4_1_ID, CH4_CHNO, CH4_1_DN);
        }
        return ch4_1;
    }

    private Channel ch4_2 = null;

    public Channel getCh4_2() {
        if (this.ch4_2 == null) {
            ch4_2 = this.getCh(CH4_2_ID, CH4_CHNO, CH4_2_DN);
        }
        return ch4_2;
    }

    private Channel ch5 = null;

    public Channel getCh5() {
        if (this.ch5 == null) {
            ch5 = this.getCh(CH5_ID, CH5_CHNO, CH5_DN);
        }
        return ch5;
    }

    private Channel ch6 = null;

    public Channel getCh6() {
        if (this.ch6 == null) {
            ch6 = this.getCh(CH6_ID, CH6_CHNO, CH6_DN);
        }
        return ch6;
    }

    public Programme getPg1() {
        return this.getPg(this.getCh2(), PG1_EVENTID, PG1_TITLE, new Date(PG1_START_TIME), new Date(PG1_STOP_TIME));
    }

    public Programme getPg2() {
        return this.getPg(this.getCh3(), PG2_EVENTID, PG2_TITLE, new Date(PG2_START_TIME), new Date(PG2_STOP_TIME));
    }

    public Programme getPg3() {
        return this.getPg(this.getCh1(), PG3_EVENTID, PG3_TITLE, new Date(PG1_START_TIME), new Date(PG1_STOP_TIME));
    }

    public Programme getPg4() {
        return this.getPg(this.getCh2(), PG4_EVENTID, PG4_TITLE, new Date(PG4_START_TIME), new Date(PG4_STOP_TIME));
    }

    public Programme getPg9() {
        return this.getPg(this.getCh6(), PG9_EVENTID, PG9_TITLE, new Date(PG9_START_TIME), new Date(PG9_STOP_TIME));
    }

    public Excludechannel getEc1() {
        return this.getEc(CH1_ID);
    }

    public Excludechannel getEc2() {
        return this.getEc(CH4_2_ID);
    }

    /**
     * 定数から、チャンネル、番組、除外チャンネルのエンティティとそのリストを新規作成する。
     */
    public synchronized void make() {

        this.sealList();

        channelList = new ArrayList<>();
        channelList.add(this.getCh1());
        channelList.add(this.getCh2());
        channelList.add(this.getCh3());
        channelList.add(this.getCh4_1());
        channelList.add(this.getCh4_2());
        channelList.add(this.getCh5());
        channelList.add(this.getCh6());

        programmeList = new ArrayList<>();
        programmeList.add(this.getPg1());
        programmeList.add(this.getPg2());
        programmeList.add(this.getPg3());
        programmeList.add(this.getPg4());
        programmeList.add(this.getPg9());

        excludechannellList = new ArrayList<>();
        excludechannellList.add(this.getEc1());
        excludechannellList.add(this.getEc2());

    }

    private synchronized void sealList() {
        this.ch1 = null;
        this.ch2 = null;
        this.ch3 = null;
        this.ch4_1 = null;
        this.ch4_2 = null;
        this.ch5 = null;
        this.ch6 = null;
    }

    private Channel getCh(String ChannelId, int ChannelNo, String DisplayName) {
        final Channel ch = new Channel();
        ch.setChannelId(ChannelId);
        ch.setChannelNo(ChannelNo);
        ch.setDisplayName(DisplayName);
        return ch;
    }

    public Programme getPg(Channel channelId, int eventId, String title, Date startDatetime, Date stopDatetime) {
        final Programme p = new Programme();
        p.setChannelId(channelId);
        p.setEventId(eventId);
        p.setTitle(title);
        p.setStartDatetime(startDatetime);
        p.setStopDatetime(stopDatetime);
        return p;
    }

    private Excludechannel getEc(String ChannelId) {
        final Excludechannel ec = new Excludechannel();
        ec.setChannelId(ChannelId);
        return ec;
    }

    public synchronized List<String> getAllChannslIdList() {
        this.make();
        List<String> res = new ArrayList<>();
        for (Channel ch : this.getAllChannelList()) {
            res.add(ch.getChannelId());
        }
        return res;
    }

    public synchronized List<String> getAllUseableChannslIdList() {
        this.make();
        List<String> res = new ArrayList<>();
        for (Channel ch : this.getUseableChannelList()) {
            res.add(ch.getChannelId());
        }
        return res;
    }

    public synchronized List<String> getAllExcludeChannslIdList() {
        this.make();
        List<String> res = new ArrayList<>();
        for (Excludechannel ec : this.getExcludechannellList()) {
            res.add(ec.getChannelId());
        }
        return res;
    }

    public synchronized List<Integer> getAllUseableEventIdList() {
        this.make();
        List<Integer> res = new ArrayList<>();
        for (Programme pg : this.getUseableProgrammeList()) {
            res.add(pg.getEventId());
        }
        return res;
    }

    /**
     * 新規作成したチャンネルの除外リストに載っていないエンティティリストを取得する。
     *
     * @return
     */
    public synchronized List<Channel> getUseableChannelList() {
        final List<Channel> res = new ArrayList<>();
        final Set<String> ecs = new HashSet<>(this.getAllExcludeChannslIdList());
        for (Channel ch : this.getAllChannelList()) {
            if (!ecs.contains(ch.getChannelId())) {
                res.add(ch);
            }
        }
        return res;
    }

    /**
     * 新規作成したチャンネルのエンティティリストを取得する。
     *
     * @return
     */
    public synchronized List<Channel> getAllChannelList() {
        this.make();
        this.sealList();
        return channelList;
    }

    /**
     * 新規作成した番組の除外リストに載っていないエンティティリストを取得する。
     *
     * @return
     */
    public synchronized List<Programme> getUseableProgrammeList() {
        final List<Programme> res = new ArrayList<>();
        final Set<String> ecs = new HashSet<>(this.getAllExcludeChannslIdList());
        for (Programme pg : this.getAllProgrammeList()) {
            if (!ecs.contains(pg.getChannelId().getChannelId())) {
                res.add(pg);
            }
        }
        return res;
    }

    /**
     * 新規作成した番組のエンティティリストを取得する。
     *
     * @return
     */
    public synchronized List<Programme> getAllProgrammeList() {
        this.make();
        this.sealList();
        return programmeList;
    }

    /**
     * 新規作成した除外チャンネルのエンティティリストを取得する。
     *
     * @return
     */
    public synchronized List<Excludechannel> getExcludechannellList() {
        this.make();
        this.sealList();
        return excludechannellList;
    }

    private synchronized void deleteAll(EntityManager em) {
        LOG.info("DB登録内容を全て削除します。");

        LOG.info("現在の番組登録を全て削除します。");
        final TypedQuery<Programme> pg_del;
        pg_del = em.createNamedQuery("Programme.deleteAll", Programme.class);
        pg_del.executeUpdate();
        LOG.info("現在の番組登録を全て削除しました。");

        LOG.info("現在の除外登録を全て削除します。");
        final TypedQuery<Excludechannel> ex_del;
        ex_del = em.createNamedQuery("Excludechannel.deleteAll", Excludechannel.class);
        ex_del.executeUpdate();
        LOG.info("現在の除外登録を全て削除しました。");

        LOG.info("現在のチャンネル登録を全て削除します。");
        final TypedQuery<Channel> ch_del;
        ch_del = em.createNamedQuery("Channel.deleteAll", Channel.class);
        ch_del.executeUpdate();
        LOG.info("現在のチャンネル登録を全て削除しました。");

        LOG.info("DB登録内容を全て削除しました。");

    }

    public synchronized void insertChs(EntityManager em, List<Channel> channelList) {
        LOG.info("チャンネル登録開始。");
        for (Channel ch : channelList) {
            em.persist(ch);
            LOG.info("チャンネル登録 = " + ToStringBuilder.reflectionToString(ch));
        }
        LOG.info("チャンネル登録開完了。");
    }

    public synchronized void insertPgs(EntityManager em, List<Programme> programmeList) {
        LOG.info("番組登録開始。");
        for (Programme pg : programmeList) {
            em.persist(pg);
            LOG.info("番組登録 = " + ToStringBuilder.reflectionToString(pg));
        }
        LOG.info("番組登録完了。");
    }

    private synchronized void updateDB01(EntityManager em) {
        LOG.info("番組登録を行います。");
        this.insertChs(em, channelList);
        this.insertPgs(em, programmeList);
        LOG.info("番組登録を行いました。");
    }

    private synchronized void updateDB02(EntityManager em) {
        LOG.info("除外チャンネル登録開始。");
        for (Excludechannel ech : excludechannellList) {
            em.persist(ech);
            LOG.info("チャンネルIDを除外登録 = " + ToStringBuilder.reflectionToString(ech));
        }
        LOG.info("除外チャンネル登録完了。");
    }

    public synchronized void reloadDB() {
        this.make();
        EntityManager em = getTestDbEm().getEntityManager();
        LOG.info("DBの内容を初期化します。");
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        LOG.info("番組登録トランザクション開始。");
        this.deleteAll(em);
        this.updateDB01(em);
        trans.commit();
        LOG.info("番組登録トランザクションコミット。");
        LOG.info("除外チャンネル登録トランザクション開始。");
        trans.begin();
        this.updateDB02(em);
        trans.commit();
        LOG.info("除外チャンネル登録トランザクションコミット。");
        LOG.info("DBの内容を初期化しました。");
        em.close();
    }

    public static List<Channel> dumpChannelTable() {
        try (EntityManagerMaker emm = getTestDbEm()) {
            final EntityManager em = emm.getEntityManager();
            final TypedQuery<Channel> ql = em.createNamedQuery("Channel.findAll", Channel.class);
            final List<Channel> res = ql.getResultList();
            for (Channel ch : res) {
                LOG.info(ToStringBuilder.reflectionToString(ch));
            }
            em.close();
            return res;
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return new ArrayList<>();
    }

    public static List<Programme> dumpProgrammeTable() {
        try (EntityManagerMaker emm = getTestDbEm()) {
            final EntityManager em = emm.getEntityManager();
            final TypedQuery<Programme> ql = em.createNamedQuery("Programme.findAll", Programme.class);
            final List<Programme> res = ql.getResultList();
            for (Programme pg : res) {
                LOG.info(ToStringBuilder.reflectionToString(pg));
            }
            em.close();
            return res;
        } catch (Exception ex) {
            LOG.error("", ex);
        }
        return new ArrayList<>();
    }

    public static List<Excludechannel> dumpExcludechannelTable() {
        try (EntityManagerMaker emm = getTestDbEm()) {
            final EntityManager em = emm.getEntityManager();
            final TypedQuery<Excludechannel> ql = em.createNamedQuery("Excludechannel.findAll", Excludechannel.class);
            final List<Excludechannel> res = ql.getResultList();
            for (Excludechannel ech : res) {
                LOG.info(ToStringBuilder.reflectionToString(ech));
            }
            em.close();
            return res;
        } catch (Exception ex) {
            LOG.error("{}", ex);
        }
        return new ArrayList<>();
    }

}
