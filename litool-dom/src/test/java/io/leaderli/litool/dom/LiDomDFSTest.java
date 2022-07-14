package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/5
 */
@SuppressWarnings("ALL")
class LiDomDFSTest {

    @Test
    void accept() throws DocumentException {

        DOMElement domRootByPath = LiDomUtil.getDOMRootByPath("/scanner.xml");
        LiDomDFS liDomDFS = new LiDomDFS(new LiDomDFSContext(null), domRootByPath);

        liDomDFS.accept(new LiDomVisitor() {
            @Override
            public void visit(LiDomDFSContext context, DOMElement element, int index) {
                LiPrintUtil.print("begin:", element.getTagName(), index, Lira.of(element.attributes()).map(Node::asXML).getRaw());
            }

            @Override
            public void visit(LiDomDFSContext context, String content) {
                LiPrintUtil.print("body:", content);
            }

            @Override
            public void visit(LiDomDFSContext context) {
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
