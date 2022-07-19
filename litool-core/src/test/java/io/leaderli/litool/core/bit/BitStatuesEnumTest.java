package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * @author leaderli
 * @since 2022/6/14
 */
class BitStatuesEnumTest {

    @Test
    public void test() {

        assert BitStatusEnum.valueOf("B1").value == 1;
        assert BitStatusEnum.valueOf("B32").value == Integer.MIN_VALUE;

        Assertions.assertEquals("0001", StringUtils.ljust(Integer.toBinaryString(BitStatusEnum.B1.value), 4, "0"));
        Assertions.assertEquals("00010000", StringUtils.ljust(Integer.toBinaryString(BitStatusEnum.B5.value), 8, "0"));

        assert BitStatusEnum.B1.match(0b111);
        assert !BitStatusEnum.B1.match(0b100);

    }

    @Test
    public void getBitStatusMap() {
        Set<Integer> integers = BitStatusEnum.getBitStatusMap().keySet();
        assert integers.size() == 32;
        for (Integer value : integers) {
            assert BitUtil.onlyOneBit(value);

        }
    }

    @Test
    public void test1() {
        Map<BitStatusEnum, String> status = new EnumMap<>(BitStatusEnum.class);

        status.put(BitStatusEnum.B2, "B2");
        status.put(BitStatusEnum.B1, "B1");

        Assertions.assertEquals("[0000 0000 0000 0000 0000 0000 0000 0001, 0000 0000 0000 0000 0000 0000 0000 0010]", status.keySet().toString());

    }
}
