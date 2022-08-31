package io.leaderli.litool.config;

import io.leaderli.litool.core.collection.LiMapUtil;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.io.FileNameUtil;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
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

        Lira<File> resourceFile = ResourceUtil.getResourceFile(LiYamlConfig::isYamlFile);

        for (File file : resourceFile) {
            System.out.println(file);
            RuntimeExceptionTransfer.run(() -> yaml.load(Files.newInputStream(file.toPath())));
        }

    }

    public static boolean isYamlFile(File file) {
        return isYamlFile(file.getName());
    }

    public static boolean isYamlFile(String fileName) {
        return StringUtils.equalsAny(FileNameUtil.extName(fileName), "yml", "yaml");
    }

    public static boolean isYamlFile(URL url) {
        return isYamlFile(url.getFile());
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
                .throwable_map(f -> (Map<?, ?>) new Yaml().load(Files.newInputStream(f.toPath())))
                .forThrowableEach(f -> box.value(LiMapUtil.merge(box.value(), f)));

        return box.value();
    }

}
