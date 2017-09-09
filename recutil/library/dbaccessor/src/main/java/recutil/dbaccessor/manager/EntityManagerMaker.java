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
package recutil.dbaccessor.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author normal
 */
public final class EntityManagerMaker implements AutoCloseable {

    private final SelectedPersistenceName name;

    private final EntityManagerFactory emf;

    /**
     * 接続するDBを選択できる。テスト用。
     *
     * @param name 接続先。
     */
    public EntityManagerMaker(SelectedPersistenceName name) {
        if (name == null) {
            throw new NullPointerException("Persistenceが選択されていません。");
        } else {
            this.name = name;
            emf = Persistence.createEntityManagerFactory(this.name.getPersistenceName());
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public void close() {
        emf.close();
    }

}
