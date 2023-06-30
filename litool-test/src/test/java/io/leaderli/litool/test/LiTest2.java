package io.leaderli.litool.test;

import com.google.gson.Gson;
import io.leaderli.litool.core.test.CartesianMap;
import io.leaderli.litool.core.util.ConsoleUtil;
import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.test2.limock.Foo2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2023/6/30 9:59 AM
 */
public class LiTest2 {


    static void init() {

        LiMock.mock(MockMap.class);
        Map<String, String> map = new MockMap<>();

        MethodResultCartesianForParameter v = new MethodResultCartesianForParameter(null, "def");
        v.add(new Object[]{"123"}, null, 123);
        v.add(new Object[]{"124"}, null, 124);
        LiMock.whenArgs(() -> map.get(""), v);

    }

    @MockInit
    @LiTest
    void test() {
        Map<String, String> map = new MockMap<>();
        ConsoleUtil.print_format("123:{1},124:{2},other:{other}", map.get("123"), map.get("124"),
                map.get(RandomUtil.randomString(3)));

    }

    @SuppressWarnings("unchecked")
    static void init2() {

        //language=JSON
        String json = "{\"height\": 188, \"gender\": [true, false], \"age\": [1, null]}";

        Gson gson = new Gson();
        Map<String, Object[]> map = gson.fromJson(json, Map.class);

        CartesianMap<String, Object> cartesianMap = new CartesianMap<>(HashMap::new, map);

        LiMock.mock(Foo2.class);
        Foo2 foo2 = new Foo2();
        LiMock.when(foo2::map, cartesianMap.cartesian());


    }


    @MockInit("init2")
    @LiTest
    void test2() {
        Foo2 foo2 = new Foo2();
        Map<String, Object[]> map = foo2.map();
        ConsoleUtil.print_format("height:{height},gender:{gender},age:{age}", map.get("height"), map.get("gender"), map.get("age"));

    }


}
