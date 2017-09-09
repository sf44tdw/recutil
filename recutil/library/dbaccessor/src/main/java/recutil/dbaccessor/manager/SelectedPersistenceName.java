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

import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * 接続先の名前を保持するクラス。一度設定した接続先名は変更できない。
 *
 * @author normal
 */
public final class SelectedPersistenceName {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    private static SelectedPersistenceName instance;

    public static synchronized void selectPersistence(PersistenceNameHolder nameHolder) {
        if (instance == null) {
            final String x;
            if (nameHolder != null) {
                x = nameHolder.getPersistenceName();
            } else {
                x = null;
            }
            if (x == null || "".equals(x)) {
                throw new IllegalArgumentException("接続先が選択されていません。");
            }

            instance = new SelectedPersistenceName(nameHolder);
        } else {
            LOG.warn("接続先は設定済みです。変更できません。引数は無視されます。 現在 = {} 引数 = {}", instance.getPersistenceName(), nameHolder.getPersistenceName());
        }
    }

    public static synchronized SelectedPersistenceName getInstance() {
        if (instance == null) {
            throw new IllegalStateException("接続先が設定されていません。");
        }
        return instance;
    }

    private final String PersistenceName;

    public SelectedPersistenceName(PersistenceNameHolder nameHolder) {
        this.PersistenceName = nameHolder.getPersistenceName();
    }

    public String getPersistenceName() {
        return PersistenceName;
    }

}
