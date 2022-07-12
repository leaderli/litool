package io.leaderli.litool.config;

import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.util.LiIoUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class YamlConfig {


    public static void checkYamlFormat() {


        Yaml yaml = new Yaml();
        LiIoUtil.getResourcesFile(f -> LiStringUtils.endsWithAny(f.getName(), ".yml", ".yaml")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), e -> {
            throw new IllegalStateException(e);
        });
    }


}
