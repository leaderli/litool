package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.event.ILiEventListener;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.dom.LiDomUtil;
import io.leaderli.litool.dom.XmlMapConvert;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextInfo;
import io.leaderli.litool.runner.event.BeginEvent;
import io.leaderli.litool.runner.event.EndEvent;
import io.leaderli.litool.runner.event.UnitErrorEvent;
import io.leaderli.litool.runner.xml.MainElement;
import org.dom4j.DocumentException;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/14
 */
class UnitElementTest {

    @Test
    void test() {
        SaxEventInterceptor<MainElement> interceptor = new SaxEventInterceptor<>(MainElement.class);
        MainElement mainElement = interceptor.parse("unit_error.xml");

        Map<String, String> request = new HashMap<>();
        request.put("bfzType", "1");

        Context context = new Context(request);
        ILiEventListener<UnitErrorEvent> listener = new UnitErrorEventILiEventListener();
        context.registerListener(listener);
        mainElement.executor().visit(context);
        CharSequence skill = context.getResponse("skill");

        Assertions.assertEquals("003", skill);

    }

    @Test
    void test2() throws DocumentException {
        DOMElement mainElement = LiDomUtil.getDOMRootByPath("unit_error.xml");

        Map<String, Object> read = XmlMapConvert.read(mainElement);

        String write = XmlMapConvert.write(read).asXML();

        read = XmlMapConvert.read(LiDomUtil.getDOMRootByString(write));

        Assertions.assertEquals(write, XmlMapConvert.write(read).asXML());

    }


    //    @Test
    void unit_long() {

        SaxEventInterceptor<MainElement> interceptor = new SaxEventInterceptor<>(MainElement.class);
        MainElement mainElement = interceptor.parse("unit_long.xml");

        Map<String, String> request = new HashMap<>();
        request.put("bfzType", "1");

        ContextInfo contextInfo = new ContextInfo();
        Context context = new Context(request);
        context.registerListener(new BeginEventILiEventListener(contextInfo));
        context.registerListener(new UnitErrorEventILiEventListener());
        context.registerListener(new EndEventILiEventListener(contextInfo));

        mainElement.executor().visit(context);
//        context.visit(mainElement.executor());

//        GsonUtil.print(context.origin_request_or_response);
//        GsonUtil.print(contextInfo);


    }

    private static class UnitErrorEventILiEventListener implements ILiEventListener<UnitErrorEvent> {

        @Override
        public void listen(UnitErrorEvent event) {

            LiTuple2<String, Throwable> tuple = event.getSource().get();
//                tuple._2.printStackTrace();
//                System.out.println(tuple);
        }
    }

    private static class BeginEventILiEventListener implements ILiEventListener<BeginEvent> {
        private final ContextInfo info;

        public BeginEventILiEventListener(ContextInfo contextInfo) {
            this.info = contextInfo;
        }

        @Override
        public void listen(BeginEvent event) {

            info.setElapse(System.currentTimeMillis());
        }
    }

    private static class EndEventILiEventListener implements ILiEventListener<EndEvent> {
        private final ContextInfo info;

        public EndEventILiEventListener(ContextInfo contextInfo) {
            this.info = contextInfo;
        }

        @Override
        public void listen(EndEvent event) {
            info.setElapse(System.currentTimeMillis() - info.getElapse());
        }
    }
}
