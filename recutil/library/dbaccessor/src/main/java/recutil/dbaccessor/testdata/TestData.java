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

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
import recutil.dbaccessor.entity.TempExcludechannel;
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
    private List<TempExcludechannel> tempExcludechannellList;

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

    public TempExcludechannel getTempEc1() {
        return this.getEc(CH1_ID);
    }

    public TempExcludechannel getTempEc2() {
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

        tempExcludechannellList = new ArrayList<>();
        tempExcludechannellList.add(this.getTempEc1());
        tempExcludechannellList.add(this.getTempEc2());

        excludechannellList = new ArrayList<>();
        for (TempExcludechannel tec : this.tempExcludechannellList) {
            excludechannellList.add(new Excludechannel(tec.getChannelId()));
        }
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

    private TempExcludechannel getEc(String ChannelId) {
        final TempExcludechannel ec = new TempExcludechannel();
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

    public synchronized List<Long> getAllUseableEventIdList() {
        this.make();
        List<Long> res = new ArrayList<>();
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
     * 新規作成した除外チャンネルのエンティティリストを取得する。(転記元)
     *
     * @return
     */
    public synchronized List<TempExcludechannel> getTempExcludechannellList() {
        this.make();
        this.sealList();
        return this.tempExcludechannellList;
    }

    /**
     * 新規作成した除外チャンネルのエンティティリストを取得する。(転記先)
     *
     * @return
     */
    public synchronized List<Excludechannel> getExcludechannellList() {
        this.make();
        this.sealList();
        return this.excludechannellList;
    }

    public static final String PG_AFTER_60 = "AFTER_60_SECOND";
    public static final String PG_AFTER_120 = "AFTER_120_SECOND";
    public static final String PG_AFTER_180 = "AFTER_180_SECOND";
    public static final String PG_AFTER_240 = "AFTER_240_SECOND";
    public static final String PG_AFTER_300 = "AFTER_300_SECOND";
    public static final String PG_AFTER_360 = "AFTER_360_SECOND";
    public static final long ONE_MINUTE_IN_MILLIS = (60L * 1000L);
    public static final String PG_AFTER_600 = "AFTER_600_SECOND";
    public static final String PG_AFTER_1200 = "AFTER_1200_SECOND";
    public static final String PG_AFTER_1800 = "AFTER_1800_SECOND";
    public static final String PG_AFTER_2400 = "AFTER_2400_SECOND";
    public static final String PG_AFTER_3000 = "AFTER_3000_SECOND";
    public static final String PG_AFTER_3600 = "AFTER_3600_SECOND";
    public static final long TEN_MINUTE_IN_MILLIS = ONE_MINUTE_IN_MILLIS * 10L;
    public static final String PG_AFTER_7200 = "AFTER_7200_SECOND";
    public static final String PG_AFTER_10800 = "AFTER_10800_SECOND";
    public static final String PG_AFTER_14400 = "AFTER_14400_SECOND";
    public static final String PG_AFTER_18000 = "AFTER_18000_SECOND";
    public static final String PG_AFTER_21600 = "AFTER_21600_SECOND";
    public static final String PG_AFTER_25200 = "AFTER_25200_SECOND";
    public static final long ONE_HOUR_IN_MILLIS = ONE_MINUTE_IN_MILLIS * 60L;
    private Programme p60;
    private Programme p120;
    private Programme p180;
    private Programme p240;
    private Programme p300;
    private Programme p360;
    private Programme p600;
    private Programme p1200;
    private Programme p1800;
    private Programme p2400;
    private Programme p3000;
    private Programme p3600;
    private Programme p7200;
    private Programme p10800;
    private Programme p14400;
    private Programme p18000;
    private Programme p21600;
    private Programme p25200;

    private ZonedDateTime d = ZonedDateTime.now();
    private ZonedDateTime d2 = d.truncatedTo(ChronoUnit.MINUTES);

    public Date getNowTime() {
        return new Date(nowTime.getTime());
    }
    private Date nowTime = Date.from(d2.toInstant());

    public List<Programme> getTestProgrammes() {
        make();
        List<Programme> _t = new ArrayList<>();

        ZonedDateTime d = ZonedDateTime.now();
        ZonedDateTime d2 = d.truncatedTo(ChronoUnit.MINUTES);
        nowTime = Date.from(d2.toInstant());

        final long pp = (30L * 1000L);

        int id = 1000000;

//今から1分後に始まる番組。
        final Date after60sec = new Date((nowTime.getTime() + ONE_MINUTE_IN_MILLIS));
        p60 = getPg(getCh5(), id++, PG_AFTER_60, after60sec, new Date(after60sec.getTime() + pp));
        _t.add(p60);

//同、2分後。
        final Date after120sec = new Date((after60sec.getTime() + ONE_MINUTE_IN_MILLIS));
        p120 = getPg(getCh5(), id++, PG_AFTER_120, after120sec, new Date(after120sec.getTime() + pp));
        _t.add(p120);

        //同、3分後。
        final Date after180sec = new Date((after120sec.getTime() + ONE_MINUTE_IN_MILLIS));
        p180 = getPg(getCh5(), id++, PG_AFTER_180, after180sec, new Date(after180sec.getTime() + pp));
        _t.add(p180);

        //同、4分後。
        final Date after240sec = new Date((after180sec.getTime() + ONE_MINUTE_IN_MILLIS));
        p240 = getPg(getCh5(), id++, PG_AFTER_240, after240sec, new Date(after240sec.getTime() + pp));
        _t.add(p240);

        //同、5分後。
        final Date after300sec = new Date((after240sec.getTime() + ONE_MINUTE_IN_MILLIS));
        p300 = getPg(getCh5(), id++, PG_AFTER_300, after300sec, new Date(after300sec.getTime() + pp));
        _t.add(p300);

        //同、6分後。
        final Date after360sec = new Date((after300sec.getTime() + ONE_MINUTE_IN_MILLIS));
        p360 = getPg(getCh5(), id++, PG_AFTER_360, after360sec, new Date(after360sec.getTime() + pp));
        _t.add(p360);

        //同、10分後。
        final Date after10min = new Date((after360sec.getTime() + TEN_MINUTE_IN_MILLIS));
        p600 = getPg(getCh5(), id++, PG_AFTER_600, after10min, new Date(after10min.getTime() + pp));
        _t.add(p600);

        //同、20分後。
        final Date after20min = new Date((after10min.getTime() + TEN_MINUTE_IN_MILLIS));
        p1200 = getPg(getCh5(), id++, PG_AFTER_1200, after20min, new Date(after20min.getTime() + pp));
        _t.add(p1200);

        //同、30分後。
        final Date after30min = new Date((after20min.getTime() + TEN_MINUTE_IN_MILLIS));
        p1800 = getPg(getCh5(), id++, PG_AFTER_1800, after30min, new Date(after30min.getTime() + pp));
        _t.add(p1800);

        //同、40分後。
        final Date after40min = new Date((after30min.getTime() + TEN_MINUTE_IN_MILLIS));
        p2400 = getPg(getCh5(), id++, PG_AFTER_2400, after40min, new Date(after40min.getTime() + pp));
        _t.add(p2400);

        //同、50分後。
        final Date after50min = new Date((after40min.getTime() + TEN_MINUTE_IN_MILLIS));
        p3000 = getPg(getCh5(), id++, PG_AFTER_3000, after50min, new Date(after50min.getTime() + pp));
        _t.add(p3000);

        //同、60分後。
        final Date after60min = new Date((after50min.getTime() + TEN_MINUTE_IN_MILLIS));
        p3600 = getPg(getCh5(), id++, PG_AFTER_3600, after60min, new Date(after60min.getTime() + pp));
        _t.add(p3600);

        //2時間
        final Date after2hour = new Date((after60min.getTime() + ONE_HOUR_IN_MILLIS));
        p7200 = getPg(getCh5(), id++, PG_AFTER_7200, after2hour, new Date(after2hour.getTime() + pp));
        _t.add(p7200);

//3時間
        final Date after3hour = new Date((after2hour.getTime() + ONE_HOUR_IN_MILLIS));
        p10800 = getPg(getCh5(), id++, PG_AFTER_10800, after3hour, new Date(after3hour.getTime() + pp));
        _t.add(p10800);

//4時間
        final Date after4hour = new Date((after3hour.getTime() + ONE_HOUR_IN_MILLIS));
        p14400 = getPg(getCh5(), id++, PG_AFTER_14400, after4hour, new Date(after4hour.getTime() + pp));
        _t.add(p14400);

//5時間
        final Date after5hour = new Date((after4hour.getTime() + ONE_HOUR_IN_MILLIS));
        p18000 = getPg(getCh5(), id++, PG_AFTER_18000, after5hour, new Date(after5hour.getTime() + pp));
        _t.add(p18000);

//6時間
        final Date after6hour = new Date((after5hour.getTime() + ONE_HOUR_IN_MILLIS));
        p21600 = getPg(getCh5(), id++, PG_AFTER_21600, after6hour, new Date(after6hour.getTime() + pp));
        _t.add(p21600);

//7時間
        final Date after7hour = new Date((after6hour.getTime() + ONE_HOUR_IN_MILLIS));
        p25200 = getPg(getCh5(), id++, PG_AFTER_25200, after7hour, new Date(after7hour.getTime() + pp));
        _t.add(p25200);
        return _t;
    }

    public Programme getP120() {
        return p120;
    }

    public Programme getP180() {
        return p180;
    }

    public Programme getP240() {
        return p240;
    }

    public Programme getP300() {
        return p300;
    }

    public Programme getP360() {
        return p360;
    }

    public Programme getP600() {
        return p600;
    }

    public Programme getP1200() {
        return p1200;
    }

    public Programme getP1800() {
        return p1800;
    }

    public Programme getP2400() {
        return p2400;
    }

    public Programme getP3000() {
        return p3000;
    }

    public Programme getP3600() {
        return p3600;
    }

    public Programme getP7200() {
        return p7200;
    }

    public Programme getP10800() {
        return p10800;
    }

    public Programme getP14400() {
        return p14400;
    }

    public Programme getP18000() {
        return p18000;
    }

    public Programme getP21600() {
        return p21600;
    }

    public Programme getP25200() {
        return p25200;
    }

    /**
     * 現在時刻から60-25200秒後までに始まる番組情報を作成し、データベースに追加する。
     *
     * @return
     */
    public void insertRecentStartProgrammes() {
        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();
            EntityTransaction trans = man.getTransaction();
            trans.begin();
            LOG.info("番組登録トランザクション開始。");
            insertPgs(man, this.getTestProgrammes());
            trans.commit();
            LOG.info("除外チャンネル登録トランザクションコミット。");
            man.close();
        }
    }

    public synchronized void deleteAll(EntityManager em) {
        LOG.info("DB登録内容を全て削除します。");

        LOG.info("現在の番組登録を全て削除します。");
        final TypedQuery<Programme> pg_del;
        pg_del = em.createNamedQuery("Programme.deleteAll", Programme.class);
        pg_del.executeUpdate();
        LOG.info("現在の番組登録を全て削除しました。");

        LOG.info("現在の除外登録(転記先)を全て削除します。");
        final TypedQuery<Excludechannel> ex_del;
        ex_del = em.createNamedQuery("Excludechannel.deleteAll", Excludechannel.class);
        ex_del.executeUpdate();
        LOG.info("現在の除外登録(転記先)を全て削除しました。");

        LOG.info("現在のチャンネル登録を全て削除します。");
        final TypedQuery<Channel> ch_del;
        ch_del = em.createNamedQuery("Channel.deleteAll", Channel.class);
        ch_del.executeUpdate();
        LOG.info("現在のチャンネル登録を全て削除しました。");

        LOG.info("現在の除外登録(転記元)を全て削除します。");
        final TypedQuery<TempExcludechannel> tex_del;
        tex_del = em.createNamedQuery("TempExcludechannel.deleteAll", TempExcludechannel.class);
        tex_del.executeUpdate();
        LOG.info("現在の除外登録(転記元)を全て削除しました。");

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
        LOG.info("除外チャンネル(転記元)登録開始。");
        for (TempExcludechannel tech : tempExcludechannellList) {
            em.persist(tech);
            LOG.info("チャンネルIDを除外登録 = " + ToStringBuilder.reflectionToString(tech));
        }
        LOG.info("除外チャンネル(転記元)登録完了。");
        LOG.info("除外チャンネル(転記先)登録開始。");
        for (Excludechannel ech : excludechannellList) {
            em.persist(ech);
            LOG.info("チャンネルIDを除外登録 = " + ToStringBuilder.reflectionToString(ech));
        }
        LOG.info("除外チャンネル(転記先)登録完了。");
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
    public static List<TempExcludechannel> dumpTempExcludechannelTable() {
        try (EntityManagerMaker emm = getTestDbEm()) {
            final EntityManager em = emm.getEntityManager();
            final TypedQuery<TempExcludechannel> ql = em.createNamedQuery("TempExcludechannel.findAll", TempExcludechannel.class);
            final List<TempExcludechannel> res = ql.getResultList();
            for (TempExcludechannel ech : res) {
                LOG.info(ToStringBuilder.reflectionToString(ech));
            }
            em.close();
            return res;
        } catch (Exception ex) {
            LOG.error("{}", ex);
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
