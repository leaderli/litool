package io.leaderli.litool.dom;

import org.dom4j.dom.DOMElement;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/5
 */
public interface LiDomVisitor {

    /**
     * 开始访问节点，主要用于访问 属性值
     *
     * @param attributes 属性
     */
    default void visit(Map<String, String> attributes) {

    }

    /**
     * 访问子节点
     *
     * @param child 直接子节点
     * @param index 子节点的位置
     */
    default void visit(DOMElement child, int index) {

    }

    /**
     * 节点的文本
     *
     * @param content 文本
     */
    default void visit(String content) {

    }

    /**
     * 访问节点结束
     */
    default void visit() {

    }
}
