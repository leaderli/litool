package io.leaderli.litool.dom;

import io.leaderli.litool.core.text.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LiDomUtilTest {


    @Test
    void createElement() {

        Assertions.assertEquals("<hello/>", LiDomUtil.createElement("hello").asXML());
        Assertions.assertEquals("<hello><fuck/></hello>", LiDomUtil.createElement("hello", "fuck").getParent().asXML());


    }

    @Test
    void write() throws DocumentException {
        String xml = "<test>\n" +
                "    <t1>1</t1>\n" +
                "    <t2>1</t2>\n" +
                "    <t3>\n" +
                "        <tt3>tt3</tt3>\n" +
                "    </t3>\n" +
                "</test>";

        DOMElement root = LiDomUtil.getDOMRootByString(xml);

        Assertions.assertEquals(LiDomUtil.pretty(root), StringUtils.read(LiDomUtil.write(root)));


    }

    @Test
    void test() throws DocumentException {

        DOMElement dom = LiDomUtil.getDOMRootByPath("/test1.xml");

        assertEquals(dom.asXML(), "<test>\n" +
                "    <t1>1</t1>\n" +
                "    <t2>1</t2>\n" +
                "    <t3>\n" +
                "        <tt3>tt3</tt3>\n" +
                "    </t3>\n" +
                "</test>");


        MyDom myDom = new MyDom(dom);
        myDom.accept(new Visitor() {

        });

    }

    @Test
    void selectSingleNode() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/test1.xml");

        assertEquals("<t1>1</t1>", LiDomUtil.selectSingleNode(dom, "t1").asXML());
        assertEquals("<t1>1</t1>", LiDomUtil.selectNodes(dom, "t1").get(0).asXML());
        assertEquals("<t1>1</t1>", LiDomUtil.selectNodes(dom).get(0).asXML());

        assertEquals(0, LiDomUtil.selectNodes(dom, "t12").size());
        dom = LiDomUtil.getDOMRootByInputStream(LiDomUtil.class.getResourceAsStream("/test1.xml"));
        Assertions.assertNotNull(dom);
        dom = LiDomUtil.getDOMRootByString("<root></root>");
        Assertions.assertNotNull(dom);

    }

    @Test
    void pretty() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/test1.xml");
        assertEquals("\n" +
                "<test> \n" +
                "  <t1>1</t1>  \n" +
                "  <t2>1</t2>  \n" +
                "  <t3> \n" +
                "    <tt3>tt3</tt3> \n" +
                "  </t3> \n" +
                "</test>", LiDomUtil.pretty(dom));

//        LiDomUtil.prettyPrint(dom);
    }


    @Test
    void testCdata() throws DocumentException {
        DOMElement dom = LiDomUtil.getDOMRootByPath("/cdata.xml");

        Assertions.assertEquals("<t1>1</t1>", dom.getTextTrim());

    }

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
}
