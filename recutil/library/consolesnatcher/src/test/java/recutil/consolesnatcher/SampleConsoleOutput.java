/**
 * http://qiita.com/boss_ape/items/b68b0bd6d85cf18d1626
 */
package recutil.consolesnatcher;

/**
 * 標準出力と標準エラー出力するサンプル処理.<br>
 * 0:正常終了<br>
 * 1:異常終了
 *
 * @author boss_ape
 */
public class SampleConsoleOutput
{
    /**
     * fugafugaをhogehogeする
     *
     * @param args パラメータnullではない事
     */
    public static void hogeHoge(String arg)
    {
        try {
            // パラメータがnullならException発生
            if (null == arg)
                throw new NullPointerException();

        } catch (NullPointerException e) {
            // 標準エラー出力
            System.err.print("エラーが発生");
            return;
        }

        // 標準出力
        System.out.print("正常終了");
        return;
    }
}