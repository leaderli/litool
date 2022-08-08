package io.leaderli.litool.runner.xml;

import com.google.gson.Gson;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.json.GsonUtil;
import io.leaderli.litool.runner.adapter.RunnerGson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class MainElementTest {

    @Test
    void test() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);

        MainElement main = dfs.parse("main.xml");


        System.out.println(dfs.getParseErrorMsgs());
        Assertions.assertEquals(1, dfs.getParseErrorMsgs().size());

//        GsonUtil.print(main);


        Gson gson = RunnerGson.GSON_BUILDER.setPrettyPrinting().create();

        System.out.println(gson.toJson(main));


    }

}