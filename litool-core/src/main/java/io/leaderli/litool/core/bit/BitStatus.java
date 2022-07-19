package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Modifier;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/6/15 10:37 AM
 */
public class BitStatus {

    private final Map<BitStatusEnum, String> statuses = new EnumMap<>(BitStatusEnum.class);

    private BitStatus() {


    }

    /**
     * @param stateClass 有状态标记的类
     * @return 根据 class 中的 int 静态常量属性标记状态 ，int 的值 需要满足 {@link BitUtil#onlyOneBit(int)}
     */
    public static BitStatus of(Class<?> stateClass) {
        Map<Integer, BitStatusEnum> bitStatusMap = BitStatusEnum.getBitStatusMap();
        BitStatus bitStatus = new BitStatus();
        Stream.of(stateClass.getDeclaredFields())
                .filter(field -> Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()))
                .filter(field -> ObjectsUtil.sameAny(field.getType(), int.class, Integer.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        Integer value = (Integer) field.get(null);
                        field.setAccessible(false);

                        BitStatusEnum statusEnum = bitStatusMap.get(value);
                        if (statusEnum != null) {
                            bitStatus.statuses.put(statusEnum, field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        return bitStatus;
    }

    /**
     * @param status 实际的状态值
     * @return 根据状态值，输出各状态的属性名称，状态属性名称根据其位置从右向左输出，使用竖线分割
     */
    public String beauty(int status) {


        return this.statuses.keySet().stream()
                .filter(bit -> bit.match(status))
                .map(this.statuses::get)
                .collect(Collectors.joining("|"));


    }

    @Override
    public String toString() {

        return statuses.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining(System.lineSeparator()));
    }
}
