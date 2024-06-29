package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

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

    /**
     * 该数组的角标对应一个 {@link BitPositionEnum#mask_msb} 的所对应的名称，如果为null，则表示无名称
     */
    private final String[] mask_msb_name = new String[BitPositionEnum.values().length];


    /**
     * 根据包含状态常量的类，创建一个新的 BitStatus 实例。
     *
     * @param statusConstant 包含状态常量的类
     * @return 新的 BitStatus 实例
     */
    public static BitStr of(Class<?> statusConstant) {

        BitStr bitStr = new BitStr();
        for (Field field : statusConstant.getFields()) {
            if (ObjectsUtil.sameAny(field.getType(), int.class, Integer.class)
                    && ModifierUtil.isPublic(field)
                    && ModifierUtil.isFinal(field)
                    && ModifierUtil.isStatic(field))
                try {
                    ReflectUtil.setAccessible(field);
                    int statues = (int) field.get(null);
                    BitPositionEnum bitPositionEnum = BitPositionEnum.of(statues);
                    if (bitPositionEnum != BitPositionEnum.NONE) {
                        bitStr.mask_msb_name[bitPositionEnum.mask_position] = field.getName();
                    }
                } catch (IllegalAccessException ignore) {

                }

        }

        return bitStr;

    }


    /**
     * 根据状态值输出每个状态的属性名称。状态属性名称按照在二进制上的位置的顺序输出，并用竖线分隔。
     * <p>
     * 示例：
     * <pre>
     *     PUBLIC|STATIC|INTERFACE|ABSTRACT
     * </pre>
     *
     * @param bitStatus 用二进制表示的状态值
     * @return 与 {@link #mask_msb_name} 中的键对应的值
     */
    public String beauty(int bitStatus) {
        // 获取每个位对应的属性名称，并过滤掉空值
        String[] names = Stream.of(BitPositionEnum.ofs(bitStatus))
                .map(b -> mask_msb_name[b.mask_position])
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        // 将属性名称用竖线连接起来作为结果返回，并按照二进制数字的顺序返回
        ArrayUtils.reverse(names);
        return String.join("|", names);
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
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < mask_msb_name.length; i++) {
            String name = mask_msb_name[i];
            if (name != null) {
                str.append(BitPositionEnum.getByPosition(i)).append(" ").append(name).append(System.lineSeparator());
            }
        }
        return str.toString();
    }
}
