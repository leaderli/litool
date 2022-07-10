package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.util.LiPrintUtil;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class YamlLoaderTest {

    @Test
    public void test() {

        Yaml yaml = new Yaml();
        InputStream resourceAsStream = YamlLoaderTest.class.getResourceAsStream("/a.yml");
        Map<String, Object> load1 = yaml.load(resourceAsStream);
        System.out.println(load1);
        resourceAsStream = YamlLoaderTest.class.getResourceAsStream("/b.yml");
        Map<String, Object> load2 = yaml.load(resourceAsStream);
        System.out.println(load2);

        System.out.println(LiMapUtil.merge(load1, load2));


        URL resource = YamlLoaderTest.class.getResource("/");
        System.out.println(resource);

        assert resource != null;
        File[] files = new File(resource.getFile()).listFiles(pathname -> pathname.getName().endsWith(".yml"));

        LiPrintUtil.println((Object[]) files);

    }
}
