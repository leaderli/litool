package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.dom4j.dom.DOMElement;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/5
 */
public class LiDomDFS<Child, Parent> {

    private final DOMElement element;
    private final LiDomDFSContext<Child, Parent> context;
    /**
     * {@link #element} 在 父节点的位置，-1 表示未知，或无父节点
     */
    private final int index;

    public LiDomDFS(LiDomDFSContext<Parent, ?> context, DOMElement element, int index) {
        Objects.requireNonNull(element, " element is null ");
        this.context = new LiDomDFSContext<>(context);
        this.element = element;
        this.index = index;
    }

    public LiDomDFS(LiDomDFSContext<Parent, ?> context, DOMElement element) {
        this(context, element, -1);
    }

    public LiDomDFS(LiDomDFSContext<Parent, ?> context, String content) {
        this(context, RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByString, content));
    }

    public LiDomDFS(LiDomDFSContext<Parent, ?> context, InputStream inputStream) {
        this(context, RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByInputStream, inputStream));
    }

    public void accept(LiDomVisitor visitor) {

        visitor.visit(this.context, this.element, index);
        List<DOMElement> children = LiDomUtil.selectNodes(this.element);
        for (int i = 0; i < children.size(); i++) {


            DOMElement child = children.get(i);
            new LiDomDFS<>(this.context, child, i).accept(visitor);
//            visitor.visit(child, i);
        }
        visitor.visit(this.context, this.element.getTextTrim());
        visitor.visit(this.context);
    }

    public Child getBean() {
        return this.context.bean;
    }
}