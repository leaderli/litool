package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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


        return getFields(cls)
                .filter(f -> f.getName().equals(name))
                .filter(f -> !onlyCurrentClass || f.getDeclaringClass().equals(cls))
                .first();

    }

    /**
     * @param cls 查找的类
     * @return 查找类所有的属性
     * @see Class#getFields()
     * @see Class#getDeclaredFields()
     */
    public static Lira<Field> getFields(Class<?> cls) {

        if (cls == null) {
            return Lira.none();
        }

        return CollectionUtils.union(Lira.of(cls.getFields()), Lira.of(cls.getDeclaredFields()));
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


        if (obj == null) {
            return Lino.none();
        }
        return getField(obj.getClass(), name, onlyCurrentClass)
                .map(f -> getFieldValue(obj, f).get());

    }

    /**
     * @param obj   查找的实例 , obj 为空时，一般为调用静态方法
     * @param field 查找的属性
     * @return 包含属性实际值的 {@link Lino}, 当类不存在或者属性不存在时返回 {@link Lino#none()}
     */
    public static Lino<?> getFieldValue(Object obj, Field field) {


        if (field == null) {
            return Lino.none();
        }

        setAccessible(field);
        return Lino.throwable_of(() -> field.get(obj));
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

        if (obj == null) {
            return false;
        }

        return getField(obj.getClass(), name, onlyCurrentClass)
                .map(f -> setFieldValue(obj, f, value)).get(false);

    }

    /**
     * @param obj   查找的实例
     * @param field 查找的属性
     * @param value 设置的值
     * @return 返回是否成功修改值
     */
    public static boolean setFieldValue(Object obj, Field field, Object value) {

        if (field == null) {
            return false;
        }
        setAccessible(field);
        try {
            field.set(obj, value);
            // 执行到此，说明未抛出异常，则可以表明赋值成功
            return true;
        } catch (Throwable ignore) {
            return false;
        }

    }

    /**
     * @param cls  cls
     * @param args 构造器参数 , 对于可选参数，需要手动传递数组类型
     * @param <T>  泛型
     * @return 根据参数个数和参数的类型，调用最接近的一个构造器
     */
    public static <T> Lino<T> newInstance(Class<T> cls, Object... args) {


        Objects.requireNonNull(cls);
        if (args == null || args.length == 0) {
            return newInstance(cls);
        }

        return getConstructors(cls)
                .filter(constructor -> constructor.getParameterCount() == args.length)
                .filter(constructor -> sameParameterTypes(constructor, args))
                .first()
                .throwable_map(c -> c.newInstance(args))
                .cast(cls);


    }

    /**
     * @param <T> 泛型
     * @param cls class
     * @return 返回一个新的实例
     */
    public static <T> Lino<T> newInstance(Class<T> cls) {
        Objects.requireNonNull(cls);
        return getConstructor(cls).throwable_map(constructor -> {
            setAccessible(constructor);
            return constructor.newInstance();
        });


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
     * @param cls 类
     * @param <T> 泛型
     * @return 获取无参构造器
     */
    public static <T> Lino<Constructor<T>> getConstructor(Class<T> cls) {

        return getConstructors(cls)
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .first();
    }

    /**
     * @param cls 类
     * @param <T> 泛型
     * @return 查找类的所有构造器
     * @see Class#getConstructors()
     * @see Class#getDeclaredConstructors()
     */
    @SuppressWarnings("unchecked")
    public static <T> Lira<Constructor<T>> getConstructors(Class<T> cls) {

        Objects.requireNonNull(cls);
        Object union = CollectionUtils.union(cls.getConstructors(), cls.getDeclaredConstructors());
        return (Lira<Constructor<T>>) union;

    }

    /**
     * @param cls 类
     * @return #findAnnotations(Class, Function)
     */
    public static Lira<Annotation> findAnnotations(Class<?> cls) {
        return findAnnotations(cls, null);
    }

    /**
     * @param cls    类
     * @param filter 过滤
     * @return 获取所有注解，包括重复注解，忽略重复注解的容器注解
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Lira<Annotation> findAnnotations(Class<?> cls, Function<? super Annotation, ?> filter) {


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
        return Lira.of(result).filter(filter);

    }

    /**
     * @param cls  类
     * @param mark 标记注解类
     * @return 查找类的所有注解中被 mark 注解的注解
     * @see #findAnnotations(Class, Function)
     */
    public static Lira<Annotation> findAnnotationsWithMark(Class<?> cls, Class<? extends Annotation> mark) {

        return findAnnotations(cls, annotation -> annotation.annotationType().isAnnotationPresent(mark));
    }

    /**
     * 查找当前类或父类的同名方法
     *
     * @param cls  查找的类
     * @param name 方法名
     * @return 查找的方法
     * @see #getMethod(Class, String, boolean)
     */
    public static Lino<Method> getMethod(Class<?> cls, String name) {
        return getMethod(cls, name, false);
    }

    /**
     * @param cls              查找的类
     * @param name             方法名
     * @param onlyCurrentClass 是否仅查找当前类
     * @return 查找的方法
     * @see #getMethod(Class, String, boolean)
     */
    public static Lino<Method> getMethod(Class<?> cls, String name, boolean onlyCurrentClass) {

        return getMethods(cls)
                .filter(m -> m.getName().equals(name))
                .filter(m -> !onlyCurrentClass || m.getDeclaringClass().equals(cls))
                .first();
    }

    /**
     * @param cls 查找的类
     * @return 查找类所有的方法
     * @see Class#getMethods()
     * @see Class#getDeclaredMethods()
     */
    public static Lira<Method> getMethods(Class<?> cls) {
        if (cls == null) {
            return Lira.none();
        }
        return CollectionUtils.union(cls.getMethods(), cls.getDeclaredMethods());
    }

    public static void setAccessible(AccessibleObject obj) {
        if (!obj.isAccessible()) {
            obj.setAccessible(true);
        }
    }

    public static Lino<?> getMethodValue(Method method, Object obj, Object... args) {

        if (method == null) {
            return Lino.none();
        }
        setAccessible(method);

        return Lino.throwable_of(() -> method.invoke(obj, args));
    }

    /**
     * @param cls 类
     * @return 获取类上所有的接口 type ，包括继承类上的，对于重复的接口，以最先查找到的为准
     */
    public static Lira<Type> getInterfacesType(Class<?> cls) {
        List<Type> list = new ArrayList<>();
        while (cls != null && cls != Object.class) {
            list.addAll(getGenericInterfacesOfInterface(cls));
            cls = cls.getSuperclass();
        }
        return Lira.of(list).distinct(TypeUtil::equals);
    }

    private static List<Type> getGenericInterfacesOfInterface(Class<?> type) {

        // 优先添加同一层级
        List<Type> interfaces = new ArrayList<>(Arrays.asList(type.getGenericInterfaces()));

        for (Type genericInterface : type.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                interfaces.addAll(getGenericInterfacesOfInterface((Class<?>) ((ParameterizedType) genericInterface).getRawType()));
            } else if (genericInterface instanceof Class) {
                interfaces.addAll(getGenericInterfacesOfInterface((Class<?>) genericInterface));
            }
        }
        return interfaces;
    }


    /**
     * @param cls 获取类的父类 type ， 会优先使用泛型 type
     * @return 所有父类 type
     * @see Type
     * @see Class#getGenericSuperclass()
     */
    public static Lira<Type> getSuperclassType(Class<?> cls) {
        if (cls == null || cls == Object.class) {
            return Lira.none();
        }

        List<Type> list = new ArrayList<>();
        while (cls != Objects.class) {

            Type temp = cls.getGenericSuperclass();
            if (temp instanceof ParameterizedType) {
                cls = (Class<?>) ((ParameterizedType) temp).getRawType();
            } else {
                temp = cls.getSuperclass();
                cls = (Class<?>) temp;
            }
            if (cls != null && cls != Object.class) {
                list.add(temp);
            } else {
                break;
            }
        }
        return Lira.of(list);
    }

    /**
     * 查找第一个
     *
     * @param cls 类
     * @param sup 泛型接口
     * @return 获取指定泛型接口的泛型类型，如果没有指定泛型类型会返回 {@link Lino#none()}
     * @see #getGenericInterfacesType(Class, Class, int)
     */
    public static Lino<Class<?>> getGenericInterfacesType(Class<?> cls, Class<?> sup) {

        return getGenericInterfacesType(cls, sup, 0);
    }

    /**
     * 查找第一个
     *
     * @param cls      类
     * @param inter    泛型接口
     * @param position 泛型接口的位置
     * @return 获取指定泛型接口的泛型类型，如果没有指定泛型类型会返回 {@link Lino#none()}
     * @see #getGenericInterfacesType(Class, Class, int)
     */
    public static Lino<Class<?>> getGenericInterfacesType(Class<?> cls, Class<?> inter, int position) {


        if (cls == null || inter == null || !inter.isInterface() || inter.getTypeParameters().length < position + 1) {
            return Lino.none();
        }

        Lino<Class<?>> find = getInterfacesType(cls)
                .cast(ParameterizedType.class)
                .filter(type -> type.getRawType() == inter)
                .first()
                .map(type -> type.getActualTypeArguments()[position])
                .filter(type -> !(type instanceof TypeVariable))
                .map(TypeUtil::getClass);
        if (find.present()) {
            return find;
        }


        Lino<ParameterizedType> superType = Lino.none();
        for (Type type : getSuperclassType(cls)) {


            superType = Lino.of(type)
                    .cast(ParameterizedType.class)
                    .filter(f -> ClassUtil.isAssignableFromOrIsWrapper(inter, (Class<?>) f.getRawType()));
//
            if (superType.present()) {
                break;
            }

        }
        return superType
                .map(type -> type.getActualTypeArguments()[position])
                .filter(type -> !(type instanceof TypeVariable))
                .map(TypeUtil::getClass);

    }

    /**
     * 查找第一个
     *
     * @param cls 类
     * @param sup 泛型父类
     * @return 获取指定泛型父类的泛型类型，如果没有指定泛型类型会返回 {@link Lino#none()}
     * @see #getGenericSuperclassType(Class, Class, int)
     */
    public static Lino<Class<?>> getGenericSuperclassType(Class<?> cls, Class<?> sup) {
        return getGenericSuperclassType(cls, sup, 0);

    }

    /**
     * @param cls      类
     * @param sup      泛型父类
     * @param position 泛型父类的位置
     * @return 获取指定泛型父类的泛型类型，如果没有指定泛型类型会返回 {@link Lino#none()}
     */
    public static Lino<Class<?>> getGenericSuperclassType(Class<?> cls, Class<?> sup, int position) {

        if (cls == null || sup == null || position < 0 || cls == Object.class || sup.isInterface() || sup.getTypeParameters().length < position + 1) {
            return Lino.none();
        }

        return getSuperclassType(cls)
                .cast(ParameterizedType.class)
                .filter(type -> type.getRawType() == sup)
                .first()
                .map(type -> type.getActualTypeArguments()[position])
                .map(TypeUtil::getClass);


    }
}
