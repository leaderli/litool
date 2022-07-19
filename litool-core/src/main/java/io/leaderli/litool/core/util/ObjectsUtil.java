package io.leaderli.litool.core.util;

/**
 * @author leaderli
 * @since 2022/6/16 1:02 PM
 */
public class ObjectsUtil {

    public static boolean sameAny(Object origin, Object... compares) {


        for (Object compare : compares) {
            if (compare == origin) {
                return true;
            }
        }
        return false;

    }
}
