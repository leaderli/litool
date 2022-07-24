package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public interface NoBodySaxBean extends SaxBean {
    @Override
    default void body(BodyEvent bodyEvent) {
    }
}
