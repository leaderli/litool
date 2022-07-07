package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

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

    public static class Bean {
        public final String name;
        public final String version;
        public final Object[] children;

        public Bean(LiDomDFSContext context) {
            this.name = context.attributes.get("name");
            this.version = context.attributes.get("version");
            this.children = context.children.toArray();
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", children=" + Arrays.toString(children) +
                    '}';
        }
    }

    @Test
    void bean() throws DocumentException {
        LiDomDFS liDomDFS = new LiDomDFS(new LiDomDFSContext(null), LiDomUtil.getDOMRootByPath("/bean.xml"));


        liDomDFS.accept(new MyLiDomVisitor());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static class MyLiDomVisitor implements LiDomVisitor {


        public MyLiDomVisitor() {
        }

        @Override
        public void visit(LiDomDFSContext context, DOMElement element, int index) {


            context.tag.value(Bean.class);
            element.attributeIterator()
                    .forEachRemaining(a ->
                            context.attributes.put(a.getName(), a.getStringValue())
                    );
        }

        @Override
        public void visit(LiDomDFSContext context, String content) {
        }

        @Override
        public void visit(LiDomDFSContext context) {

            Class type = context.tag.value();

            try {
                Bean instance = (Bean) type.getConstructor(LiDomDFSContext.class).newInstance(context);

                if (context.parent != null) {
                    context.parent.children.add(instance);
                } else {

                    LiPrintUtil.print(instance);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
