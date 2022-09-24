package io.leaderli.litool.core.lang;

import com.google.gson.Gson;
import io.leaderli.litool.core.lang.lean.Lean;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

/**
 * @author leaderli
 * @since 2022/9/24 9:41 AM
 */
class LeanTest {

    @SuppressWarnings("rawtypes")
    @Test
    void test() {
        Gson gson = new Gson();

        Map map = gson.fromJson("{\"name\":\"1\",\"bean\": {\"name\": \"2\"},\"beans\": [{\"name\": \"3\"}]}", Map.class);
        Bean1 parser = Lean.parser(map, Bean1.class);

        print(gson.toJson(parser));
//        System.out.println(parser.beans.get(0).name);
    }

    @Test
    void test2() throws NoSuchFieldException {
        System.out.println(Bean1.class.getDeclaredField("beans").getGenericType());


    }

    private static class Bean1 {
        private String name;
        private Bean1 bean;
        private List<Bean1> beans;
    }
}
