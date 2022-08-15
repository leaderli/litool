package io.leaderli.litool.dom.sax;

import org.dom4j.dom.DOMElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于指定名称 {@link #tag()} 的复杂成员变量
 */
public interface SaxBean extends SaxEventHandler {

    default String id() {
        return "";
    }

    default String tag() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
