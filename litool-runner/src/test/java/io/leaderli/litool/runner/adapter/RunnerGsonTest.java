package io.leaderli.litool.runner.adapter;

import io.leaderli.litool.dom.parser.LiDomDFSContext;
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
        LiDomDFSContext<MainElement> dfs = new LiDomDFSContext<>(MainElement.class);
        MainElement main = dfs.parse("main.xml");

        String json = RunnerGson.GSON.toJson(main);
        System.out.println(json);

        main = RunnerGson.GSON.fromJson(json, MainElement.class);

        GsonUtil.print(main);


    }
}
