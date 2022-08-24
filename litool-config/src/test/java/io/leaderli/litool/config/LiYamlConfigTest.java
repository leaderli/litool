package io.leaderli.litool.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class LiYamlConfigTest {

    @Test
    public void checkYamlFormat() {
        Assertions.assertThrows(RuntimeException.class, LiYamlConfig::checkYamlFormat);
    }

    @Test
    public void loadResourcesYmlFiles() {

        Assertions.assertEquals(7, LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").size());
        Assertions.assertEquals("a", LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").get("value"));
        Assertions.assertEquals("b", LiYamlConfig.loadResourcesYmlFiles("a.yml", "b.yml").get("value"));


    }

}
