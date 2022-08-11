package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.dom.LiDomUtil;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.dom.DOMElement;

import java.io.InputStream;

/**
 * @author leaderli
 * @since 2022/8/11 5:38 PM
 */
public class GenerateXmlID {

    public static String generate(String xml) throws DocumentException {

        DOMElement root = LiDomUtil.getDOMRootByString(xml);
        VisitorSupport support = new MyVisitorSupport();
        support.visit(root);
        return LiDomUtil.pretty(root);

    }

    public static InputStream generateByPath(String path) throws DocumentException {

        DOMElement main = LiDomUtil.getDOMRootByPath(path);
        return ResourceUtil.createContentStream(generate(main.asXML()));
    }

    private static class MyVisitorSupport extends VisitorSupport {
        private int id;

        public MyVisitorSupport() {
        }

        @Override
        public void visit(Element node) {
            node.addAttribute("id", "" + id++);
            for (Element element : node.elements()) {
                visit(element);
            }
        }
    }
}
