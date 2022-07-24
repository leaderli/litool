package io.leaderli.litool.runner.adapter;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.json.GsonUtil;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class RunnerGsonTest {

    @Test
    void test() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement main = dfs.parse("main.xml");

        String json = RunnerGson.GSON.toJson(main);
        System.out.println(json);

        main = RunnerGson.GSON.fromJson(json, MainElement.class);

        GsonUtil.print(main);


    }
}
