package io.leaderli.litool.runner;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.util.ConsoleUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import io.leaderli.litool.runner.xml.MainElement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}
