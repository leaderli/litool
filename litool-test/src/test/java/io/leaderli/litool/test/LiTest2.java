package io.leaderli.litool.test;

import io.leaderli.litool.core.util.ConsoleUtil;
import io.leaderli.litool.core.util.RandomUtil;

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
}
