package io.leaderli.litool.config;

import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class LiYamlLoaderTest {

    @Test
    public void checkYamlFormat() {
        System.out.println(ResourceUtil.getResource("/"));
        Yaml yaml = new Yaml();
        List<Throwable> error = new ArrayList<>();
        ResourceUtil.getResourceFile(f -> f.getName().endsWith(".yml")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), error::add);

        Assertions.assertSame(error.size(), 0);
        ResourceUtil.getResourceFile(f -> f.getName().endsWith(".txt")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), error::add);
        Assertions.assertSame(error.size(), 1);

    }

    @Test
    public void loadResourcesYmlFiles() {


        System.out.println();

        Assertions.assertEquals(7, LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").size());
        Assertions.assertEquals("a", LiYamlConfig.loadResourcesYmlFiles("b.yml", "a.yml").get("value"));
        Assertions.assertEquals("b", LiYamlConfig.loadResourcesYmlFiles("a.yml", "b.yml").get("value"));


    }

}
