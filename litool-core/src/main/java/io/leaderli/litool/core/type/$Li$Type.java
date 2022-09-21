package io.leaderli.litool.core.type;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/9/20
 */
@SuppressWarnings("java:S101")
public class $Li$Type {
    public static void main(String[] args) throws NoSuchFieldException {

        Type a = Test.class.getDeclaredField("a").getGenericType();
        System.out.println(a.getClass());
    }
}

class Test<A> {
    A a;
}
