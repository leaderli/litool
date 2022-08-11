package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.dom.LiDomUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.dom.sax.SaxBean;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.dom.DOMElement;

/**
 * @author leaderli
 * @since 2022/8/11 5:38 PM
 */
public class RunnerSaxEventInterceptorUtil {

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

    public static String generateID(String xml) throws DocumentException {

        DOMElement root = LiDomUtil.getDOMRootByString(xml);
        VisitorSupport support = new MyVisitorSupport();
        support.visit(root);
        return LiDomUtil.pretty(root);

    }


    public static <T extends SaxBean> T parse(String path, Class<T> entryClass) throws DocumentException {
        DOMElement main = LiDomUtil.getDOMRootByPath(path);
        SaxEventInterceptor<T> dfs = new SaxEventInterceptor<>(entryClass);

        String xml = generateID(main.asXML());
        return dfs.parse(ResourceUtil.createContentStream(xml));
    }
}
