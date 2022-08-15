package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.event.ILiEventListener;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.dom.LiDomUtil;
import io.leaderli.litool.dom.XmlMapConvert;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.adapter.RunnerGson;
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
        //noinspection Convert2Lambda
        ILiEventListener<UnitErrorEvent> listener = new ILiEventListener<UnitErrorEvent>() {

            @Override
            public void listen(UnitErrorEvent source) {

                LiTuple2<String, Throwable> tuple = source.getSource().get();
//                tuple._2.printStackTrace();
//                System.out.println(tuple);
            }
        };
//        System.out.println(listener.componentType());
        context.registerListener(listener);
        mainElement.executor().visit(context);
        CharSequence skill = context.getResponse("skill");

        Assertions.assertEquals("003", skill);

    }

    @Test
    void test2() throws DocumentException {
        DOMElement mainElement = LiDomUtil.getDOMRootByPath("unit_error.xml");

        Map<String, Object> read = XmlMapConvert.read(mainElement);
        System.out.println(RunnerGson.GSON.toJson(read));

    }
}
