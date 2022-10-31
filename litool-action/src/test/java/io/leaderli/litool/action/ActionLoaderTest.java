package io.leaderli.litool.action;

import io.leaderli.litool.config.LiYamlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/10/12 7:38 PM
 */
class ActionLoaderTest {

    @Test
    void test() {


        Assertions.assertNotNull(LiYamlConfig.loadResourcesYmlFiles("action.yml"));
        Assertions.assertNull(LiYamlConfig.loadResourcesYmlFiles("action.yml").get("ab"));

    }

}
