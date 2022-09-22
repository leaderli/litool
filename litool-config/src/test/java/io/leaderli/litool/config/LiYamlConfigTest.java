package io.leaderli.litool.config;

import io.leaderli.litool.core.lang.Shell;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;

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


    //    @Test
    void shell() {

        String task = (String) LiYamlConfig.loadResourcesYmlFiles("d.yml").get("task");

        new Shell(new File("/")).command("/bin/bash", "-c", task);
    }
}
