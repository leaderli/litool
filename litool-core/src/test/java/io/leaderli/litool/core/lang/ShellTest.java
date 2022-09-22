package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author leaderli
 * @since 2022/9/22 9:24 AM
 */
class ShellTest {

    @Test
    void test() {

        System.out.println(System.getProperty("user.dir"));


        new Shell().command("sh", "-c", "echo $PWD");

        String a;

    }

    @Test
    void test2() throws IOException {
        System.out.println((int) '\n');
        InputStream inputStream = new InputStream() {
            @Override
            public int read() {
                if (RandomUtil.nextInt(50) == 10) {
                    return 10;
                }
                return RandomUtil.nextInt('0', 'z');
            }
        };


        System.out.println(new BufferedReader(new InputStreamReader(inputStream)).readLine());

    }
}
