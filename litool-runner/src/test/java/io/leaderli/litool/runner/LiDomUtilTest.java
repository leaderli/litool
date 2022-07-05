package io.leaderli.litool.runner;

import io.leaderli.litool.dom.LiDomUtil;
import org.dom4j.DocumentException;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiDomUtilTest {


    private static class Visitor {
        void visit(DOMElement dom) {

        }

        void visit(String text) {

        }
    }

    private static class MyDom {

        private final DOMElement dom;

        public MyDom(DOMElement dom) {
            this.dom = dom;
        }

        void accept(Visitor visitor) {

            visitor.visit(dom);
            LiDomUtil.selectNodes(dom).forEach(child -> new MyDom(child).accept(visitor));
            visitor.visit(dom.getTextTrim());
        }
    }

    @Test
    public void test() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/request.xml");
        System.out.println(dom.attributeValue("abc"));
        System.out.println(dom.attributeValue("abc", ""));

        assertEquals(dom.asXML(), "<test>\n" +
                "    <t1>1</t1>\n" +
                "    <t2>1</t2>\n" +
                "    <t3>\n" +
                "        <tt3>tt3</tt3>\n" +
                "    </t3>\n" +
                "</test>");


        MyDom myDom = new MyDom(dom);
        myDom.accept(new Visitor() {
            @Override
            void visit(DOMElement dom) {
            }

            @Override
            void visit(String text) {
            }
        });

    }

    @Test
    public void selectSingleNode() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/request.xml");

        assertEquals("<t1>1</t1>", LiDomUtil.selectSingleNode(dom, "t1").asXML());
        assertEquals("<t1>1</t1>", LiDomUtil.selectNodes(dom, "t1").get(0).asXML());
        assertEquals("<t1>1</t1>", LiDomUtil.selectNodes(dom).get(0).asXML());

        assertEquals(0, LiDomUtil.selectNodes(dom, "t12").size());
        dom = LiDomUtil.getDOMRootByInputStream(LiDomUtil.class.getResourceAsStream("/request.xml"));
        Assertions.assertNotNull(dom);
        dom = LiDomUtil.getDOMRootByString("<root></root>");
        Assertions.assertNotNull(dom);

    }

    @Test
    public void pretty() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/request.xml");
        assertEquals("\n" +
                "<test> \n" +
                "  <t1>1</t1>  \n" +
                "  <t2>1</t2>  \n" +
                "  <t3> \n" +
                "    <tt3>tt3</tt3> \n" +
                "  </t3> \n" +
                "</test>", LiDomUtil.pretty(dom));

        LiDomUtil.prettyPrint(dom);
    }
}
