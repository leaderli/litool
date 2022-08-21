package io.leaderli.litool.core.type;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class EnumUtil {


    @SafeVarargs
    public static <T extends Enum<?>> boolean sameAny(T origin, T... compares) {
        for (T compare : compares) {
            if (compare == origin) {
                return true;
            }
        }
        return false;
    }
}
