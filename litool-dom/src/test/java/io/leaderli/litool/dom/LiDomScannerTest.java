package io.leaderli.litool.dom;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/5
 */
class LiDomScannerTest {

    @Test
    void accept() throws DocumentException {

        LiDomScanner liDomScanner = new LiDomScanner(LiDomUtil.getDOMRootByPath("/scanner.xml"));

        liDomScanner.accept(new LiDomVisitor() {
            @Override
            public void visit(Map<String, String> attributes) {
                System.out.println(attributes);
            }

            @Override
            public void visit(DOMElement child, int index) {
                System.out.println(child.asXML());
            }

            @Override
            public void visit(String content) {
                System.out.println(content);
            }

            @Override
            public void visit() {
                System.out.println("end");
            }
        });

    }

    @Test
    void test2() throws DocumentException {

        DOMElement scanner = LiDomUtil.getDOMRootByPath("/scanner.xml");

        scanner.accept(
                new VisitorSupport() {
                    @Override
                    public void visit(Attribute node) {
                        System.out.println(node.getName() + " " + node.getValue());

                    }


                    @Override
                    public void visit(Element node) {
                        System.out.println("child " + node.asXML());
                    }
                }
        );

    }
}
