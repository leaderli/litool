package io.leaderli.litool.action;

import io.leaderli.litool.config.LiYamlConfig;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/10/12 7:38 PM
 */
class ActionLoaderTest {

    @Test
    void test() {


        System.out.println(LiYamlConfig.loadResourcesYmlFiles("action.yml"));
        System.out.println(LiYamlConfig.loadResourcesYmlFiles("action.yml").get("ab") == null);

    }

}
