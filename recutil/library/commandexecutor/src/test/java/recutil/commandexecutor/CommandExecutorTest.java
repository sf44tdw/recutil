/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recutil.commandexecutor;

import java.io.IOException;
import org.junit.Test;
import org.slf4j.Logger;
import recutil.loggerconfigurator.LoggerConfigurator;

/**
 *
 * @author normal
 */
public class CommandExecutorTest {

    private static final Logger LOG = LoggerConfigurator.getCallerLogger();

    /**
     * Test of execCommand method, of class CommandExecutor.
     * @throws java.lang.Exception テスト対象参照。
     */
    @Test
    public void testExecCommand_String() throws Exception {
        LOG.info("execCommand");
        String cmd = "java";
        Executor instance = new Executor();
        CommandResult result = instance.execCommand(cmd);
        LOG.info(result.toString());
    }

    /**
     * Test of execCommand method, of class CommandExecutor.
     * @throws java.io.IOException  テスト対象参照。
     * @throws java.lang.InterruptedException  テスト対象参照。
     */
    @Test
    public void testExecCommand_StringArr() throws IOException, InterruptedException {
        LOG.info("execCommand_arr");
        String cmd = "at";
        String[] params = new String[]{"-t", "211201010000", "-f", "/usr/local/bin/ptrec.sh"};
        Executor instance = new Executor();
        CommandResult result = instance.execCommand(cmd, params);
        LOG.info(result.toString());
    }

    /**
     * そんなコマンドはない。
     * @throws java.io.IOException  テスト対象参照。
     * @throws java.lang.InterruptedException  テスト対象参照。
     */
    @Test(expected = IOException.class)
    public void testExecCommand_StringArr02() throws IOException, InterruptedException {
        LOG.info("execCommand_arr02");
        String cmd = "fuga";
        String[] params = new String[]{"--help"};
        Executor instance = new Executor();
        CommandResult result = instance.execCommand(cmd, params);
        LOG.info(result.toString());
    }
}
