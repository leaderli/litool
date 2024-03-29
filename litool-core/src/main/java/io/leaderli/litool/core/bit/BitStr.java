package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022/6/15 10:37 AM
 * <p>
 * 根据类中的值等于{@link  BitPositionEnum#mask_msb}的静态常量名来代表一个int值的二进制形式。
 * <p>
 * 例如：
 * <p>
 * {@code
 * 0000 0000 0000 0000 0000 1100 0001 1111
 * STRICT|ABSTRACT|FINAL|STATIC|PROTECTED|PRIVATE|PUBLIC
 * }
 */
public class BitStr {

    private final Map<BitPositionEnum, String> bit_name = new EnumMap<>(BitPositionEnum.class);

    private BitStr() {
    }

    /**
     * 根据包含状态常量的类，创建一个新的 BitStatus 实例。
     *
     * @param statusConstant 包含状态常量的类
     * @return 新的 BitStatus 实例
     */
    public static BitStr of(Class<?> statusConstant) {

        Map<Integer, BitPositionEnum> statusBitPositionEnumMap = BitPositionEnum.newStatusBitPositionEnumMap();
        BitStr bitStr = new BitStr();

        Arrays.stream(statusConstant.getFields())
                .filter(field -> ObjectsUtil.sameAny(field.getType(), int.class, Integer.class)
                        && ModifierUtil.isPublic(field)
                        && ModifierUtil.isFinal(field)
                        && ModifierUtil.isStatic(field))
                .forEach(
                        field -> {
                            try {
                                ReflectUtil.setAccessible(field);
                                int statues = (int) field.get(null);
                                BitPositionEnum bitPositionEnum = statusBitPositionEnumMap.get(statues);
                                if (bitPositionEnum != null) {
                                    bitStr.bit_name.putIfAbsent(bitPositionEnum, field.getName());
                                }
                            } catch (IllegalAccessException ignore) {

                            }
                        }

                );

        return bitStr;


    }


    /**
     * 根据状态值输出每个状态的属性名称。状态属性名称从右到左输出，并用竖线分隔。
     * <p>
     * 示例：
     * <pre>
     *     PUBLIC|STATIC|INTERFACE|ABSTRACT
     * </pre>
     *
     * @param bitStatus 用二进制表示的状态值
     * @return 与 {@link #bit_name} 中的键对应的值
     */
    public String beauty(int bitStatus) {
        // 将状态值的每个位解析为 BitPositionEnum 枚举，并放入列表中
        List<BitPositionEnum> bitPositionEnums = new ArrayList<>();
        BitPositionEnum.of(bitStatus).forEachRemaining(bitPositionEnums::add);
        // 获取每个位对应的属性名称，并过滤掉空值
        Iterator<String> names = bitPositionEnums.stream().map(this.bit_name::get).filter(Objects::nonNull).iterator();
        // 将属性名称用竖线连接起来作为结果返回
        return StringUtils.join("|", names);
    }

    /**
     * 当前常量类的对照
     * 示例:
     * <pre>
     * {@link java.lang.reflect.Modifier}
     *
     * 0000 0000 0000 0000 0000 0000 0000 0001 PUBLIC
     * 0000 0000 0000 0000 0000 0000 0000 0010 PRIVATE
     * 0000 0000 0000 0000 0000 0000 0000 0100 PROTECTED
     * 0000 0000 0000 0000 0000 0000 0000 1000 STATIC
     * 0000 0000 0000 0000 0000 0000 0001 0000 FINAL
     * 0000 0000 0000 0000 0000 0000 0010 0000 SYNCHRONIZED
     * 0000 0000 0000 0000 0000 0000 0100 0000 VOLATILE
     * 0000 0000 0000 0000 0000 0000 1000 0000 TRANSIENT
     * 0000 0000 0000 0000 0000 0001 0000 0000 NATIVE
     * 0000 0000 0000 0000 0000 0010 0000 0000 INTERFACE
     * 0000 0000 0000 0000 0000 0100 0000 0000 ABSTRACT
     * 0000 0000 0000 0000 0000 1000 0000 0000 STRICT
     * </pre>
     *
     * @return 当前常量类各个MSB状态位的对照表
     */
    @Override
    public String toString() {

        return bit_name.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining(System.lineSeparator()));
    }
}
