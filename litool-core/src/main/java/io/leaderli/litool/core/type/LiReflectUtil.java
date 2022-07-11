package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class LiReflectUtil {

    /**
     * onlyCurrentClass = false
     *
     * @param cls  查找的类
     * @param name 查找的属性名
     * @return #getField(Class, String, boolean)
     */
    public static Lino<Field> getField(Class<?> cls, String name) {


        return getField(cls, name, false);

    }

    /**
     * @param cls              查找的类
     * @param name             查找的属性名
     * @param onlyCurrentClass 是否仅查找当前类，不查找父类
     * @return 返回 包含 {@link Field} 的 {@link Lino}
     */
    public static Lino<Field> getField(Class<?> cls, String name, boolean onlyCurrentClass) {


        return Lino.of(cls).map(Class::getFields)
                .toLira(Field.class)
                .filter(f -> f.getName().equals(name))
                .filter(f -> !onlyCurrentClass || f.getDeclaringClass().equals(cls))
                .first()
                .or(

                        Lino.of(cls).map(Class::getDeclaredFields)
                                .toLira(Field.class)
                                .filter(f -> f.getName().equals(name))
                                .filter(f -> !onlyCurrentClass || f.getDeclaringClass().equals(cls))
                                .first()::get
                );

    }

    /**
     * onlyCurrentClass = false
     *
     * @param o    查找的实例
     * @param name 查找的属性名
     * @return #getFieldValue(Object, String, boolean)
     */
    public static Lino<?> getFieldValue(Object o, String name) {


        return getFieldValue(o, name, false);
    }

    /**
     * @param o                查找的实例
     * @param name             查找的属性名
     * @param onlyCurrentClass 是否值查找当前类
     * @return 包含属性实际值的 {@link Lino}, 当类不存在或者属性不存在时返回 {@link Lino#none()}
     * @see #getField(Class, String, boolean)
     */
    public static Lino<?> getFieldValue(Object o, String name, boolean onlyCurrentClass) {


        return Lino.of(o)
                .map(Object::getClass)
                .map(c -> getField(c, name, onlyCurrentClass))
                .map(Lino::get)
                .throwable_map(f -> {
                    Object result;
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                        result = f.get(o);
                        f.setAccessible(false);
                    } else {
                        result = f.get(0);
                    }
                    return result;
                })
                ;


    }
}
