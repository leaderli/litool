package io.leaderli.litool.core.type;

public class EnumUtil {

    public static <E extends Enum<E>> E of(Class<E> clazz, String name) {
        return ReflectUtil.invokeStaticMethodByName(clazz, "values")
                .toLira(clazz)
                .filter(e -> e.name().equals(name))
                .first().get();

    }
}
