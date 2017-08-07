/**
 * http://qiita.com/boss_ape/items/b68b0bd6d85cf18d1626
 */
package recutil.consolesnatcher;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * 標準出力・標準エラー出力の出力先をコンソールから変更するクラス.くbr>
 * コンソール出力内容をテストしたい場合に使用する。<br>
 * 本クラスはスレッドセーフではないのでマルチスレッド環境では利用不可。くbr>
 * シングルトンクラス。<br>
 * log4jの出力内容を取得する場合はConsoleAppenderで、標準出力する。<br>
 * 
 * ロガーを標準出力につなげているとそれも持ってくるので、
 * コンソール出力以外が出てこないようにして使用すること。
 *
 * @author boss_ape
 */
public final class ConsoleSnatcher
{
    /** シングルトンパターンのためのインスタンス. */
    private static final ConsoleSnatcher INSTANCE = new ConsoleSnatcher();

    /** 変更前の標準出力. */
    private PrintStream nativeOut = null;

    /** 元の標準エラー出力. */
    private PrintStream nativeErr = null;

    /** 変更後の標準出力. */
    private ByteArrayOutputStream snatchedOut = new ByteArrayOutputStream();

    /** 変更後の標準エラー出力. */
    private ByteArrayOutputStream snatchedErr = new ByteArrayOutputStream();

    /** 出力先変更済みフラグ. */
    private boolean stealFlag = false;

    /**
     * デフォルトコンストラクタの禁止.
     */
    private ConsoleSnatcher() { }

    /**
     * このクラスのインスタンスを取得
     *
     * @return このクラスのインスタンス
     */
    public static ConsoleSnatcher getlnstance()
    {
        return INSTANCE;
    }

    /**
     * 出力先をコンソールからこのクラスへ出力するように変更する.
     */
    public void snatch()
    {
        // 2回以上の変更禁止
        if (!stealFlag) {
            // 現在の出力先の保存
            nativeOut = System.out;
            nativeErr = System.err;

            // 出力先変更
            System.setOut(new PrintStream(new BufferedOutputStream(snatchedOut)));
            System.setErr(new PrintStream(new BufferedOutputStream(snatchedErr)));

            //変更済みフラグ設定
            stealFlag = true;
        }
    }

    /**
     * 標準出力のクリア.
     */
    public void clearOutput()
    {
        snatchedOut.reset();
    }

    /**
     * 標準エラー出力のクリア.
     */
    public void clearErrorOutput()
    {
        snatchedErr.reset();
    }

    /**
     * 標準出力内容の取得.
     *
     * @return 標準出力内容
     */
    public String getOutput()
    {
        System.out.flush();
        return snatchedOut.toString();
    }

    /**
     * 標準エラー出力内容の取得.
     *
     * @return標準エラー出力内容
     */
    public String getErrorOutput()
    {
        System.err.flush();
        return snatchedErr.toString();
    }

    /**
     * 元の標準出力先の取得.
     *
     * @return 元の標準出力先
     */
    public PrintStream getNativeOutputStream()
    {
        return nativeOut;
    }

    /**
     * 元の標準エラー出力先の取得.
     *
     * @return 奪取した標準エラー出力先
     */
    public PrintStream getNativeErrorOutputStream()
    {
        return nativeErr;
    }

    /**
     * 出力先を元に戻すくbr>
     * 使用後は必ずこのメソッドを呼び出す。
     */
    public void release()
    {
        // 出力先が変更されている場合のみ実施
        if (stealFlag) {
            clearOutput();
            clearErrorOutput();

            //出力先を元に戻す
            System.setOut(nativeOut);
            System.setErr(nativeErr);

            stealFlag = false;
        }
    }
}
