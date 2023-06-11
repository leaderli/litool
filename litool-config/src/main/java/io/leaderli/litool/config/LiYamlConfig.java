package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiFunction;

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

        Lira<File> resourceFile = ResourceUtil.getResourceFiles(LiYamlConfig::isYamlFile);

        for (File file : resourceFile) {
            RuntimeExceptionTransfer.run(() -> yaml.load(Files.newInputStream(file.toPath())));
        }

    }

    /**
     * @param file -
     * @return 是否为yaml文件
     */
    public static boolean isYamlFile(File file) {
        return isYamlFile(file.getName());
    }

    /**
     * @param fileName -
     * @return 是否为yaml文件
     */
    public static boolean isYamlFile(String fileName) {
        return StringUtils.equalsAny(FileNameUtil.extName(fileName), "yml", "yaml");
    }

    /**
     * @param url -
     * @return 是否为yaml文件
     */
    public static boolean isYamlFile(URL url) {
        return isYamlFile(url.getFile());
    }

    /**
     * better to call {@link #checkYamlFormat()}  check all yaml file is well format
     * <p>
     * support placeholder in yaml file. it can invoke {@link  System#getProperty(String)} or {@link BeanPath#parse(Object, String)} from it's own properties
     * <p>
     * eg:
     * <pre>{@code
     * task: '${java.home}'
     * map:
     *   k: 1
     *   list:
     *     - 1
     *     - 2
     * hello: 1
     * a: ${hello}
     * b: ${map}
     * }</pre>
     *
     * @param names multi yaml file name
     * @return merged multi yaml configuration,  the latter have high priority
     * @see LiMapUtil#merge(Map, Map)
     * @see StrSubstitution#$parse(String, BiFunction)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> loadResourcesYmlFiles(String... names) {

        List<String> nameList = Arrays.asList(names);

        LiBox<Map> box = LiBox.of(new HashMap<>());
        Yaml yaml = new Yaml();

        ResourceUtil.getResourceFiles(f -> nameList.contains(f.getName()))
                .sorted(Comparator.comparingInt(f -> nameList.indexOf(f.getName())))
                .throwable_map(f -> (Map<?, ?>) yaml.load(Files.newInputStream(f.toPath())))
                .forThrowableEach(f -> {
                    Map<?, ?> merge = LiMapUtil.merge(box.value(), f);
                    box.value(merge);
                });

        Map<Object, Object> config = box.value();

        String mergeYaml = yaml.dumpAsMap(config);

        // replace placeholder
        mergeYaml = StrSubstitution.$parse(mergeYaml, (variable, def) -> {

            String property = System.getProperty(variable);
            if (property != null) {
                return property;
            }
            Object value = BeanPath.parse(config, variable).get();
            return yaml.dumpAs(value, null, DumperOptions.FlowStyle.FLOW);
        });

        return yaml.load(mergeYaml);
    }

}
