package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.internal.ReflectionAccessor;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.meta.ra.LiraRuntimeException;
import io.leaderli.litool.core.meta.ra.NullableFunction;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/12
 */
public class ReflectUtil {

    private static final ReflectionAccessor REFLECTION_ACCESSOR = ReflectionAccessor.getInstance();

    /**
     * 可以获取指定类中的指定字段
     *
     * @param clazz 目标类
     * @param name  字段名
     * @return 该字段的Lino对象
     * @see #getField(Class, String, boolean) 第三个参数取 false
     */
    public static Lino<Field> getField(Class<?> clazz, String name) {


        return getField(clazz, name, false);

    }

    /**
     * 获取指定类中的指定字段
     *
     * @param clazz            目标类
     * @param name             字段名
     * @param onlyCurrentClass 是否只在当前类中查找字段
     * @return 该字段的Lino对象
     */
    public static Lino<Field> getField(Class<?> clazz, String name, boolean onlyCurrentClass) {


        return getFields(clazz)
                .filter(f -> f.getName().equals(name))
                .filter(f -> !onlyCurrentClass || f.getDeclaringClass().equals(clazz))
                .first();

    }

    /**
     * 获取类及其父类的所有字段， JVM自动生成的字段{@link  Field#isSynthetic()}不会包含在返回结果中
     *
     * @param clazz 要获取字段的类
     * @return 类及其父类的所有字段
     * @see Class#getFields()
     * @see Class#getDeclaredFields()
     */
    public static Lira<Field> getFields(Class<?> clazz) {

        if (clazz == null) {
            return Lira.none();
        }

        return CollectionUtils.union(Field.class, clazz.getFields(), clazz.getDeclaredFields()).filter(f -> !f.isSynthetic());
    }


    /**
     * 可以获取对象中的指定字段的值
     *
     * @param obj  目标对象
     * @param name 字段名称
     * @return 返回字段对应的值，如果不存在，则返回 {@link Lino#none()}
     * @see #getFieldValue(Object, String, boolean) 第三个字段去false
     */
    public static Lino<?> getFieldValue(Object obj, String name) {


        return getFieldValue(obj, name, false);
    }


    /**
     * 可以获取对象中的指定字段的值
     *
     * @param obj              目标对象
     * @param name             字段名称
     * @param onlyCurrentClass 是否只在对象的当前类中寻找字段
     * @return 返回字段对应的值，如果不存在，则返回 {@link Lino#none()}
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
        return Lino.throwable_of(() -> field.get(object));
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


    /**
     * 设置对象的属性值
     *
     * @param obj   目标对象
     * @param name  属性名
     * @param value 属性值
     * @return 是否设置成功
     * @see #getField(Class, String, boolean)
     * @see #setFieldValue(Object, String, Object, boolean) 第三个参数取false
     */
    public static boolean setFieldValue(Object obj, String name, Object value) {

        return setFieldValue(obj, name, value, false);
    }


    /**
     * 设置对象的属性值
     *
     * @param obj              目标对象
     * @param name             属性名
     * @param value            属性值
     * @param onlyCurrentClass 是否只查找当前类
     * @return 是否设置成功
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
        if (cls.isPrimitive()) { // 基础类型isAbstract
            return (Lino<T>) Lino.of(PrimitiveEnum.get(cls).zero_value);
        }
        if (cls.isInterface() || ModifierUtil.isAbstract(cls) || cls.isEnum()) {
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
        return Lino.throwable_of(() -> constructor.newInstance(args));
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
     * onlyCurrentClass = false
     *
     * @param cls  类
     * @param name 方法名
     * @return {@link #getMethod(Class, String, boolean)}
     */
    public static Lino<Method> getMethod(Class<?> cls, String name) {
        return getMethod(cls, name, false);
    }

    /**
     * 获取类包含的方法线
     *
     * @param cls              类
     * @param name             方法名
     * @param onlyCurrentClass 是否仅在cls中查找,不在超类中查找
     * @return 类包含的方法线
     */
    public static Lino<Method> getMethod(Class<?> cls, String name, boolean onlyCurrentClass) {

        return getMethods(cls)
                .filter(m -> m.getName().equals(name))
                .filter(m -> !onlyCurrentClass || m.getDeclaringClass().equals(cls))
                .first();
    }

