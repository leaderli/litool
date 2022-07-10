package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class YamlLoaderTest {

    @Test
    public void test() throws Throwable {

        Yaml yaml = new Yaml();
        InputStream resourceAsStream = YamlLoaderTest.class.getResourceAsStream("/a.yml");
        Map load1 = yaml.load(resourceAsStream);
        System.out.println(load1);
        resourceAsStream = YamlLoaderTest.class.getResourceAsStream("/b.yml");
        Map load2 = yaml.load(resourceAsStream);
        System.out.println(load2);

        System.out.println(LiMapUtil.merge(load1, load2));


    }
}
