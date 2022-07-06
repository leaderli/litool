package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import org.dom4j.dom.DOMElement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/5
 */
public class LiDomDFS {

    private final DOMElement element;
    /**
     * {@link #element} 在 父节点的位置，-1 表示未知，或无父节点
     */
    private final int index;

    public LiDomDFS(DOMElement element, int index) {
        Objects.requireNonNull(element, " element is null ");
        this.element = element;
        this.index = index;
    }

    public LiDomDFS(DOMElement element) {
        this(element, -1);
    }

    public LiDomDFS(String content) {
        this(RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByString, content));
    }

    public LiDomDFS(InputStream inputStream) {
        this(RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByInputStream, inputStream));
    }

    public void accept(LiDomVisitor visitor) {

        Map<String, String> attributes = new HashMap<>();
        visitor.visit(this.element, index);
        List<DOMElement> children = LiDomUtil.selectNodes(this.element);
        for (int i = 0; i < children.size(); i++) {


            DOMElement child = children.get(i);
            new LiDomDFS(child, i).accept(visitor);
//            visitor.visit(child, i);
        }
        visitor.visit(this.element.getTextTrim());
        visitor.visit();
    }
}
