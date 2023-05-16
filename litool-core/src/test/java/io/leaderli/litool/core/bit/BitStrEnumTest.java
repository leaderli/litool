package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.util.ConsoleUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/7/21
 */
class BitPositionEnumTest {
    @Test
    void match() {

        assert BitPositionEnum.valueOf("B1").value == 1;
        assert BitPositionEnum.valueOf("B32").value == Integer.MIN_VALUE;

        Assertions.assertEquals("0001", StringUtils.ljust(Integer.toBinaryString(BitPositionEnum.B1.value), 4, '0'));
        Assertions.assertEquals("00010000", StringUtils.ljust(Integer.toBinaryString(BitPositionEnum.B5.value), 8, '0'
        ));


    }

    @Test
    void getBitStatusMap() {
        Set<Integer> integers = BitPositionEnum.getBitStatusMap().keySet();
        assert integers.size() == 32;

        Assertions.assertSame(32, Lira.of(integers).filter(BitUtil::onlyOneBit).size());
    }

    @Test
    void test1() {
        Map<BitPositionEnum, String> status = new EnumMap<>(BitPositionEnum.class);

        status.put(BitPositionEnum.B2, "B2");
        status.put(BitPositionEnum.B1, "B1");

        Assertions.assertEquals("[0000 0000 0000 0000 0000 0000 0000 0001, 0000 0000 0000 0000 0000 0000 0000 0010]",
                status.keySet().toString());
    }

    @Test
    void convertStatusToEnum() {

        Assertions.assertSame(BitPositionEnum.B1, BitPositionEnum.of(1).next());

        ConsoleUtil.print(BitPositionEnum.of(2));
    }
}
