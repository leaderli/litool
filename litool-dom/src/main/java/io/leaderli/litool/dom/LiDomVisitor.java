package io.leaderli.litool.dom;

import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.dom4j.dom.DOMElement;

/**
 * @author leaderli
 * @since 2022/7/5
 */
public interface LiDomVisitor {

    /**
     * 开始访问节点，主要用于访问 属性值
     *
     * @param element 节点
     * @param index   节点在父节点的位置
     * @param context  -
     */
    default void visit(LiDomDFSContext<?,?> context, DOMElement element, int index) {

    }
//
//    /**
//     * 访问子节点
//     *
//     * @param child 直接子节点
//     * @param index 子节点的位置
//     */
//    default void visit(DOMElement child, int index) {
//
//    }

    /**
     * 节点的文本
     *
     * @param context  -
     * @param content 文本
     */
    default void visit(LiDomDFSContext<?,?> context,String content) {

    }

    /**
     * 访问节点结束
     * @param context  -
     */
    default void visit(LiDomDFSContext<?,?> context) {

    }
}
