package io.leaderli.litool.core.lang;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import io.leaderli.litool.core.type.LiTypeToken;
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

        String json = "{\"name\":\"1\",\"bean\": {\"name\": \"2\"},\"beans\": [{\"name\": \"3\"}]}";
        Map map = gson.fromJson(json, Map.class);
        LiTypeToken<Bean1<Bean1>> parameterized = LiTypeToken.getParameterized(Bean1.class, Bean1.class);

        Lean lean = new Lean();
        Bean1<Bean1> parser = lean.parser(map, parameterized);
        Assertions.assertEquals("3", parser.beans.get(0).name);

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

    private static class Bean1<T> {
        private String name;
        private Bean1 bean;
        private List<T> beans;
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
