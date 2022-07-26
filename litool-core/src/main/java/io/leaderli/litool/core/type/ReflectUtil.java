package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * @param cls  cls
     * @param args 构造器参数 , 对于可选参数，需要手动传递数组类型
     * @param <T>  泛型
     * @return 根据参数个数和参数的类型，调用最接近的一个构造器
     */
    public static <T> Lino<T> newInstance(Class<T> cls, Object... args) {

        if (cls == null || args == null || args.length == 0) {
            return newInstance(cls);
        }

        return Lira.of(cls.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == args.length)
                .filter(constructor -> sameParameterTypes(constructor, args))
                .first()
                .throwable_map(c -> c.newInstance(args))
                .cast(cls);


    }

    private static Object sameParameterTypes(Constructor<?> constructor, Object[] args) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {


            Class<?> parameterType = parameterTypes[i];
            final Object arg = args[i];

            if (parameterType.isPrimitive()) {

                // 基础类型，参数传 null 时，不符合
                if (arg == null) {
                    return false;
                }

                if (!ClassUtil._instanceof(arg, parameterType)) {
                    return false;
                }

            }
            if (arg != null && !ClassUtil._instanceof(args[i], parameterType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param <T> 泛型
     * @param cls class
     * @return 返回一个新的实例
     */
    public static <T> Lino<T> newInstance(Class<T> cls) {
        return Lino.of(cls).throwable_map(Class::newInstance);
    }

    /**
     * @param cls 类
     * @return 获取所有注解，包括重复注解，忽略重复注解的容器注解
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Lira<Annotation> getAnnotations(Class<?> cls) {


        if (cls == null) {
            return Lira.none();
        }

        List<Annotation> result = new ArrayList<>();

        for (Annotation annotation : cls.getAnnotations()) {

            MethodScanner methodScanner = new MethodScanner(annotation.annotationType(), false, MethodUtil::methodOfRepeatableContainer);
            methodScanner.scan().first()
                    // 属于重复注解
                    .ifPresent(m -> {
                        Class componentType = m.getReturnType().getComponentType();
                        result.addAll(Arrays.asList(cls.getAnnotationsByType(componentType)));
                    })
                    // 非重复注解
                    .ifAbsent(() -> result.add(annotation));
        }
        return Lira.of(result);

    }

}
