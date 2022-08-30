package io.leaderli.litool.config;

import io.leaderli.litool.core.resource.ResourceUtil;
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
        System.out.println(ResourceUtil.getResourceFile(null));

        System.out.println(ResourceUtil.getResourcesLira(""));
        System.out.println(ResourceUtil.getResourceFile(f -> true));

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

}
