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

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author normal
 */
public abstract class CommandExecutor {

    private String cmd = null;

    private String[] param = null;

    /**
     * @return 渡されたコマンド
     */
    public final String getCmd() {
        return cmd;
    }

    /**
     * @return コマンドに渡されるパラメータ。
     */
    public final String[] getParam() {
        return Arrays.copyOf(param, param.length);
    }

    /**
     * 外部コマンドを引数（パラメータ）を指定して実行します。 また、標準出力、エラー出力 、リターンコードを取得します。 例：
     * execCommand(new String[]{"notepad.exe","C:\test.txt"});
     *
     *
     * @param cmd コマンド
     * @param param 実行するコマンドに渡す引数(引数,引数,...)
     * @throws java.io.IOException コマンドからの出力を取り込めなかった場合。
     * @throws java.lang.InterruptedException コマンドの実行が中断された場合。
     * @return コマンド実行結果情報を保持するCommandResult
     * @see java.lang.Runtime#exec(java.lang.String[])
     */
    public final CommandResult execCommand(String cmd, String... param) throws IOException, InterruptedException {
        this.cmd = cmd;
        this.param = Arrays.copyOf(param, param.length);
        return this._execCommand(cmd, param);
    }

    /**
     * @param cmd リンク先参照
     * @param param リンク先参照
     * @return リンク先参照
     * @throws java.io.IOException リンク先参照
     * @throws java.lang.InterruptedException リンク先参照
     * @see
     * recutil.commandexecutor.CommandExecutor#execCommand(java.lang.String,
     * java.lang.String...)
     */
    protected abstract CommandResult _execCommand(String cmd, String... param) throws IOException, InterruptedException;

}
