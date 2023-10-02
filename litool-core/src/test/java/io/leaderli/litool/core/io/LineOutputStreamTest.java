package io.leaderli.litool.core.io;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LineOutputStreamTest {

    @Test
    void test() throws IOException {
        LiBox<String> str = LiBox.none();
        LineOutputStream lineOutputStream = new LineOutputStream(str::value);

        IOUtils.copy(ResourceUtil.createContentStream("123"), lineOutputStream);

        Assertions.assertTrue(str.absent());
        lineOutputStream.flush();
        Assertions.assertTrue(str.present());
        str.reset();
        IOUtils.copy(ResourceUtil.createContentStream("123\n"), lineOutputStream);
        Assertions.assertTrue(str.present());
        str.reset();
        lineOutputStream.flush();
        Assertions.assertTrue(str.absent());

    }

}