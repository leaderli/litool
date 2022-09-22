package io.leaderli.litool.core.io;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author leaderli
 * @since 2022/9/22 8:14 PM
 */
class MockInfiniteInputStreamTest {

    @Test
    void read() throws IOException {


        MockInfiniteInputStream mockInputStream = new MockInfiniteInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mockInputStream));

        System.out.println("---");
//        System.out.println(bufferedReader.readLine());
    }

}
