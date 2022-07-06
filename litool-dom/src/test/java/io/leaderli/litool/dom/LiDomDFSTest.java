package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/5
 */
class LiDomDFSTest {

    @Test
    void accept() throws DocumentException {

        LiDomDFS liDomDFS = new LiDomDFS(new LiDomDFSContext(null), LiDomUtil.getDOMRootByPath("/scanner.xml"));

        liDomDFS.accept(new LiDomVisitor() {
            @Override
            public void visit(LiDomDFSContext context, DOMElement element, int index) {
                LiPrintUtil.print("begin:", element.getTagName(), index, Lira.of(element.attributes()).map(Node::asXML).getRaw());
            }

//            @Override
//            public void visit(DOMElement child, int index) {
////                System.out.println(child.asXML());
//                LiPrintUtil.print("child:", index, child.asXML());
//            }

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
        public final Bean bean;

        public Bean(Map<String, Object> context) {
            this.name = (String) context.get("@name");
            this.version = (String) context.get("@version");
            this.bean = (Bean) context.get("#bean");
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", version='" + version + '\'' +
                    ", bean=" + bean +
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

            context.objectMap.put("$class", Bean.class);
            element.attributeIterator().forEachRemaining(a -> context.objectMap.put("@" + a.getName(), a.getStringValue()));
        }

        @Override
        public void visit(LiDomDFSContext context, String content) {
        }

        @Override
        public void visit(LiDomDFSContext context) {

            Class type = (Class) context.objectMap.get("$class");

            try {
                Bean instance = (Bean) type.getConstructor(Map.class).newInstance(context.objectMap);

                if (context.parent != null) {
                    context.parent.objectMap.put("#bean", instance);
                } else {

                    LiPrintUtil.print(instance);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
