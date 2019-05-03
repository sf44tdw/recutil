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

/**
 *
 * @author normal
 */
public enum PERSISTENCE implements PersistenceNameHolder {
    PRODUCT("recutil_dbaccessor"), TEST("recutil_dbaccessor_Test");
    private final String persistenceName;

    private PERSISTENCE(String persistenceName) {
        this.persistenceName = persistenceName;
    }

    @Override
    public String getPersistenceName() {
        return persistenceName;
    }

}
