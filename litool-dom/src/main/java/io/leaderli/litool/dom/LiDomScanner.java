package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.meta.Lira;
import org.dom4j.Node;
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
public class LiDomScanner {

    private final DOMElement element;

    public LiDomScanner(DOMElement element) {
        Objects.requireNonNull(element, " element is null ");
        this.element = element;
    }

    public LiDomScanner(String content) {
        this(RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByString, content));
    }

    public LiDomScanner(InputStream inputStream) {
        this(RuntimeExceptionTransfer.apply(LiDomUtil::getDOMRootByInputStream, inputStream));
    }

    public void accept(LiDomVisitor visitor) {

        Map<String, String> attributes = new HashMap<>();
        Map<String, String> map = Lira.of(this.element.attributeIterator()).toMap(Node::getName, Node::getStringValue);
        visitor.visit(map);
        List<DOMElement> children = LiDomUtil.selectNodes(this.element);
        for (int i = 0; i < children.size(); i++) {
            visitor.visit(children.get(i),i);
        }
        visitor.visit(this.element.getText().trim());
        visitor.visit();
    }
}
