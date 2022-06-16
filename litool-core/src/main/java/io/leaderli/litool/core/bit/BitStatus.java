package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.util.LiObjUtil;

import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/15 10:37 AM
 */
public class BitStatus {

    private final Map<BitStatusEnum, String> status = new EnumMap<BitStatusEnum, String>(BitStatusEnum.class);

    private BitStatus() {


    }

    public static BitStatus of(Class<?> stateClass) {
        Map<Integer, BitStatusEnum> bitStatusMap = BitStatusEnum.getBitStatusMap();
        BitStatus bitStatus = new BitStatus();
        Stream.of(stateClass.getFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()))
                .filter(field -> LiObjUtil.sameAny(field.getType() ,int.class , Integer.class))
                .forEach(field -> {
                    try {
                        Integer value = (Integer) field.get(null);

                        BitStatusEnum statusEnum = bitStatusMap.get(value);
                        if (statusEnum != null) {
                            bitStatus.status.put(statusEnum, field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(bitStatus.status);
        return bitStatus;
    }

    public String beauty(int status) {

        return "";

    }
}
