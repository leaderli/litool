package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiYamlConfig {


    /**
     * Check all yaml file under classpath if the format is correct.
     * it will throw {@link RuntimeException} if format is incorrect.
     */
    public static void checkYamlFormat() {


        Yaml yaml = new Yaml();
        for (File file : ResourceUtil.getResourceFile(LiYamlConfig::isYamlFile)) {
            RuntimeExceptionTransfer.run(() -> yaml.load(new FileInputStream(file)));
        }

    }

    public static boolean isYamlFile(File file) {
        return StringUtils.endsWithAny(file.getName(), ".yml", ".yaml");
    }

    /**
     * need to call {@link #checkYamlFormat()} first
     *
     * @param names multi yaml file name
     * @return merged multi yaml configuration ,  the latter have high priority
     * @see LiMapUtil#merge(Map, Map)
     */
    public static Map<String, Object> loadResourcesYmlFiles(String... names) {

        List<String> nameList = Arrays.asList(names);

        LiBox<Map<String, Object>> box = LiBox.of(new HashMap<>());
        ResourceUtil.getResourceFile(f -> nameList.contains(f.getName()))
                .sorted(Comparator.comparingInt(f -> nameList.indexOf(f.getName())))
                .throwable_map(f -> (Map<?, ?>) new Yaml().load(new FileInputStream(f)))
                .forThrowableEach(f -> box.value(LiMapUtil.merge(box.value(), f)));

        return box.value();
    }

}
