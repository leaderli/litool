package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.leaderli.litool.core.util.ConsoleUtil.LINE_SEPARATOR;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class ConsoleUtilTest {


    static MyOutputStream out = new MyOutputStream();

    static {
        ConsoleUtil.CONSOLE = new PrintStream(out);
    }


    @Test
    void print() {


        out.reset();
        ConsoleUtil.print0("123", 4565);
        Assertions.assertEquals("4565" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.print("123", "456");
        Assertions.assertEquals("123 456" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.print(1, null, 1, Arrays.asList(1, null));
        Assertions.assertEquals("1  1 [1, null]" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.print(null, null, 1, Arrays.asList(1, null));
        Assertions.assertEquals("  1 [1, null]" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.print_format("a {0}", 2, 3);
        Assertions.assertEquals("a 2" + LINE_SEPARATOR, out.toString());


        out.reset();
        ConsoleUtil.print0("_", "1", "2");
        Assertions.assertEquals("1_2" + LINE_SEPARATOR, out.toString());


        out.reset();
        ConsoleUtil.print(1, 2, 3);
        Assertions.assertEquals("1 2 3" + LINE_SEPARATOR, out.toString());


        out.reset();
        ConsoleUtil.print(Arrays.asList(1, 2, 3));
        Assertions.assertEquals("1 2 3" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.print(Arrays.asList(1, 2, 3).iterator());
        Assertions.assertEquals("1 2 3" + LINE_SEPARATOR, out.toString());

    }

    @Test
    void println() {

        out.reset();
        ConsoleUtil.CONSOLE.println("123");
        Assertions.assertEquals("123" + LINE_SEPARATOR, out.toString());
        out.reset();
        ConsoleUtil.println(1, 2, 3);
        Assertions.assertEquals("1" + LINE_SEPARATOR + "2" + LINE_SEPARATOR + "3" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.println(Arrays.asList(1, 2, 3));
        Assertions.assertEquals("1" + LINE_SEPARATOR + "2" + LINE_SEPARATOR + "3" + LINE_SEPARATOR, out.toString());

        out.reset();
        ConsoleUtil.println(Arrays.asList(1, 2, 3).iterator());
        Assertions.assertEquals("1" + LINE_SEPARATOR + "2" + LINE_SEPARATOR + "3" + LINE_SEPARATOR, out.toString());
    }

    @Test
    void line() {
        out.reset();
        ConsoleUtil.line();
        Assertions.assertTrue(out.toString().contains("-"));

    }


    @Test
    void print0() {


        out.reset();
        ConsoleUtil.print0("*", 2, 3);
        Assertions.assertEquals("2*3" + LINE_SEPARATOR, out.toString());
    }

    @Test
    void print_format() {

        out.reset();
        ConsoleUtil.print_format("a {0}", 2, 3);
        Assertions.assertEquals("a 2" + LINE_SEPARATOR, out.toString());

    }


    private static class MyOutputStream extends OutputStream {
        private final List<Byte> bytes = new ArrayList<>();

        @Override
        public void write(int b) {
            bytes.add((byte) b);
        }

        public void reset() {
            bytes.clear();
        }

        @Override
        public String toString() {

            byte[] bytes = new byte[this.bytes.size()];
            for (int i = 0; i < this.bytes.size(); i++) {
                bytes[i] = this.bytes.get(i);
            }
            return new String(bytes);
        }
    }
}
