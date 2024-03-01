package io.leaderli.litool.config;

import io.leaderli.litool.core.concurrent.ErrorHandledFuture;
import io.leaderli.litool.core.env.OSInfo;
import io.leaderli.litool.core.io.StringWriter;
import io.leaderli.litool.core.lang.Shell;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
class LiYamlConfigTest {


    @Test
    void checkYamlFormat() {


        Assertions.assertThrows(RuntimeException.class, LiYamlConfig::checkYamlFormat);
    }

    @Test
    void loadResourcesYmlFiles() {

        Assertions.assertEquals(7, LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").size());
        Assertions.assertEquals("a", LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").get("value"));
        Assertions.assertEquals("b", LiYamlConfig.loadResourcesYmlFiles("a.yml", "b.yml").get("value"));

    }

    @Test
    void isYamlFile() throws MalformedURLException {
        Assertions.assertFalse(LiYamlConfig.isYamlFile("yml"));
        Assertions.assertTrue(LiYamlConfig.isYamlFile("1.yml"));
        Assertions.assertFalse(LiYamlConfig.isYamlFile(new File("yml")));
        Assertions.assertTrue(LiYamlConfig.isYamlFile(new File("1.yml")));
        Assertions.assertFalse(LiYamlConfig.isYamlFile(new File("yml").toURI().toURL()));
        Assertions.assertTrue(LiYamlConfig.isYamlFile(new File("1.yml").toURI().toURL()));
    }


    @Test
    void shell() {
        StringWriter out = new StringWriter();
        System.setErr(new PrintStream(out));
        String[] configs = new String[]{"d.yml"};

        if (OSInfo.isWindows()) {

            configs = new String[]{"d.yml", "d_win.yml"};
        }
        String task = (String) LiYamlConfig.loadResourcesYmlFiles(configs).get("task");


        String bash = "/bin/bash";
        if (OSInfo.isWindows()) {

            bash = "\"D:\\ProgramFiles\\Git\\bin\\bash.exe\"";
        }
        ErrorHandledFuture<String> command = new Shell(new File("/")).command(bash, "-c", task);

        String x = command.get(100, TimeUnit.MILLISECONDS);
        Assertions.assertNotNull(x);


        String script = StringUtils.read(ResourceUtil.getResourceAsStream("echo.sh"));

        Assertions.assertEquals("123\n" +
                "456", new Shell(new File("/")).bash(script).get());

    }
}
