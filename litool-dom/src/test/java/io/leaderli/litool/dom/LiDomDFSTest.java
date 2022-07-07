package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lino;
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


    @Test
    void bean() throws DocumentException {
        LiDomDFS<?, ?> liDomDFS = new LiDomDFS<>(null, LiDomUtil.getDOMRootByPath("/bean.xml"));

        liDomDFS.accept(new MyLiDomVisitor());

        System.out.println(liDomDFS.getBean());
    }

    private static class MyLiDomVisitor implements LiDomVisitor {


        public MyLiDomVisitor() {
        }

        @Override
        public void visit(LiDomDFSContext<?, ?> context, DOMElement element, int index) {


            context.type = LiDomTagBean.getTagBeanClass(context.type, element.getTagName());
            element.attributeIterator()
                    .forEachRemaining(a ->
                            context.attributes.put(a.getName(), a.getStringValue())
                    );
        }

        @Override
        public void visit(LiDomDFSContext<?, ?> context, String content) {
            context.content = content;
        }

        @Override
        public void visit(LiDomDFSContext<?, ?> context) {
            context.newInstance();
        }
    }
}
