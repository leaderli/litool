package io.leaderli.litool.runner;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.util.ConsoleUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.event.EchoListener;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RunnerTest {

    static MainElement element;
    static MainElementExecutor executor;

    @BeforeAll
    static void beforeAll() {
        SaxEventInterceptor<MainElement> interceptor = new SaxEventInterceptor<>(MainElement.class);
        element = interceptor.parse("runner_test.xml");

        ConsoleUtil.println(interceptor.getParseErrorMsgs());
        assertEquals(0, interceptor.getParseErrorMsgs().size());

        executor = new MainElementExecutor(element);
    }

    /**
     * 百夫长黑金卡
     */
    @Test
    void test1() {
        Map<String, String> request = new HashMap<>();
        request.put("bfzType", "1");

        Context context = new Context(request);

        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "001"));
    }

    /**
     * 百夫长白金卡
     */
    @Test
    void test2() {
        Map<String, String> request = new HashMap<>();
        request.put("bfzType", "2");

        Context context = new Context(request);
        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "002"));
    }

    /**
     * 夜间
     */
    @Test
    void test3() {
        Map<String, String> request = new HashMap<>();
        request.put("bfzType", "0");
        request.put("switch86", "0");
        request.put("_env", "local_test");
        request.put("_testTime", "2300");

//        System.out.println(new TimeInstruct().invoke("HHmm", "2300", "local_test"));
//        System.out.println(new BetweenTimeInstruct().invoke("2230","0700","2300"));
        Context context = new Context(request);
        context.registerListener(new EchoListener() {
            @Override
            public void listen(EchoEvent source) {
                System.out.println(source.getSource().get()._2);
            }
        });
        System.out.println(context.origin_request_or_response);
        executor.visit(context);
        System.out.println(context.origin_request_or_response);

        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "sequence_a"));
    }

    /**
     * 英语语种 0900-1730
     */
    @Test
    void test4() {
        Map<String, String> request = new HashMap<>();
        request.put("language", "1");
        request.put("_env", "local_test");
        request.put("_testTime", "1200");

        Context context = new Context(request);
        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "031"));
    }

    /**
     * 分行内呼 0800-1900工作时间 2300-0700夜间
     */
    @Test
    void test5() {
        Map<String, String> request = new HashMap<>();
        request.put("isBankInline", "1");
        request.put("_env", "local_test");
        request.put("_testTime", "1200");

        Context context = new Context(request);
        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "042"));
    }

    /**
     * 单位公务卡 0800-1900工作时间 2300-0700夜间
     */
    @Test
    void test6() {
        Map<String, String> request = new HashMap<>();
        request.put("phoneType", "201");
        request.put("_env", "local_test");
        request.put("_testTime", "1200");

        Context context = new Context(request);
        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "080"));

        request.put("_testTime", "0000");
        context = new Context(request);
        executor.visit(context);
        skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "030"));
    }

    /**
     * 超高端白金卡
     */
    @Test
    void test7() {
        Map<String, String> request = new HashMap<>();
        request.put("custType", "3");
        request.put("_env", "local_test");
        request.put("_testTime", "1200");

        Context context = new Context(request);
        executor.visit(context);
        CharSequence skill = context.getResponse("skill");
        assertTrue(StringUtils.equals(skill, "053"));
    }
}
