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

import java.text.MessageFormat;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 実際にはコマンドを実行しないクラス。 渡されたコマンドとパラメータを記憶し、あとで取得できる。
 *
 * @author normal
 */
public final class DummyExecutor extends CommandExecutor {


    @Override
    public CommandResult _execCommand(String cmd, String... param) {
         final String msg = "これはダミーです。何もしません。";
        final String param_s = ArrayUtils.toString(param, "引数なし。");
        Object[] p = {msg, cmd, param_s};
        CommandResult returns = new CommandResult(new MessageFormat("message = {0}, command = {1}, parameters = {2}").format(p), msg, 0);
        return returns;
    }

}
