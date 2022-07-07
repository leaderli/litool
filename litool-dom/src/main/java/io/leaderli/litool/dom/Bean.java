package io.leaderli.litool.dom;

import io.leaderli.litool.core.util.LiStrUtil;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class Bean extends SaxBean<Bean> {
    public String name;
    public String version;

    public Bean() {
    }


    @Override
    public Class<Bean> componentType() {
        return Bean.class;
    }

    @Override
    public String toString() {
     return    LiStrUtil.join(",", name, version,  children);
    }
}
