package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.LiConstant;
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
     * @param cls  the class
     * @param name the name of field
     * @return {@link #getField(Class, String, boolean))
     */
    public static Lino<Field> getField(Class<?> cls, String name) {


        return getField(cls, name, false);

    }

    /**
     * @param cls              the class
     * @param name             the name of field
     * @param onlyCurrentClass whether only find cls and not find supper class
     * @return the lino of field that  cls contain
     */
    public static Lino<Field> getField(Class<?> cls, String name, boolean onlyCurrentClass) {


        return getFields(cls)
                .filter(f -> f.getName().equals(name))
                .filter(f -> !onlyCurrentClass || f.getDeclaringClass().equals(cls))
                .first();

    }

    /**
     * get all field of class or it's super class, if {@link  Class#isMemberClass()} it will
     * have a jvm default field that named {@link  LiConstant#INNER_CLASS_THIS_FIELD}, the result
     * of this method will remove this default jvm  field
     *
     * @param cls the class
     * @return all field of class or it's super class
     * @see Class#getFields()
     * @see Class#getDeclaredFields()
     */
    public static Lira<Field> getFields(Class<?> cls) {

        if (cls == null) {
            return Lira.none();
        }

        Lira<Field> union = CollectionUtils.union(Lira.of(cls.getFields()), Lira.of(cls.getDeclaredFields()));
        if (cls.isMemberClass()) {

            return union.filter(f -> !f.isSynthetic() || !f.getName().equals(LiConstant.INNER_CLASS_THIS_FIELD));
        }
        return union;
    }


    /**
     * onlyCurrentClass = false
     *
     * @param obj  the obj
     * @param name the name of field
     * @return {@link #getFieldValue(Object, String, boolean))
     */
    public static Lino<?> getFieldValue(Object obj, String name) {


        return getFieldValue(obj, name, false);
    }

    /**
     * @param obj              the obj
     * @param name             the name of field
     * @param onlyCurrentClass whether only find cls and not find supper class
     * @return the lino of field value, if cannot find the field or field value is not present,
     * will return {@link  Lino#none()}
     * @see #getField(Class, String, boolean)
     * @see #getFieldValue(Object, Field)
     */
    public static Lino<?> getFieldValue(Object obj, String name, boolean onlyCurrentClass) {


        if (obj == null) {
            return Lino.none();
        }
        return getField(obj.getClass(), name, onlyCurrentClass).map(f -> getFieldValue(obj, f).get());

    }

    /**
     * @param obj   the obj is instance of the  {@link  Field#getDeclaringClass()},
     *              if {@code obj == null}, it's commonly mean the static field
     * @param field the field
     * @return the lino of field value, if cannot find the field or field value is not present,
     * will return {@link  Lino#none()}
     */
    public static Lino<?> getFieldValue(Object obj, Field field) {


        if (field == null) {
            return Lino.none();
        }

        setAccessible(field);
        return Lino.throwable_of(() -> field.get(obj));
    }

    public static void setAccessible(AccessibleObject obj) {
        if (obj != null) {
            obj.setAccessible(true);
        }
    }


    /**
     * onlyCurrentClass = false
     *
     * @param obj   the obj
     * @param name  the name of field
     * @param value the new value of field
     * @return {@link #setFieldValue(Object, String, Object, boolean)}
     */
    public static boolean setFieldValue(Object obj, String name, Object value) {

        return setFieldValue(obj, name, value, false);
    }


    /**
     * @param obj              the obj
     * @param name             the name of field
     * @param value            the new value of field
     * @param onlyCurrentClass whether only find cls and not find supper class
     * @return whether the value was set successfully
     * @see #getField(Class, String, boolean)
     * @see #setFieldValue(Object, Field, Object)
     */
    public static boolean setFieldValue(Object obj, String name, Object value, boolean onlyCurrentClass) {

        if (obj == null) {
            return false;
        }

        return getField(obj.getClass(), name, onlyCurrentClass)
                .map(f -> setFieldValue(obj, f, value)).get(false);

    }

    /**
     * @param obj   the obj
     * @param field the field
     * @param value the new value of field
     * @return whether the value was set successfully
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
     * @param args the constructor arguments, for the optional parameter, it should manually use arr.
     *             such as {@code  Demo(String ... names) }, should called by {@code  newInstance(Demo.class,new String[]{"li","yang"})}
     * @param <T>  the type of instance
     * @return according to the number of parameters and it's class type, choose a similar one constructor to new a obj
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
     * @param cls class
     * @param <T> the type of instance
     * @return return the new instance create by the no-argument constructor of cls
     */
    public static <T> Lino<T> newInstance(Class<T> cls) {
        Objects.requireNonNull(cls);
        return getConstructor(cls)
                .throwable_map(constructor -> {
                    setAccessible(constructor);
                    return constructor.newInstance();
                });


    }


    /**
     * union of {@link  Class#getConstructors()} and {@link  Class#getDeclaredConstructors()}
     *
     * @param cls class
     * @param <T> the type of instance
     * @return all constructor of cls, public constructor is ahead of private constructor
     */
    @SuppressWarnings("unchecked")
    public static <T> Lira<Constructor<T>> getConstructors(Class<T> cls) {

        Objects.requireNonNull(cls);
        Object union = CollectionUtils.union(cls.getConstructors(), cls.getDeclaredConstructors());
        return (Lira<Constructor<T>>) union;

    }

    private static Object sameParameterTypes(Constructor<?> constructor, Object[] args) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {


            Class<?> parameterType = parameterTypes[i];
            final Object arg = args[i];

            if (parameterType.isPrimitive()) {

                // primitive value don't have null
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
     * @param cls class
     * @param <T> the type of instance
     * @return the no-argument constructor, it's may be private constructor.
     */
    public static <T> Lino<Constructor<T>> getConstructor(Class<T> cls) {

        return getConstructors(cls)
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .first();
    }

    /**
     * @param annotationInstance the  annotation instance
     * @param annotated          the annotation that the annotation class of annotationInstance may  annotated
     * @param <T>                the type of  annotated annotation
     * @return the first found annotation annotated at the annotation class of annotationInstance
     * @see Annotation#annotationType()
     * @see #getAnnotation(AnnotatedElement, Class)
     */
    public static <T extends Annotation> Lino<T> getAnnotation(Annotation annotationInstance, Class<T> annotated) {

        return Lino.of(annotationInstance)
                .map(Annotation::annotationType)
                .unzip(type -> getAnnotation(type, annotated));
    }

    /**
     * @param annotatedElement the annotatedElement
     * @param annotated        the annotation that annotatedElement may  annotated
     * @param <T>              the type of  annotated annotation
     * @return the first found annotation annotated at annotatedElement
     */
    public static <T extends Annotation> Lino<T> getAnnotation(AnnotatedElement annotatedElement, Class<T> annotated) {
        return Lira.of(annotatedElement.getAnnotationsByType(annotated)).first();
    }

    /**
     * filter = null
     *
     * @param cls the class
     * @return #findAnnotations(Class, Function)
     */
    public static Lira<Annotation> findAnnotations(AnnotatedElement cls) {
        return findAnnotations(cls, null);
    }

    /**
     * @param cls    the class
     * @param filter the  {@link  Function} accept annotation and return a value that convert to boolean by {@link  io.leaderli.litool.core.util.BooleanUtil#parse(Boolean)}
     * @return get all annotation, include repeatable annotation, and will remove the container annotation of repeatable annotation.
     * eg:
     * will exclude {@code NotNulls}
     * <pre>
     * {@code
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target(ElementType.TYPE)
     * @Repeatable(NotNulls.class)
     * @interface NotNull {
     *  String value();
     * }
     *
     * @Retention(RetentionPolicy.RUNTIME)
     * @Target(ElementType.TYPE)
     * @interface NotNulls {
     *  NotNull[] value();
     *  String name() default "";
     * }
     * }
     * </pre>
     * @see io.leaderli.litool.core.util.BooleanUtil#parse(Boolean)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Lira<Annotation> findAnnotations(AnnotatedElement cls, Function<? super Annotation, ?> filter) {


        if (cls == null) {
            return Lira.none();
        }

        List<Annotation> result = new ArrayList<>();

        for (Annotation annotation : cls.getAnnotations()) {

            MethodScanner methodScanner = new MethodScanner(annotation.annotationType(), false,
                    MethodUtil::methodOfRepeatableContainer);
            methodScanner.scan().first()
                    .ifPresent(m -> {
                        //  repeatable annotation
                        Class componentType = m.getReturnType().getComponentType();
                        result.addAll(Arrays.asList(cls.getAnnotationsByType(componentType)));
                    })
                    .ifAbsent(() -> result.add(annotation));
        }
        return Lira.of(result).filter(filter);

    }

    /**
     * @param annotatedElement the annotatedElement
     * @param metaAnnotation   the annotation annotated at an annotation class
     * @return the lira of annotation of annotatedElement and each annotation is annotated by metaAnnotation
     * @see #findAnnotations(AnnotatedElement, Function)
     */
    public static Lira<Annotation> findAnnotationsWithMetaAnnotation(AnnotatedElement annotatedElement, Class<? extends Annotation> metaAnnotation) {

        return findAnnotations(annotatedElement, annotation -> annotation.annotationType().isAnnotationPresent(metaAnnotation));
    }


    /**
     * onlyCurrentClass = false
     *
     * @param cls  the class
     * @param name the name of  method
     * @return {@link #getMethod(Class, String, boolean)}
     */
    public static Lino<Method> getMethod(Class<?> cls, String name) {
        return getMethod(cls, name, false);
    }


    /**
     * @param cls              the class
     * @param name             the name of  method
     * @param onlyCurrentClass whether only find cls and not find supper class
     * @return the lino of method that cls contain
     */
    public static Lino<Method> getMethod(Class<?> cls, String name, boolean onlyCurrentClass) {

        return getMethods(cls)
                .filter(m -> m.getName().equals(name))
                .filter(m -> !onlyCurrentClass || m.getDeclaringClass().equals(cls))
                .first();
    }


    /**
     * get all method of class or it's super class
     *
     * @param cls the class
     * @return all method of class or it's super class
     * @see Class#getMethods()
     * @see Class#getDeclaredMethods()
     */
    public static Lira<Method> getMethods(Class<?> cls) {
        if (cls == null) {
            return Lira.none();
        }
        return CollectionUtils.union(cls.getMethods(), cls.getDeclaredMethods());
    }

    /**
     * @param method the method
     * @param obj    the obj, if {@code  obj == null}, the method should be static method
     * @param args   the args of method
     * @return return value that method return, if method is void, it always return {@code null}
     */
    public static Lino<?> getMethodValue(Method method, Object obj, Object... args) {

        if (method == null) {
            return Lino.none();
        }
        setAccessible(method);

        return Lino.throwable_of(() -> method.invoke(obj, args));
    }

    /**
     * @param constructor the constructor
     * @param <T>         the type of constructor
     * @return the declare class of constructor
     */
    public static <T> Class<T> getClass(Constructor<T> constructor) {

        if (constructor == null) {
            return null;
        }
        return constructor.getDeclaringClass();
    }

    /**
     * @param method the  method
     * @return the declare class of method
     */
    public static Class<?> getClass(Method method) {
        if (method == null) {
            return null;
        }
        return method.getDeclaringClass();
    }

    /**
     * @param field the  field
     * @return the declare class of field
     */
    public static Class<?> getClass(Field field) {
        if (field == null) {
            return null;
        }
        return field.getDeclaringClass();
    }

    /**
     * @param field the field
     * @return the type of field
     */
    public static Class<?> getType(Field field) {
        if (field == null) {
            return null;
        }
        return field.getType();
    }


    /**
     * @param method the method
     * @return the type of  method return
     */
    public static Class<?> getType(Method method) {
        if (method == null) {
            return null;
        }
        return method.getReturnType();
    }
}
