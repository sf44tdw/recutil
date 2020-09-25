package recutil.simpleatrunner;

import static recutil.commmonutil.Util.*;
import static recutil.reservecommon.AtExecutor.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.manager.CsvManagerFactory;

import loggerconfigurator.LoggerConfigurator;
import recutil.commandexecutor.CommandExecutor;
import recutil.commandexecutor.CommandResult;
import recutil.commandexecutor.Executor;
import recutil.reservecommon.AtExecutor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author normal
 */
public class Main {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    public static final String getSep() {
        return getDefaultLineSeparator();
    }

    private static String dumpArgs(String[] args) {
        return ArrayUtils.toString(args, "引数なし。");
    }

    public static void main(String[] args) {
        try {
            new Main().start(new Executor(), args);
            System.exit(0);
        } catch (Throwable ex) {
            String s="エラー。 引数 = " + dumpArgs(args);
            System.out.println(s);
            System.out.println(ex);
            LOG.error(s, ex);
            System.exit(1);
        }
    }

    private Date DateTimeFormatter(String str) throws java.text.ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(str);
    }

    protected void start(final CommandExecutor executor, final String[] args) throws ParseException, InterruptedException {
        final Option inputCsvFileOption = Option.builder("f")
                .longOpt("csvfile")
                .desc("実行予約日時とコマンドのペアを格納したCSVの絶対パスを指定する。相対パスを指定した場合の結果は保証しない。")
                .hasArg(true)
                .required(true)
                .type(File.class)
                .build();

        Options opts = new Options();
        opts.addOption(inputCsvFileOption);
        CommandLineParser parser = new DefaultParser();

        HelpFormatter help = new HelpFormatter();
        CommandLine cl;
        try {
            cl = parser.parse(opts, args);
        } catch (org.apache.commons.cli.ParseException ex) {
            help.printHelp("使用法", opts);
            throw ex;
        }

        final String optVal_f;
        final File inputCsvFile;
        if (cl.hasOption(inputCsvFileOption.getOpt())) {
        	optVal_f = cl.getOptionValue(inputCsvFileOption.getOpt());
        	inputCsvFile = relativePahtToAbsolutePath(optVal_f);
        } else {
            inputCsvFile = null;
        }

        if (inputCsvFile == null) {
            throw new IllegalArgumentException("CSVファイルが指定されていません。");
        }

        if (!inputCsvFile.isFile()) {
            throw new IllegalArgumentException(inputCsvFile.getAbsolutePath() + " は、有効なファイルではありません。");
        }

        List<Record> _timeAndcommands;
        try {
            CsvConfig cfg = new CsvConfig();
            cfg.setQuoteDisabled(false);                         // デフォルトでは無効となっている囲み文字を有効にします。
            cfg.setEscapeDisabled(false);                        // デフォルトでは無効となっている(囲み文字中に囲み文字を使う場合の)エスケープ文字を有効にします。
            cfg.setIgnoreLeadingWhitespaces(true);               // 項目値前のホワイトスペースを除去します。
            cfg.setIgnoreTrailingWhitespaces(true);              // 項目値後のホワイトスペースを除去します。

            //読み込み
            _timeAndcommands = CsvManagerFactory.newCsvManager().config(cfg).load(Record.class).from(inputCsvFile);
        } catch (IOException ex) {
            LOG.error("ファイルの読み込みに失敗しました。 " + inputCsvFile.getAbsolutePath(), ex);
            _timeAndcommands = new ArrayList<>();
        }
        final List<Record> timeAndcommands = _timeAndcommands;
        _timeAndcommands = null;

        final AtExecutor exec = new AtExecutor(executor);

        for (Record r : timeAndcommands) {
            Date _dat;
            LOG.info(r.toString());
            try {
                _dat = this.DateTimeFormatter(r.getReserveDateTime());
            } catch (java.text.ParseException ex) {
                LOG.error("日付の変換に失敗しました。 形式は、 " + DATE_FORMAT + " です。" + r.toString());
                _dat = null;
            }
            final Date dat = _dat;
            _dat = null;
            if (dat != null) {
                CommandResult res = exec.executeAt(dat, "予約", r.getCommand());

                if (res != null) {
                    LOG.info(res.toString());
                    //何故かatコマンドの出力はエラー出力のほうに出てくるので、そっちを表示する。
                    System.out.println(res.getStandardError());
                    if (res.getReturnCode() != 0) {
                        throw new IllegalArgumentException("予約に失敗した可能性があります。");
                    } else {
                        System.out.println("予約が行われました。");
                    }
                } else {
                    LOG.error("予約コマンドの実行中に何らかの問題が発生しました。予約の成功は保証されません。");
                }
            }
        }

    }
}
