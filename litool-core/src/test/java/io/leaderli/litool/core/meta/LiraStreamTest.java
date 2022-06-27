package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class LiraStreamTest {

    @Test
    public void test() {


        Lira<Integer> intStream = Lira.of(1, 2, 3, null, 5);



        intStream.map(i -> {

            if(i==3){
               return null;
            }
            return
            i + "-->";
        }).forEach(System.out::println);


    }

}
