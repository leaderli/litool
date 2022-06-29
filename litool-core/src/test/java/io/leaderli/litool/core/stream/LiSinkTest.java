package io.leaderli.litool.core.stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;


public class LiSinkTest {

    @Test
    public void test() {

        LiSink<String, Boolean> prev = null;
        for (int i = 0; i < 1000; i++) {

            prev = new LiSink<String, Boolean>(prev) {
                @Override
                public Boolean apply(String request, Boolean last) {


                    if (this.nextSink.present()) {

                        return this.nextSink.get().apply(request, last);
                    }
                    return false;
                }
            };
        }



        assertFalse( prev.request("hello"));


    }


}
