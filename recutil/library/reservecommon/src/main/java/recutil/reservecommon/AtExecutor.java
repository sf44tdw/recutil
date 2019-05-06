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
package recutil.reservecommon;

import static recutil.reservecommon.AtExecutor.RESERVE_COMMAND_PARAMS.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;

import recutil.commandexecutor.CommandExecutor;
import recutil.commandexecutor.CommandResult;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *atコマンドを使用して指定の日時において何らかのコマンドを実行するよう設定する。<br>
 *実行環境において。atコマンドへのパスが通っていることを前提とする。
 *
 * @author normal
 */
public final class AtExecutor {

    public final class RESERVE_COMMAND_PARAMS {

        private RESERVE_COMMAND_PARAMS() {
        }

        public static final String RESERVE_COMMAND = "at";
        public static final String OPTION_TIME = "-t";
        public static final String OPTION_FILE = "-f";
    }

    /**
     * 日付フォーマット
     */
    public static final String DATE_FORMAT = "yyyyMMddHHmm";

    /**
     * 現在時刻から何秒後から予約できるか。
     */
    public static final long DIFF = 120000L;

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();
    private final CommandExecutor executor;

    public AtExecutor(CommandExecutor executor) {
        this.executor = executor;
        if (executor == null) {
            throw new NullPointerException("コマンド実行用クラスがありません。");
        }
    }

    private boolean stringCheck(String s) {
        if (s != null && !"".equals(s)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFuture(Date dat) {
        if (dat == null) {
            throw new NullPointerException("コマンド実行の予約時刻がnullです。");
        }
        long x = dat.getTime();
        long y = System.currentTimeMillis();
        if (x >= (y + DIFF)) {
            return true;
        } else {
            LOG.warn("現在から見て一定時間以内は指定できません。 予約限度時刻 = {}, 現在時刻 ={}", new Date(y), new Date(x + DIFF));
            return false;
        }
    }

    public CommandResult executeAt(Date reserveDateTime, final String sign, final String targetCommand) throws InterruptedException {
        final File tempFile;

        if (!this.isFuture(reserveDateTime)) {
            throw new IllegalArgumentException("予約までの時間が近すぎます。");
        }
        final Date t2 = new Date(reserveDateTime.getTime());

        try {
            tempFile = File.createTempFile("myApp", ".tmp");
            tempFile.deleteOnExit();

            try ( //テンポラリファイルに録画コマンドを書き込む。
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)));) {
                if (stringCheck(sign)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("at -cで確認したときに出てくる内容に含ませるテキスト = {}", sign);
                    }
                    bw.write(sign);
                    bw.newLine();
                }

                if (stringCheck(targetCommand)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("コマンド = {}", targetCommand);
                    }
                }
                bw.write(targetCommand);
                bw.newLine();
            }

            String s2 = new SimpleDateFormat(DATE_FORMAT).format(t2);
            CommandResult res = executor.execCommand(RESERVE_COMMAND, OPTION_TIME, s2, OPTION_FILE, tempFile.getAbsolutePath());
            if (res != null) {
                LOG.info("予約コマンド実行。");
                LOG.info(res.toString());
                return res;
            } else {
                throw new IllegalArgumentException("予約コマンドの実行中に何らかの問題が発生しました。予約の成功は保証されません。");
            }
        } catch (IOException ex) {
            LOG.error("予約コマンドの生成に失敗しました。", ex);
            return null;
        }
    }

}
