package io.leaderli.litool.dom;

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
        return "Bean{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", children=" + children +
                '}';
    }
}
