package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.LiPrintUtil;
import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/5
 */
class LiDomDFSTest {

    @Test
    void accept() throws DocumentException {

        LiDomDFS liDomDFS = new LiDomDFS(LiDomUtil.getDOMRootByPath("/scanner.xml"));

        liDomDFS.accept(new LiDomVisitor() {
            @Override
            public void visit(DOMElement element, int index) {
                LiPrintUtil.print("begin:", element.getTagName(), index, Lira.of(element.attributes()).map(Node::asXML).getRaw());
            }

//            @Override
//            public void visit(DOMElement child, int index) {
////                System.out.println(child.asXML());
//                LiPrintUtil.print("child:", index, child.asXML());
//            }

            @Override
            public void visit(String content) {
                LiPrintUtil.print("body:", content);
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
