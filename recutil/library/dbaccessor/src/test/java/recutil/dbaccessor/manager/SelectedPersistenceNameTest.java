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

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class SelectedPersistenceNameTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public SelectedPersistenceNameTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        x();
    }

    @After
    public void tearDown() {
        x();
    }

    private void x() {
        SelectedPersistenceName clazz = null;
        try {
            clazz = SelectedPersistenceName.getInstance();
            try {
                //テストのたびに初期化しないと、前のテストの値が残って次に影響が出る。
                Field targetField = clazz.getClass().getDeclaredField("instance");     // 更新対象アクセス用のFieldオブジェクトを取得する。
                Field modifiersField = Field.class.getDeclaredField("modifiers");          // Fieldクラスはmodifiersでアクセス対象のフィールドのアクセス判定を行っているのでこれを更新する。
                modifiersField.setAccessible(true);                                        // modifiers自体もprivateなのでアクセス可能にする。
                modifiersField.setInt(targetField,
                        targetField.getModifiers() & ~Modifier.PRIVATE & ~Modifier.FINAL); // 更新対象アクセス用のFieldオブジェクトのmodifiersからprivateとfinalを外す。
                targetField.set(null, null);                                               // 値更新:
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                LOG.error("Reflection error.", ex);
            }
        } catch (IllegalStateException ex) {
            LOG.info("not init.", ex);
        }
    }

    /**
     * Test of selectPersistence method, of class SelectedPersistenceName.
     */
    @Test
    public void testSelectPersistence_01() {
        LOG.info("selectPersistence_01");
        PersistenceNameHolder nameHolder = PERSISTENCE.PRODUCT;
        SelectedPersistenceName.selectPersistence(nameHolder);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), nameHolder.getPersistenceName());
    }

    private class NullStr implements PersistenceNameHolder {

        @Override
        public String getPersistenceName() {
            return null;
        }
    }

    /**
     * Test of selectPersistence method, of class SelectedPersistenceName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectPersistence_02_01() {
        LOG.info("selectPersistence_02_01");
        PersistenceNameHolder nameHolder = new NullStr();
        SelectedPersistenceName.selectPersistence(nameHolder);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), nameHolder.getPersistenceName());
    }

    private class EmptyStr implements PersistenceNameHolder {

        @Override
        public String getPersistenceName() {
            return "";
        }
    }

    /**
     * Test of selectPersistence method, of class SelectedPersistenceName.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSelectPersistence_02_02() {
        LOG.info("selectPersistence_02_02");
        PersistenceNameHolder nameHolder = new EmptyStr();
        SelectedPersistenceName.selectPersistence(nameHolder);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), nameHolder.getPersistenceName());
    }

    /**
     * Test of selectPersistence method, of class SelectedPersistenceName.
     */
    @Test
    public void testSelectPersistence_03_01() {
        LOG.info("selectPersistence_03_01");
        SelectedPersistenceName.selectPersistence(PERSISTENCE.PRODUCT);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), PERSISTENCE.PRODUCT.getPersistenceName());
        SelectedPersistenceName.selectPersistence(PERSISTENCE.TEST);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), PERSISTENCE.PRODUCT.getPersistenceName());
    }

    /**
     * Test of getInstance method, of class SelectedPersistenceName.
     */
    @Test
    public void testGetInstance_01() {
        LOG.info("getInstance_01");
        PersistenceNameHolder nameHolder = PERSISTENCE.PRODUCT;
        SelectedPersistenceName.selectPersistence(nameHolder);
        assertEquals(SelectedPersistenceName.getInstance().getPersistenceName(), nameHolder.getPersistenceName());
    }

    /**
     * Test of getInstance method, of class SelectedPersistenceName.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetInstance_02() {
        LOG.info("getInstance_02");
        @SuppressWarnings("unused")
		SelectedPersistenceName result = SelectedPersistenceName.getInstance();
    }

}
