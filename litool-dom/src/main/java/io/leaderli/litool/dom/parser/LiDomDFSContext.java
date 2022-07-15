package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.util.LiIoUtil;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.core.util.LiStrUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxEvent;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext {


    public static <T extends SaxBean> T parse(String path, Class<T> cls) {


        return RuntimeExceptionTransfer.get(() -> {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DFSLocatorHandler<T> dh = new DFSLocatorHandler<>();
            saxParser.parse(LiIoUtil.getResourceAsStream(path), dh);

            Map<Integer, String> map = LiIoUtil.lineStrOfResourcesFile(path);
            for (SaxEvent saxEvent : dh.getSaxEventList()) {
                LiPrintUtil.print(LiStrUtil.rjust(saxEvent.toString(), 30), map.get(saxEvent.locator.getLineNumber()));
            }
            return null;
        });
    }
}
