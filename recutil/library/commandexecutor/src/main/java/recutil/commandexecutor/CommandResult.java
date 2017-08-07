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
package recutil.commandexecutor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * コマンド実行結果情報を保持する
 *
 * @author normal
 */
public class CommandResult {

    private final String standardOutput;
    private final String standardError;
    private final int returnCode;

    public CommandResult(String standardOutput, String standardError, int returnCode) {
        this.standardOutput = standardOutput;
        this.standardError = standardError;
        this.returnCode = returnCode;
    }

    /**
     *
     * @author normal
     * @return コマンド実行結果のうち、標準出力に送られたもの。
     */
    public String getStandardOutput() {
        return standardOutput;
    }

    /**
     *
     * @author normal
     * @return コマンド実行結果のうち、標準エラー出力に送られたもの。
     */
    public String getStandardError() {
        return standardError;
    }

    /**
     *
     * @author normal
     * @return リターンコード
     */
    public int getReturnCode() {
        return returnCode;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 43, this);
    }

     @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
