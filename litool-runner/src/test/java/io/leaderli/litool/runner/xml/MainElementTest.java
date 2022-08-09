package io.leaderli.litool.runner.xml;

import com.google.gson.Gson;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.adapter.RunnerGson;
import io.leaderli.litool.runner.executor.MainElementExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class MainElementTest {

    @Test
    void test() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);

        MainElement main = dfs.parse("main.xml");


//        GsonUtil.print(main);


        Gson gson = RunnerGson.GSON_BUILDER.setPrettyPrinting().create();

//        System.out.println(gson.toJson(main));

        Map<String, String> request = new HashMap<>();
        request.put("CHANNEL", "IVR");


        Context context = new Context(request);
        MainElementExecutor executor = main.executor();
        executor.visit(context);

        Assertions.assertEquals("IVR", context.getRequest("CHANNEL"));


    }

}
