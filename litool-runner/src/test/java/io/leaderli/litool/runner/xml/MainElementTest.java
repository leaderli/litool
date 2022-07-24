package io.leaderli.litool.runner.xml;

import com.google.gson.Gson;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
import io.leaderli.litool.runner.adapter.RunnerGson;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class MainElementTest {

    @Test
    void test() {
        LiDomDFSContext<MainElement> dfs = new LiDomDFSContext<>(MainElement.class);

        MainElement main = dfs.parse("main.xml");

//        GsonUtil.print(main);


        Gson gson = RunnerGson.GSON_BUILDER.setPrettyPrinting().create();

        System.out.println(gson.toJson(main));


    }

}
