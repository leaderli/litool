package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class ReflectUtil {

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
     * @param obj  查找的实例
     * @param name 查找的属性名
     * @return #getFieldValue(Object, String, boolean)
     */
    public static Lino<?> getFieldValue(Object obj, String name) {


        return getFieldValue(obj, name, false);
    }

    /**
     * @param obj              查找的实例
     * @param name             查找的属性名
     * @param onlyCurrentClass 是否只查找当前类
     * @return 包含属性实际值的 {@link Lino}, 当类不存在或者属性不存在时返回 {@link Lino#none()}
     * @see #getField(Class, String, boolean)
     */
    public static Lino<?> getFieldValue(Object obj, String name, boolean onlyCurrentClass) {


        return Lino.of(obj)
                .map(o -> getField(obj.getClass(), name, onlyCurrentClass).get())
                .map(f -> getFieldValue(obj, f).get());

    }

    /**
     * @param obj   查找的实例
     * @param field 查找的属性
     * @return 包含属性实际值的 {@link Lino}, 当类不存在或者属性不存在时返回 {@link Lino#none()}
     */
    public static Lino<?> getFieldValue(Object obj, Field field) {

        if (obj == null) {
            return Lino.none();
        }
        return Lino.of(field).throwable_map(f -> {

                    Object result;
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                        result = f.get(obj);
                        f.setAccessible(false);
                    } else {
                        result = f.get(obj);
                    }
                    return result;
                }

        );
    }

    /**
     * onlyCurrentClass = false
     *
     * @param obj   查找的实例
     * @param name  查找的属性名
     * @param value 设置的值
     * @return {@link #setFieldValue(Object, String, Object, boolean)}
     */
    public static boolean setFieldValue(Object obj, String name, Object value) {


        return setFieldValue(obj, name, value, false);
    }

    /**
     * @param obj              查找的实例
     * @param name             查找的属性名
     * @param value            设置的值
     * @param onlyCurrentClass 是否只查找当前类
     * @return 返回是否成功修改值
     */

    public static boolean setFieldValue(Object obj, String name, Object value, boolean onlyCurrentClass) {


        return Lino.of(obj)
                .map(o -> getField(obj.getClass(), name, onlyCurrentClass).get())
                .map(f -> setFieldValue(obj, f, value)).get(false);

    }

    /**
     * @param obj   查找的实例
     * @param field 查找的属性
     * @param value 设置的值
     * @return 返回是否成功修改值
     */
    public static boolean setFieldValue(Object obj, Field field, Object value) {

        if (obj == null) {
            return false;
        }
        return Lino.of(field).throwable_map(f -> {

            if (!f.isAccessible()) {
                f.setAccessible(true);
                f.set(obj, value);
                f.setAccessible(false);
            } else {
                f.set(obj, value);
            }
            // 执行到此，说明未抛出异常，则可以表明赋值成功
            return true;
        }).present();

    }
}
