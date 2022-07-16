package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class LiLinkTest {


    @Test
    public void test() {


        System.out.println(LiLink.of().present());
        System.out.println(LiLink.of(1)
                .then(Object::toString)
                .error(() -> System.out.println("------>1"))
                .error(() -> System.out.println("------>2"))
                .then(v -> {
                    System.out.println("then 2");
                    return true;
                })
                .then(i -> i > 10)
                .error(i -> System.out.println("error:" + i))

                .present()
        );

        System.out.println("------------------------------");
        System.out.println(LiLink.of(null).then(Object::toString).error(() -> System.out.println("------>1")).error(() -> System.out.println("------>2")).then(v -> {
            System.out.println("then 2");
            return true;
        }).present());
    }
}
