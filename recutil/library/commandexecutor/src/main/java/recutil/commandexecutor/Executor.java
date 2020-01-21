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
package recutil.commandexecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;

import loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 *
 * 元ネタ:http://chat-messenger.net/blog-entry-52.html
 */
public class Executor extends CommandExecutor {

    private static final Logger log = LoggerConfigurator.getCallerLogger();

    /**
     *
     * Process.waitFor()を実行していますので、外部コマンドの実行が 終了するまでこのメソッドは待機します。
     *
     * @return 継承元参照
     * @throws java.io.IOException 継承元参照
     * @throws java.lang.InterruptedException 継承元参照
     * @see CommandExecutor#_execCommand(String[])
     */
    @Override
    protected synchronized CommandResult _execCommand(String[] cmds) throws IOException, InterruptedException {

        try {

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmds);
            InputStream in;

            //標準出力
            in = p.getInputStream();
            String stdOut = processMessage(in);
            in.close();

            //標準エラー出力
            in = p.getErrorStream();
            String stdErr = processMessage(in);
            in.close();

            //リターンコード
            int rC = p.waitFor();

            CommandResult returns = new CommandResult(stdOut, stdErr, rC);
            return returns;
        } catch (IOException ex) {
            log.error("コマンド出力結果の取り込みに失敗。", ex);
            throw ex;
        } catch (InterruptedException ex) {
            log.error("コマンドの実行が中断されました。", ex);
            throw ex;
        }
    }

    private synchronized String processMessage(InputStream in) {
        StringBuilder sB = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String LINE_SEPA = recutil.commmonutil.Util.getDefaultLineSeparator();
            String line;
            while ((line = br.readLine()) != null) {
                sB.append(line).append(LINE_SEPA);
            }
        } catch (IOException ex) {
            log.error("コマンド出力結果の処理に失敗。", ex);
        }
        return sB.toString();
    }

}
