package io.leaderli.litool.core.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 8:45 AM
 */
class BitStatusTest {


    @Test
    public void beauty() {


        BitStatus bitStatus = BitStatus.of(TestMask.class);
        Assertions.assertEquals("C", bitStatus.beauty(8));

        Assertions.assertEquals("", bitStatus.beauty(16));
        Assertions.assertEquals("", bitStatus.beauty(4));
        Assertions.assertEquals("A|B|C", bitStatus.beauty(123));
        Assertions.assertEquals("A", bitStatus.beauty(1));
        Assertions.assertEquals("", bitStatus.beauty(0));

        Assertions.assertEquals("A", bitStatus.beauty(0b001 | 0b100));
    }

    @Test
    void test() {

        BitStatus bitStatus = BitStatus.of(TestMask.class);

        Assertions.assertTrue(bitStatus.toString().contains("0001"));

    }

    public interface TestMask {

        int A = 0b1;
        int B = 0b10;
        int C = 0b1000;
    }

}
