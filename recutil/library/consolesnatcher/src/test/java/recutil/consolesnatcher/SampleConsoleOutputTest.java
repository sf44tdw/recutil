/**
 * http://qiita.com/boss_ape/items/b68b0bd6d85cf18d1626
 */
package recutil.consolesnatcher;

import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 * SampleConsoleOutputクラステスト.<br>
 *
 * @author boss_ape
 */
public class SampleConsoleOutputTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * 標準出力・標準エラー出力変更管理オブジェクト.
     */
    private ConsoleSnatcher stdout = ConsoleSnatcher.getlnstance();

    /**
     * テストメソッド実行前にSystem.exit()設定を変更.
     */
    @Before
    public void changeSystem() {
        // 標準出力・標準エラー出力先を変更
        stdout.snatch();
    }

    /**
     * 標準出力・標準エラー出力先設定を戻す.<br>
     * バッファにたまっていた内容を最後にコンソール出力する。
     */
    @After
    public void resetSystem() {

        String DEFAULT_LINE_SEPARATOR = System.getProperty("line.separator");

        // バッファにたまっている内容を一時退避
        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        // コンソール出力に戻す
        stdout.release();

        // コンソール出力
        if (std.length() > 0) {
            LOG.info(DEFAULT_LINE_SEPARATOR + "--------");
            LOG.info(std);
        }
        if (std_err.length() > 0) {
            LOG.info(DEFAULT_LINE_SEPARATOR + "--------");
            LOG.info(std_err);
        }
    }

    /**
     * hogeHogeメソッド正常系のテスト.
     */
    @Test
    public void testHogeHoge正しいパラメータを渡す() {
        // バッチ実行
        SampleConsoleOutput.hogeHoge("正しいパラメータ");

        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        assertThat(std_err.length(), is(0));
        assertThat(std, is("正常終了"));
    }

    /**
     * hogeHogeメソッド異常系のテスト.
     */
    @Test
    public void testHogeHogeパラメータnullを渡す() {
        // hogeHoge実行
        SampleConsoleOutput.hogeHoge(null);

        String std = stdout.getOutput();
        String std_err = stdout.getErrorOutput();

        assertThat(std_err, is("エラーが発生"));
        assertThat(std.length(), is(0));
    }
}
