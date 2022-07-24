package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public interface NoAttributeSaxBean extends SaxBean {
    @Override
    default void attribute(AttributeEvent attributeEvent) {
    }
}
