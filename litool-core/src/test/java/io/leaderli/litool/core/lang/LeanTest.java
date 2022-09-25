package io.leaderli.litool.core.lang;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

/**
 * @author leaderli
 * @since 2022/9/24 9:41 AM
 */
@SuppressWarnings("rawtypes")
class LeanTest {


    @Test
    void test() {
        Gson gson = new Gson();

        Map map = gson.fromJson("{\"name\":\"1\",\"bean\": {\"name\": \"2\"},\"beans\": [{\"name\": \"3\"}]}", Map.class);
        Lean lean = new Lean();
        Bean1 parser = lean.parser(map, Bean1.class);

        print(gson.toJson(parser));
//        System.out.println(parser.beans.get(0).name);
        Assertions.assertNotNull(parser);
    }

    @Test
    void test2() {

        Gson gson = new Gson();

        Map map = gson.fromJson("{\"name\":\"1\",\"bean3\": {\"name\": \"2\",\"bean2\": {\"name\": \"3\"}}}", Map.class);
        Lean lean = new Lean();
        Bean2 parser = lean.parser(map, Bean2.class);

        print(gson.toJson(parser));
        Assertions.assertNotNull(parser);

    }

    private static class Bean1 {
        private String name;
        private Bean1 bean;
        private List<Bean1> beans;
    }

    private static class Bean2 {
        private Bean3 bean3;
        private String name;

    }

    private static class Bean3 {
        private String name;
        private Bean2 bean2;
    }
}
