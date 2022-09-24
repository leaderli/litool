package io.leaderli.litool.config;

import io.leaderli.litool.core.concurrent.CatchFuture;
import io.leaderli.litool.core.env.OSInfo;
import io.leaderli.litool.core.lang.Shell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
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
        String[] configs = new String[]{"d.yml"};

        if (OSInfo.isWindows()) {

            configs = new String[]{"d.yml", "d_win.yml"};
        }
        String task = (String) LiYamlConfig.loadResourcesYmlFiles(configs).get("task");


        System.out.println(task);
        String bash = "/bin/bash";
        if (OSInfo.isWindows()) {

            bash = "\"D:\\ProgramFiles\\Git\\bin\\bash.exe\"";
        }
        CatchFuture<String> command = new Shell(new File("/")).command(bash, "-c", task);
        System.out.println(command);

        while (!command.isDone()) {
            String x = command.get(100, TimeUnit.MILLISECONDS);
            if (!x.isEmpty()) {

                System.out.println(x);
            }
        }
    }
}
