package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.util.LiResourceUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.util.*;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiYamlConfig {


    /**
     * 校验资源目录下的所有 yml 文件 的是否可以被正确加载
     */
    public static void checkYamlFormat() {


        Yaml yaml = new Yaml();
        LiResourceUtil.getResourceFile(f -> LiStringUtils.endsWithAny(f.getName(), ".yml", ".yaml")).forThrowableEach(f -> yaml.load(new FileInputStream(f)), e -> {
            throw new IllegalStateException(e);
        });
    }

    /**
     * @param names 多个配置文件名称
     * @return 合并多个yml配置项，后面的优先级更高
     * @see LiMapUtil#merge(Map, Map)
     */
    public static Map<String, Object> loadResourcesYmlFiles(String... names) {

        List<String> strings = Arrays.asList(names);

        LiBox<Map<String, Object>> result = LiBox.of(new HashMap<>());
        LiResourceUtil.getResourceFile(f -> strings.contains(f.getName()))
                .sort(Comparator.comparingInt(f -> strings.indexOf(f.getName())))
                .forThrowableEach(
                        f -> result.value(LiMapUtil.merge(result.value(), new Yaml().load(new FileInputStream(f)))),
                        e -> {
                            throw new IllegalStateException(e);
                        });
        return result.value();
    }

}
