package io.leaderli.litool.config;

import io.leaderli.litool.core.util.LiIoUtil;
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
public class YamlLoaderTest {

    @Test
    public void checkYamlFormat() {

        Yaml yaml = new Yaml();
        List<Throwable> error = new ArrayList<>();
        LiIoUtil.getResourcesFile(f -> f.getName().endsWith(".yml")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), error::add);

        Assertions.assertSame(error.size(), 0);
        LiIoUtil.getResourcesFile(f -> f.getName().endsWith(".txt")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), error::add);
        Assertions.assertSame(error.size(), 1);

    }

}
