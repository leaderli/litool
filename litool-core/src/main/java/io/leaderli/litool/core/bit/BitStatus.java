package io.leaderli.litool.core.bit;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.util.ObjectsUtil;

import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leaderli
 * @since 2022/6/15 10:37 AM
 * <p>
 * Use constant names to represent binary
 */
public class BitStatus {

    private final Map<BitStatusEnum, String> bit_name = new EnumMap<>(BitStatusEnum.class);

    private BitStatus() {
    }

    /**
     * @param statusConstant the class  who contain constants with represent status
     * @return a new BitStatus
     */
    public static BitStatus of(Class<?> statusConstant) {
        Map<Integer, BitStatusEnum> bitStatusMap = BitStatusEnum.getBitStatusMap();
        BitStatus bitStatus = new BitStatus();

        Lira<Field> sorted = ReflectUtil.getFields(statusConstant)
                .filter(field -> ObjectsUtil.sameAny(field.getType(), int.class, Integer.class)
                        && ModifierUtil.isPublic(field)
                        && ModifierUtil.isFinal(field)
                        && ModifierUtil.isStatic(field)
                );

        for (Field field : sorted) {

            ReflectUtil.getFieldValue(null, field)
                    .cast(Integer.class)
                    .map(bitStatusMap::get)
                    .ifPresent(statusEnum ->
                            bitStatus.bit_name.putIfAbsent(statusEnum, field.getName())
                    );
        }

        return bitStatus;


    }

    /**
     * According to the state value, output the attribute name of each state.
     * The state attribute name is output from right to left according to its
     * position, and is separated by a vertical line.
     * <p>
     * eg:
     * <pre>
     *     PUBLIC|STATIC|INTERFACE|ABSTRACT
     * </pre>
     *
     * @param bit_status the status represented by binary
     * @return The value corresponding to the key in the  {@link #bit_name}
     */
    public String beauty(int bit_status) {

        Lira<String> name_status = BitStatusEnum.of(bit_status).map(this.bit_name::get);
        return StringUtils.join("|", name_status);


    }

    /**
     * eg:
     * {@link java.lang.reflect.Modifier}
     * <pre>
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
     * @return Comparison table for all states
     */
    @Override
    public String toString() {

        return bit_name.entrySet().stream().map(e -> e.getKey() + " " + e.getValue()).collect(Collectors.joining(System.lineSeparator()));
    }
}