    /**
     * 获取类或其超类的所有方法
     *
     * @param cls 类
     * @return 类或其超类的所有方法
     * @see Class#getMethods()
     * @see Class#getDeclaredMethods()
     */
    public static Lira<Method> getMethods(Class<?> cls) {
        if (cls == null) {
            return Lira.none();
        }
        return CollectionUtils.union(Method.class, cls.getMethods(), cls.getDeclaredMethods()).filter(m -> !m.isSynthetic());
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

        return Lino.throwable_of(() -> method.invoke(obj, args));
    }

    /**
     * 使用{@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}动态创建接口的实现。
     * 接口中的原始成员方法将委托给具有相同{@link MethodSignature}的proxyObj bean,接口中所有的Object类的方法也会直接委托给proxy来执行
     * 或者委托给类型兼容并标记有{@link RuntimeType}注解的方法。
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
                .filter(m -> ReflectUtil.getAnnotation(m, RuntimeType.class));
        Map<Method, Method> proxyMethodMap = Lira.of(interfaceType.getMethods())
                .filter(m -> !ModifierUtil.isStatic(m)) // 静态方法不需要代理
                .tuple(origin -> findProxyMethod(typeToken, proxyTypeToken, runtimeTypeMethods, origin))
                .toMap(m -> m);
        for (Method method : Object.class.getMethods()) {
            proxyMethodMap.put(method, method);
        }
        InvocationHandler invocationHandler = (proxy, method, args) -> proxyMethodMap.get(method).invoke(proxyObj, args);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, invocationHandler);

    }

    private static <T, P> Method findProxyMethod(LiTypeToken<T> typeToken, LiTypeToken<P> proxyTypeToken, Lira<Method> runtimeTypeMethods, Method originMethod) {
        //优先查找相同签名的方法
        MethodSignature referenceMethodSignature = MethodSignature.non_strict(originMethod, typeToken);
        Method sameSignatureMethod = MethodUtil.getSameSignatureMethod(proxyTypeToken, referenceMethodSignature);
        if (sameSignatureMethod != null) {
            return sameSignatureMethod;
        }

        // 查询具有相同请求参数和返回参数，且被 RuntimeType注解的方法
        for (Method runtimeMethod : runtimeTypeMethods) {

            MethodSignature runtimeMethodSignature = MethodSignature.non_strict(runtimeMethod, proxyTypeToken);
            if (referenceMethodSignature.returnType == runtimeMethodSignature.returnType
                    && Arrays.equals(referenceMethodSignature.parameterTypes, runtimeMethodSignature.parameterTypes)) {
                return runtimeMethod;
            }
        }

        //查找具有兼容性的请求参数和返回参数，且被RuntimeType注解的方法

        for (Method runtimeMethod : runtimeTypeMethods) {


            // 代理方法返回类型为实际类型子类或者为Object类
            if (ClassUtil.isAssignableFromOrIsWrapper(runtimeMethod.getReturnType(), originMethod.getReturnType()) && parameterTypeCovariant(originMethod, runtimeMethod)) {
                return runtimeMethod;
            }

        }
        for (Method runtimeMethod : runtimeTypeMethods) {


            // 代理方法返回类型为Object类
            if (runtimeMethod.getReturnType() == Object.class && parameterTypeCovariant(originMethod, runtimeMethod)) {
                return runtimeMethod;
            }

        }
        throw new LiraRuntimeException("can not proxy method " + originMethod);
    }

    private static boolean parameterTypeCovariant(Method origin, Method runtimeMethod) {

        if (origin.getParameterCount() == runtimeMethod.getParameterCount()) {

            for (int i = 0; i < origin.getParameterTypes().length; i++) {

                // 代码方法的参数类型均为原方法的父类，这样就可以保证兼容性
                if (!ClassUtil.isAssignableFromOrIsWrapper(runtimeMethod.getParameterTypes()[i], origin.getParameterTypes()[i])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
