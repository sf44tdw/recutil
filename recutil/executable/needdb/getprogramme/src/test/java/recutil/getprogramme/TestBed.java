/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.getprogramme;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.dbaccessor.entity.Channel;
import recutil.dbaccessor.entity.Excludechannel;
import recutil.dbaccessor.entity.Programme;
import recutil.dbaccessor.manager.EntityManagerMaker;
import recutil.dbaccessor.manager.SelectedPersistenceName;
import recutil.dbaccessor.testdata.TestData;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * Criteria API動作確認用
 * @author normal
 */
public class TestBed {

    private static final boolean EXECUTE_FLAG = true;

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    final TestData dat;

    public TestBed() {
        if (!EXECUTE_FLAG) {
            dat = new TestData();
        } else {
            dat = null;
        }
    }

    @Test
    public void criteriaTest_excludeChannel() {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = false;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        dat.reloadDB();
        LOG.info("criteriaTest_excludeChannel");
        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();
            final CriteriaBuilder builder = man.getCriteriaBuilder();
            final CriteriaQuery<Excludechannel> query = builder.createQuery(Excludechannel.class);
            final Root<Excludechannel> root = query.from(Excludechannel.class);
            query.select(root).distinct(true);
            final TypedQuery<Excludechannel> ql;
            ql = man.createQuery(query);
            List<Excludechannel> table;
            table = ql.getResultList();
            String xx = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    @Test
    public void criteriaTest_excludeChannel_Tuple() {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = false;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        dat.reloadDB();
        LOG.info("criteriaTest_excludeChannel_Tuple");
        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();
            final CriteriaBuilder builder = man.getCriteriaBuilder();
            final CriteriaQuery<String> query = builder.createQuery(String.class);
            final Root<Excludechannel> root = query.from(Excludechannel.class);
            query.select(root.get("channelId")).distinct(true);
            final TypedQuery<String> ql;
            ql = man.createQuery(query);
            List<String> table;
            table = ql.getResultList();
            String xx = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    @Test
    public void criteriaTest_channel() {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = false;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        dat.reloadDB();
        LOG.info("criteriaTest_channel");
        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {
            EntityManager man = mk.getEntityManager();
            final CriteriaBuilder builder = man.getCriteriaBuilder();
            final CriteriaQuery<Channel> query = builder.createQuery(Channel.class);
            final Root<Channel> root = query.from(Channel.class);
            query.where(builder.equal(root.get("channelId"), TestData.CH1_ID));

            final TypedQuery<Channel> ql;
            ql = man.createQuery(query);
            List<Channel> table;
            table = ql.getResultList();
            String xx = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx);
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }

    @Test
    public void criteriaTest_Channel_Without_excludeChannel() {
        if (!EXECUTE_FLAG) {
            return;
        }
        final boolean METHOD_EXECUTE_FLAG = false;
        if (!METHOD_EXECUTE_FLAG) {
            return;
        }
        dat.reloadDB();
        LOG.info("criteriaTest_Programme_Without_excludeChannel");
        try (EntityManagerMaker mk = new EntityManagerMaker(SelectedPersistenceName.getInstance())) {

            EntityManager man = mk.getEntityManager();
            final EntityTransaction trans = man.getTransaction();
            trans.begin();

            final CriteriaBuilder builder = man.getCriteriaBuilder();
            final CriteriaQuery<String> query_ex = builder.createQuery(String.class);
            final Root<Excludechannel> root_ex = query_ex.from(Excludechannel.class);
            query_ex.select(root_ex.get("channelId")).distinct(true);
            final TypedQuery<String> ql_ex;
            ql_ex = man.createQuery(query_ex);
            List<String> table_ex;
            table_ex = ql_ex.getResultList();

            final CriteriaQuery<Programme> query = builder.createQuery(Programme.class);
            final Root<Programme> root = query.from(Programme.class);
            Expression<String> exp = root.get("channelId");
            Predicate predicate = exp.in(table_ex).not();
            query.where(predicate);

            final TypedQuery<Programme> ql;
            ql = man.createQuery(query);
            List<Programme> table;

            table = ql.getResultList();
            String xx = recutil.commmonutil.Util.dumpList(table);
            LOG.info(xx);
            trans.commit();
        } catch (Throwable ex) {
            LOG.error("エラー。", ex);
            throw ex;
        }
    }
}
