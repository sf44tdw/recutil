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
package recutil.loggerconfigurator;

import org.slf4j.Logger;
import org.slf4j.helpers.NOPLogger;

/**
 * このクラスのサブクラスでロガーを取得する際に抑止アノテーションを使用し、期待通りのロガーが取得されたか判定する。
 * @author normal
 */
public abstract class SuppressAnnotationTester {

    /**
     * 出力抑止状態を示す定数。
     */
    public static enum SUPPRESS_STATUS {
        /**
         * ログ出力が抑止されている。
         */
        SUPPRESS, 
        /**
         * ログ出力が抑止されていない。
         */
        NOT_SUPPRESS
    };

    private final Logger _LOG;

    private final SUPPRESS_STATUS expectStatus;

    /**
     * @param _LOG サブクラスで取得したロガー。
     * @param expectStatus 期待される出力抑止状態。
     */
    public SuppressAnnotationTester(Logger _LOG, SUPPRESS_STATUS expectStatus) {
        this._LOG = _LOG;
        this.expectStatus = expectStatus;
    }

    /**
     * ログ出力が抑止されているか。
     * @return 出力抑止状態を示す定数。
     */
    public final SUPPRESS_STATUS isSuppress() {
        final SUPPRESS_STATUS loggerStat;
        if (this._LOG instanceof NOPLogger) {
            loggerStat = SUPPRESS_STATUS.SUPPRESS;
        } else {
            loggerStat = SUPPRESS_STATUS.NOT_SUPPRESS;
        }
        return loggerStat;
    }

    public SUPPRESS_STATUS getExpectStatus() {
        return expectStatus;
    }

    /**
     * 期待される出力抑止状態か?
     * 
     * @return 期待される出力抑止状態ならばtrue。それ以外はfalse。
     */
    public final boolean isExpectedLogger() {
        return (this.getExpectStatus() == this.isSuppress());
    }

}
