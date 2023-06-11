package io.leaderli.litool.action;

import io.leaderli.litool.config.LiYamlConfig;
import io.leaderli.litool.core.lang.BeanPath;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import org.yaml.snakeyaml.Yaml;

public class ActionLoader {

    private ActionLoader() {
    }

    static {
        LiYamlConfig.checkYamlFormat();
    }

    /**
     * loading config bean from a yaml file, this yaml support placeholder of {@link  System#getProperty(String)}
     * and {@link  BeanPath#parse(Object, String)} from additional config bean
     *
     * @param actionClass  the action config class
     * @param yamlFullPath the yamlFullPath file  path
     * @param config       the additional config bean for replace placeholder of yaml file content
     * @param <T>          the type of action config
     * @return the config bean
     */
    public static <T> T loadFromYaml(Class<T> actionClass, String yamlFullPath, Object config) {

        String actionYml = StringUtils.read(ResourceUtil.getResourceAsStream(yamlFullPath));

        actionYml = StrSubstitution.$format(actionYml, (variable, def) -> {

            String property = System.getProperty(variable);
            if (property != null) {
                return property;
            }
            return BeanPath.parse(config, variable).get();
        });

        return new Yaml().loadAs(actionYml, actionClass);
    }


}
