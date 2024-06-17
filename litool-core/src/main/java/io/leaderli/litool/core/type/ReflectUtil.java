package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.internal.ReflectionAccessor;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.meta.ra.LiraRuntimeException;
import io.leaderli.litool.core.meta.ra.NullableFunction;

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
     * 可以获取对象中的指定字段的值
     *
     * @param obj  目标对象
     * @param name 字段名称
     * @return 返回字段对应的值，如果不存在，则返回 {@link Lino#none()}
     * @see #getField(Class, String)
     * @see #getFieldValue(Object, Field)
     */
    public static Lino<?> getFieldValue(Object obj, String name) {

        if (obj == null) {
            return Lino.none();
        }

        Class<?> clazz = obj.getClass();
        return getField(clazz, name).unzip(f -> getFieldValue(obj, f));
    }

    /**
     * 可以获取指定类中的指定字段
     *
     * @param clazz 目标类
     * @param name  字段名
     * @return 该字段的Lino对象
     * @see #getField(Class, Function)
     */
    public static Lino<Field> getField(Class<?> clazz, String name) {
        return getField(clazz, f -> f.getName().equals(name));
    }

    /**
     * 获取类及其父类的所有字段， JVM自动生成的字段{@link  Field#isSynthetic()}不会包含在返回结果中
     * <p>
     * 不包含 Object 的字段
     *
     * @param clazz 要获取字段的类
     * @return 类及其父类的所有字段
     * @see Class#getFields()
     * @see Class#getDeclaredFields()
     */
    public static Lira<Field> getFields(Class<?> clazz) {

        if (clazz == null || clazz == Object.class) {
            return Lira.none();
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        while ((clazz = clazz.getSuperclass()) != Object.class) {
            declaredFields = ArrayUtils.append(declaredFields, clazz.getDeclaredFields());
        }
        return Lira.of(declaredFields)
                .filter(f -> !f.isSynthetic())
                .sorted((f1, f2) -> ModifierUtil.priority(f2) - ModifierUtil.priority(f1));
    }

    /**
     * 获取指定的属性
     *
     * @param clazz  要获取字段的类
     * @param filter 属性是否满足条件
     * @return 类及其父类的所有字段
     * @see Class#getFields()
     * @see Class#getDeclaredFields()
     */
    public static Lino<Field> getField(Class<?> clazz, Function<Field, Boolean> filter) {

        // 相对于获取全部属性在过滤，速度更快
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getFields()) {
                if (filter.apply(field)) {
                    return Lino.of(field);
                }
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (filter.apply(field)) {
                    return Lino.of(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return Lino.none();

    }


    /**
     * 根据对象和{@link Field}获取字段值
     *
     * @param object 对象实例,如果为null,表示获取静态字段
     * @param field  字段
     * @return 字段值的Lino包装, 如果找不到字段或字段值为空, 返回{@link Lino#none()}
     */
    public static Lino<?> getFieldValue(Object object, Field field) {


        if (field == null) {
            return Lino.none();
        }

        setAccessible(field);
        return Lino.ofIgnoreError(() -> field.get(object));
    }

    /**
     * 设置对象的可访问性
     *
     * @param obj 可访问对象
     */
    public static void setAccessible(AccessibleObject obj) {
        if (obj != null) {
            REFLECTION_ACCESSOR.makeAccessible(obj);
        }
    }

    /*
     * @param obj   目标对象
     * @param name  属性名
     * @param value 属性值
     * @return 是否设置成功
     * @see #getField(Class, String, boolean)
     * @see #setFieldValue(Object, String, Object, boolean) 第三个参数取false
     */
    public static boolean setFieldValue(Object obj, String name, Object value) {

        if (obj == null) {
            return false;
        }
        return getField(obj.getClass(), name)
                .map(f -> setFieldValue(obj, f, value))
                .get(false);
    }

    /**
     * 设置对象的属性值
     *
     * @param obj   目标对象
     * @param field 属性
     * @param value 属性值
     * @return 是否设置成功
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
     * 根据类和参数创建实例
     *
     * @param cls  类
     * @param args 构造方法的参数,对于可选参数,需要手动使用数组,例如:{@code Demo(String ... names)},
     *             应调用为{@code newInstance(Demo.class, new String[]{"li", "yang"})}
     * @param <T>  实例类型
     * @return 根据参数数量和类型选择一个最相近的构造方法创建实例
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
     * 根据类创建实例
     *
     * @param cls 类
     * @param <T> 实例类型
     * @return 使用cls的无参数构造方法创建的新实例，如果是基本类型，则返回其零值。如果是内部类，则会先创建外部类，在创建内部类
     * 如果无法正常创建，则返回 {@link  Lino#none()}
     */
    @SuppressWarnings("unchecked")
    public static <T> Lino<T> newInstance(Class<T> cls) {

        if (cls == null) {
            return Lino.none();
        }
        PrimitiveEnum primitiveEnum = PrimitiveEnum.get(cls);
        if (primitiveEnum != PrimitiveEnum.OBJECT) {
            return (Lino<T>) Lino.of(PrimitiveEnum.get(cls).zero_value);
        }

        if (cls.isArray()) {
            return (Lino<T>) Lino.of(Array.newInstance(cls.getComponentType(), 0));
        }
        if (cls.isInterface() || ModifierUtil.isAbstract(cls)) {
            return Lino.none();
        }
        if (cls.isEnum()) {
            return getMethod(cls, "values").mapIgnoreError(m -> m.invoke(null)).toLira(cls).first();
        }
        Lino<T> instance = getConstructor(cls).unzip(ReflectUtil::newInstance);

        if (instance.present() || ModifierUtil.isStatic(cls)) {

            return instance;
        }
        // inner class
        return getMemberConstructor(cls).unzip(ReflectUtil::memberInstance);

    }

    /**
     * 根据构造方法和参数创建实例
     *
     * @param constructor 构造方法
     * @param args        构造方法的参数
     * @param <T>         构造方法的类型
     * @return 使用构造方法创建的新实例
     * @throws NullPointerException 如果{@code cls == null}
     */
    public static <T> Lino<T> newInstance(Constructor<T> constructor, Object... args) {

        if (constructor == null) {
            return Lino.none();
        }
        Class<T> cls = constructor.getDeclaringClass();
        if (cls.isInterface() || ModifierUtil.isAbstract(cls)) {
            return Lino.none();
        }
        setAccessible(constructor);
        return Lino.ofIgnoreError(() -> constructor.newInstance(args));
    }

    /**
     * @param memberConstructor 内部类的构造器
     * @param <T>               类型
     * @return 内部类的实例
     */
    private static <T> Lino<T> memberInstance(Constructor<T> memberConstructor) {
        return ReflectUtil
                .newInstance(memberConstructor.getParameterTypes()[0])
                .unzip(arg -> ReflectUtil.newInstance(memberConstructor, arg));
    }

    /**
     * 获取内部类的构造方法
     *
     * @param cls 类
     * @param <T> 实例类型
     * @return 内部类的构造方法, 如果找不到构造方法或不是内部类, 返回{@link Lino#none()}
     */
    public static <T> Lino<Constructor<T>> getMemberConstructor(Class<T> cls) {

        if (cls != null && (cls.isMemberClass() || cls.isSynthetic())) {
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
     * {@link Class#getConstructors()} 和 {@link Class#getDeclaredConstructors()}的联合
     *
     * @param cls 类
     * @param <T> 实例类型
     * @return cls的所有构造方法, 公共构造方法优先于私有构造方法
     * @throws NullPointerException 如果 cls == null
     */
    @SuppressWarnings("unchecked")
    public static <T> Lira<Constructor<T>> getConstructors(Class<T> cls) {

        if (cls == null) {
            return Lira.none();
        }
        Object union = CollectionUtils.union(Constructor.class, cls.getConstructors(), cls.getDeclaredConstructors());
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

                if (!ClassUtil.isInstanceof(arg, parameterType)) {
                    return false;
                }

            }
            if (arg != null && !ClassUtil.isInstanceof(args[i], parameterType)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 获取无参数构造方法,可能是私有构造方法。
     *
     * @param cls 类
     * @param <T> 实例类型
     * @return 无参数构造方法
     */
    public static <T> Lino<Constructor<T>> getConstructor(Class<T> cls) {

        return getConstructors(cls)
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .first();
    }


    /**
     * 获取注解实例标注的第一个注解
     *
     * @param annotation 注解实例
     * @param annotated  注解实例的注解类可能标注的注解
     * @param <T>        标注注解的类型
     * @return 找到的第一个标注在注解实例的注解类上的注解
     * @see Annotation#annotationType()
     * @see #getAnnotation(AnnotatedElement, Class)
     */
    public static <T extends Annotation> Lino<T> getAnnotation(Annotation annotation, Class<T> annotated) {

        return Lino.of(annotation)
                .map(Annotation::annotationType)
                .unzip(type -> getAnnotation(type, annotated));
    }

    /**
     * 获取标注元素的第一个标注
     *
     * @param annotatedElement 标注元素
     * @param annotated        标注元素可能标注的注解
     * @param <T>              标注注解的类型
     * @return 找到的第一个标注在标注元素上的注解
     */
    public static <T extends Annotation> Lino<T> getAnnotation(AnnotatedElement annotatedElement, Class<T> annotated) {
        return Lira.of(annotatedElement.getAnnotationsByType(annotated)).first();
    }

    /**
     * @param annotatedElement 注解元素
     * @return 返回元素上所有的注解
     * @see #findAnnotations(AnnotatedElement, Function)
     * @see NullableFunction#notNull()
     */
    public static Lira<Annotation> findAnnotations(AnnotatedElement annotatedElement) {
        return findAnnotations(annotatedElement, NullableFunction.notNull());
    }

    /**
     * 获取所有注解,包括可重复注解,会排除可重复注解的容器注解。
     * <p>
     * 例如:
     * 会排除{@code NotNulls}
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
     *
     * @param cls    类
     * @param filter {@link Function} 接受注解并返回一个值,该值通过
     *               {@link io.leaderli.litool.core.util.BooleanUtil#parse(Boolean)}转换为布尔值
     * @return 所有注解, 包括可重复注解。排除可重复注解的容器注解
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
     * 获取标注元素及其每个注解类都标注了元注解的注解
     *
     * @param annotatedElement 标注元素
     * @param metaannotation   注解类标注的注解
     * @return 标注元素及其每个注解类都标注了元注解的注解
     * @see #findAnnotations(AnnotatedElement, Function)
     */
    public static Lira<Annotation> findAnnotationsWithMetaAnnotation(AnnotatedElement annotatedElement, Class<? extends Annotation> metaannotation) {

        return findAnnotations(annotatedElement, annotation -> annotation.annotationType().isAnnotationPresent(metaannotation));
    }

    /**
     * 通过对象和方法名调用方法
     *
     * @param obj  对象
     * @param name 对象中的方法名
     * @param args 方法的参数
     * @return 方法返回的值, 如果方法的返回值是void, 则总是返回{@code null}
     */
    public static Lino<?> invokeMethodByName(Object obj, String name, Object... args) {

        if (obj == null || name == null) {
            return Lino.none();
        }
        return ReflectUtil.getMethod(obj.getClass(), name).unzip(method -> invokeMethod(method, obj, args));
    }


    /**
     * 通过对象和方法名调用方法
     *
     * @param clazz 类
     * @param name  对象中的方法名
     * @param args  方法的参数
     * @return 方法返回的值, 如果方法的返回值是void, 则总是返回{@code null}
     */
    public static Lino<?> invokeStaticMethodByName(Class<?> clazz, String name, Object... args) {

        if (clazz == null || name == null) {
            return Lino.none();
        }
        return ReflectUtil.getMethod(clazz, name).unzip(method -> invokeMethod(method, null, args));
    }

    /**
     * @param clazz 类
     * @param name  方法名
     * @return {@link #getMethod(Class, MethodFilter)}
     */
    public static Lino<Method> getMethod(Class<?> clazz, String name) {
        return getMethod(clazz, MethodFilter.name(name));
    }

    /**
     * @param clazz  类
     * @param filter 满足条件的方法
     * @return {@link #getMethods(Class)}
     */
    public static Lino<Method> getMethod(Class<?> clazz, MethodFilter filter) {
        while (clazz != null) {
            //相比较于属性，多添加一个，是添加接口的方法
            for (Method method : clazz.getMethods()) {
                if (filter.apply(method)) {
                    return Lino.of(method);
                }
            }

            for (Method method : clazz.getDeclaredMethods()) {
                if (filter.apply(method)) {
                    return Lino.of(method);
                }
            }

            clazz = clazz.getSuperclass();
        }
        return Lino.none();
    }

    /**
     * 获取类或其超类的所有方法，不包含Object的方法
     *
     * @param clazz 类
     * @return 类或其超类的所有方法
     * @see Class#getMethods()
     * @see Class#getDeclaredMethods()
     */
    public static Lira<Method> getMethods(Class<?> clazz) {
        Set<Method> declaredMethods = new HashSet<>();
        while (clazz != null) {
            //相比较于属性，多添加一个，是添加接口的方法
            declaredMethods.addAll(Arrays.asList(clazz.getMethods()));
            declaredMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
        }
        return Lira.of(declaredMethods)
                .filter(f -> !f.isSynthetic())
                .sorted((f1, f2) -> ModifierUtil.priority(f2) - ModifierUtil.priority(f1));
    }

    /**
     * 调用方法
     *
     * @param method 方法
     * @param obj    对象,如果{@code obj == null},方法应该是静态方法
     * @param args   方法的参数
     * @return 方法返回的值, 如果方法的返回值是void, 则总是返回{@code null}
     */
    public static Lino<?> invokeMethod(Method method, Object obj, Object... args) {

        if (method == null) {
            return Lino.none();
        }
        setAccessible(method);

        return Lino.ofIgnoreError(() -> method.invoke(obj, args));
    }

    /**
     * 使用{@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}动态创建接口的实现。
     * 接口中的原始成员方法将委托给具有相同{@link MethodSignature}的proxyObj bean,接口中所有的Object类的方法也会直接委托给proxy来执行
     * 或者委托给类型兼容并标记有{@link RuntimeMethod}注解的方法。
     * <p>
     * proxyObj将注入原始方法
     * <p>
     * 例如:
     * <pre>{@code
     * Function func = ReflectUtil.newInterfaceImpl(Function.class, delegate);
     * }</pre>
     *
     * @param typeToken      接口类型
     * @param proxyTypeToken 代理类型
     * @param proxyObj       代理对象bean
     * @param <T>            接口类型
     * @param <P>            代理类型
     * @return 接口的动态实现
     * @throws AssertException 如果接口不是单方法接口且没有超类,或者proxyObj没有委托方法
     */
    @SuppressWarnings("unchecked")
    public static <T, P> T newInterfaceImpl(LiTypeToken<T> typeToken, LiTypeToken<P> proxyTypeToken, P proxyObj) {


        Class<T> interfaceType = (Class<T>) typeToken.getRawType();
        LiAssertUtil.assertTrue(interfaceType.isInterface(), IllegalArgumentException::new, "only support interface");

        Lira<Method> runtimeTypeMethods = ReflectUtil.getMethods(proxyObj.getClass())
                .filter(m -> ReflectUtil.getAnnotation(m, RuntimeMethod.class));


        Map<Method, ThrowableFunction<Object[], ?>> proxyMethodMap = Lira.of(interfaceType.getMethods())
                .filter(m -> !ModifierUtil.isStatic(m)) // 静态方法不需要代理
                .tuple(origin -> findProxyFunction(typeToken, proxyTypeToken, proxyObj, runtimeTypeMethods, origin))
                .toMap(m -> m);
        for (Method method : Object.class.getMethods()) {
            proxyMethodMap.put(method, args -> method.invoke(proxyObj, args));
        }
        return newProxyInstance(interfaceType.getClassLoader(), interfaceType, (proxy, method, args) -> proxyMethodMap.get(method).apply(args));
    }

    private static <T, P> ThrowableFunction<Object[], ?> findProxyFunction(LiTypeToken<T> typeToken, LiTypeToken<P> proxyTypeToken, P proxyObj, Lira<Method> runtimeTypeMethods, Method originMethod) {
        //优先查找相同签名的方法
        MethodSignature referenceMethodSignature = MethodSignature.non_strict(originMethod, typeToken);
        Method sameSignatureMethod = MethodUtil.getSameSignatureMethod(proxyTypeToken, referenceMethodSignature);
        if (sameSignatureMethod != null) {
            return args -> sameSignatureMethod.invoke(proxyObj, args);
        }

        //查找具有兼容性的请求参数和返回参数，且被RuntimeType注解的方法

        for (Method runtimeMethod : runtimeTypeMethods) {


            // 代理方法返回类型为实际类型子类或者为Object类
            if (ClassUtil.isAssignableFromOrIsWrapper(originMethod.getReturnType(), runtimeMethod.getReturnType())) {
                int stat = parameterTypeCovariant(originMethod, runtimeMethod);
                if (stat == 1) {
                    return args -> runtimeMethod.invoke(proxyObj, args);
                } else if (stat == 2) {
                    return args -> runtimeMethod.invoke(proxyObj, ArrayUtils.insert(args, 0, originMethod));
                }
            }

        }
        for (Method runtimeMethod : runtimeTypeMethods) {


            // 代理方法返回类型为Object类
            if (runtimeMethod.getReturnType() == Object.class) {
                int stat = parameterTypeCovariant(originMethod, runtimeMethod);
                if (stat == 1) {
                    return args -> runtimeMethod.invoke(proxyObj, args);
                } else if (stat == 2) {
                    return args -> runtimeMethod.invoke(proxyObj, ArrayUtils.insert(args, 0, originMethod));

                }
            }
        }
        throw new LiraRuntimeException("can not proxy method " + originMethod);
    }

    /**
     * 0 表示方法参数无继承关系
     * 1 表示方法参数位数相同，参数可用于被代理的方法
     * 2 表示方法参数位数比被代理方法多一个，且最后一个参数为Method，参数可用于被代理的方法
     */
    private static int parameterTypeCovariant(Method origin, Method runtimeMethod) {

        if (origin.getParameterCount() == runtimeMethod.getParameterCount()) {

            for (int i = 0; i < origin.getParameterTypes().length; i++) {

                // 代码方法的参数类型均为原方法的父类，这样就可以保证兼容性
                if (!ClassUtil.isAssignableFromOrIsWrapper(runtimeMethod.getParameterTypes()[i], origin.getParameterTypes()[i])) {
                    return 0;
                }
            }
            return 1;
        } else if (origin.getParameterCount() + 1 == runtimeMethod.getParameterCount()) {
            Class<?> firstParameterType = runtimeMethod.getParameterTypes()[0];
            if (firstParameterType == Method.class && firstParameterType.isAnnotationPresent(RuntimeParameter.class)) {

                for (int i = 0; i < origin.getParameterTypes().length; i++) {

                    // 代码方法的参数类型均为原方法的父类，这样就可以保证兼容性
                    if (!ClassUtil.isAssignableFromOrIsWrapper(runtimeMethod.getParameterTypes()[i + 1], origin.getParameterTypes()[i])) {
                        return 0;
                    }
                }

            }
            return 2;
        }
        return 0;
    }

    /**
     * @see UnwrapInvocationHandler
     * @see Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)
     */
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(ClassLoader loader, Class<T> _interface, InvocationHandler h) {
        return (T) Proxy.newProxyInstance(loader, new Class[]{_interface}, new UnwrapInvocationHandler(h));
    }
}
