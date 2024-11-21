package io.leaderli.litool.core.io;

import io.leaderli.litool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

class TailerTest {

    @Test
    void test() {

        Tailer tailer = new Tailer(new File("123.txt"), new TailerListener() {

            @Override
            public void fileNotFound(File file) {
                FileUtil.createNewFile(file);
            }

            @Override
            public void delay(Tailer tailer) {
                System.out.println(tailer);
                if (RandomUtil.probability(10)) {
                    tailer.stop();
                }
            }
        });
//        ThreadUtil.start(tailer, false);

    }

}
