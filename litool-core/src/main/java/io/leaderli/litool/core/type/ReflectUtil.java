package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.ReflectionAccessor;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.meta.ra.NullableFunction;
import io.leaderli.litool.core.proxy.DynamicDelegation;
import io.leaderli.litool.core.text.StrSubstitution;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class ReflectUtil {

    private static final ReflectionAccessor REFLECTION_ACCESSOR = ReflectionAccessor.getInstance();

    /**
     * onlyCurrentClass = false
     *
     * @param cls  the class
     * @param name the name of field
     * @return {@link #getField(Class, String, boolean)}
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

        return CollectionUtils.union(Lira.of(cls.getFields()), Lira.of(cls.getDeclaredFields())).filter(f -> !f.isSynthetic());
    }


    /**
     * onlyCurrentClass = false
     *
     * @param obj  the obj
     * @param name the name of field
     * @return {@link #getFieldValue(Object, String, boolean)}
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
            REFLECTION_ACCESSOR.makeAccessible(obj);
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
     *             such as {@code  Demo(String ... names) }, should called by {@code  newInstance(Demo.class,new
     *             String[]{"li","yang"})}
     * @param <T>  the type of instance
     * @return according to the number of parameters and it's class type, choose a similar one constructor to new a obj
     */
    public static <T> Lino<T> newInstance(Class<T> cls, Object... args) {


        if (args == null || args.length == 0) {
            return newInstance(cls);
        }

        return getConstructors(cls)
                .filter(constructor -> constructor.getParameterCount() == args.length)
                .filter(constructor -> sameParameterTypes(constructor, args))
                .first()
                .unzip(c -> newInstance(c, args));


    }

    /**
     * @param cls class
     * @param <T> the type of instance
     * @return return the new instance create by the no-argument constructor of cls
     * @throws NullPointerException if {@code  cls == null}
     * @throws AssertException      if  cls is interface or abstract class
     */
    public static <T> Lino<T> newInstance(Class<T> cls) {


        if (cls.isInterface() || ModifierUtil.isAbstract(cls)) {
            return Lino.none();
        }
        Lino<T> instance = getConstructor(cls).unzip(ReflectUtil::newInstance);

        if (instance.present() || ModifierUtil.isStatic(cls)) {

            return instance;
        }
        // inner class
        return getMemberConstructor(cls).unzip(ReflectUtil::memberInstance);

    }

    /**
     * @param constructor the constructor
     * @param args        the args of constructor
     * @param <T>         the type of constructor
     * @return a new instance create by constructor
     * @throws NullPointerException if {@code  cls == null}
     * @throws AssertException      if  cls is interface or abstract class
     */
    public static <T> Lino<T> newInstance(Constructor<T> constructor, Object... args) {

        Class<T> cls = constructor.getDeclaringClass();
        if (cls.isInterface() || ModifierUtil.isAbstract(cls)) {
            return Lino.none();
        }
        setAccessible(constructor);
        return Lino.throwable_of(() -> constructor.newInstance(args));
    }

    private static <T> Lino<T> memberInstance(Constructor<T> c) {
        return ReflectUtil
                .newInstance(c.getParameterTypes()[0])
                .unzip(arg -> ReflectUtil.newInstance(c, arg));
    }

    public static <T> Lino<Constructor<T>> getMemberConstructor(Class<T> cls) {

        Objects.requireNonNull(cls);
        if (cls.isMemberClass() || cls.isSynthetic()) {
            return getConstructors(cls)
                    .filter(c ->
                            // find the constructor with out class parameter
                            Lira.of(c.getParameterTypes()).first()
                                    .filter(par -> par == cls.getEnclosingClass() || cls.getName().startsWith(par.getName()))
                    )
                    .first();
        }

        return Lino.none();
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
        return findAnnotations(cls, NullableFunction.notNull());
    }

    /**
     * @param annotatedElement the annotatedElement
     * @param metaAnnotation   the annotation annotated at an annotation class
     * @return the lira of annotation of annotatedElement and each annotation is annotated by metaAnnotation
     * @see #findAnnotations(AnnotatedElement, Function)
     */
    public static Lira<Annotation> findAnnotationsWithMetaAnnotation(AnnotatedElement annotatedElement, Class<?
            extends Annotation> metaAnnotation) {

        return findAnnotations(annotatedElement,
                annotation -> annotation.annotationType().isAnnotationPresent(metaAnnotation));
    }

    /**
     * @param cls    the class
     * @param filter the  {@link  Function} accept annotation and return a value that convert to boolean by
     *               {@link  io.leaderli.litool.core.util.BooleanUtil#parse(Boolean)}
     * @return get all annotation, include repeatable annotation, and will remove the container annotation of
     * repeatable annotation.
     * eg:
     * will exclude {@code NotNulls}
     * <pre>
     * {@code @Retention(RetentionPolicy.RUNTIME)}
     * {@code @Target(ElementType.TYPE)}
     * {@code @Repeatable(NotNulls.class)}
     * {@code @interface} NotNull {
     *  String value();
     * }
     * {@code @Retention(RetentionPolicy.RUNTIME)}
     * {@code @Target(ElementType.TYPE)}
     * {@code @interface} NotNulls {
     *  NotNull[] value();
     *  String name() default "";
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
     * @param obj  the obj
     * @param name the name of method in obj
     * @param args the args of method
     * @return return value that method return, if method is void, it always return {@code null}
     */
    public static Lino<?> invokeMethodByName(Object obj, String name, Object... args) {

        if (obj == null || name == null) {
            return Lino.none();
        }
        return ReflectUtil.getMethod(obj.getClass(), name).unzip(method -> invokeMethod(method, obj, args));
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
        return CollectionUtils.union(cls.getMethods(), cls.getDeclaredMethods()).filter(m -> !m.isSynthetic());
    }

    /**
     * @param method the method
     * @param obj    the obj, if {@code  obj == null}, the method should be static method
     * @param args   the args of method
     * @return return value that method return, if method is void, it always return {@code null}
     */

    public static Lino<?> invokeMethod(Method method, Object obj, Object... args) {

        if (method == null) {
            return Lino.none();
        }
        setAccessible(method);

        return Lino.throwable_of(() -> method.invoke(obj, args));
    }

    /**
     * use {@link LiTypeToken} is for generic purpose
     * <p>
     * eg:
     * <pre>{@code
     * LiTypeToken token = new LiTypeToken<Supplier<String>>() {};
     * Function<String, String> func = ReflectUtil.newInterfaceImpl(token, delegate);
     * }</pre>
     *
     * @param type     the typeToken of interface
     * @param delegate the delegate bean
     * @param <T>      the type of interface
     * @return {@link #newInterfaceImpl(Class, DynamicDelegation)}
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInterfaceImpl(LiTypeToken<T> type, DynamicDelegation delegate) {
        return (T) newInterfaceImpl(type.getRawType(), delegate);
    }

    /**
     * use {@link  Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)} to dynamic create a implement
     * of the interface. the origin method in interface will delegate to delegation bean with same {@link MethodSignature}
     * or delegate the method which annotated by {@link RuntimeType} and type compatible.
     * <p>
     * the interface should only have one method, and don't have super class
     * <p>
     * the delegation will be inject the origin method by {@link  DynamicDelegation#setOrigin(Method)}
     * <p>
     * eg:
     * <pre>{@code
     * Function func = ReflectUtil.newInterfaceImpl(Function.class, delegate);
     * }</pre>
     *
     * @param _interface the interface
     * @param delegation the delegation bean
     * @param <T>        the type of interface
     * @return a dynamic implement of interface
     * @throws AssertException if _interface is not the single-method and no super class interface or delegation don't
     *                         have delegate method
     * @see DynamicDelegation
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInterfaceImpl(Class<T> _interface, DynamicDelegation delegation) {

        LiAssertUtil.assertTrue(_interface.isInterface() && _interface.getInterfaces().length == 0, "only support interface and without super class");
        final Map<Method, Method> delegateMethods = new HashMap<>();
        Method[] methods = ReflectUtil.getMethods(_interface).toArray(Method.class);
        LiAssertUtil.assertTrue(methods.length == 1, "only support one method interface");

        Method origin = methods[0];
        delegation.setOrigin(origin);
        Method find = MethodUtil.getSameSignatureMethod(delegation, origin).or(() ->

                        ReflectUtil.getMethods(delegation.getClass())
                                .filter(m -> ReflectUtil.getAnnotation(m, RuntimeType.class))
                                .filter(m -> ClassUtil.isAssignableFromOrIsWrapper(m.getReturnType(), origin.getReturnType()))
                                .filter(m -> {
                                    if (m.getParameterTypes().length != origin.getParameterTypes().length) {
                                        return false;
                                    }
                                    for (int i = 0; i < m.getParameterTypes().length; i++) {

                                        if (!ClassUtil.isAssignableFromOrIsWrapper(m.getParameterTypes()[i], origin.getParameterTypes()[i])) {
                                            return false;
                                        }
                                    }
                                    return true;
                                })
                                .first().get())
                .assertNotNone(() -> StrSubstitution.format("{delegation} cannot delegate {origin}", delegation, origin))
                .get();
        delegateMethods.put(origin, find);
        for (Method om : ReflectUtil.getMethods(Object.class)) {
            delegateMethods.put(om, om);
        }

        return (T) Proxy.newProxyInstance(_interface.getClassLoader(), new Class[]{_interface}, (proxy, method, args) -> delegateMethods.get(method).invoke(delegation, args));

    }

}
