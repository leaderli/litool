package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * used to mock the method call
 */
public class LiMock {

    /**
     * it's used for mark method return void or the bean that had be mocked or mocking
     */
    public static final Object SKIP = new Object() {
        @Override
        public String toString() {
            return "skip";
        }
    };


    /**
     * 基于参数的匹配的返回值的集合
     */
    public static final Map<Method, Function<Object[], Object[]>> methodValuesDependsOnParametersOfTestMethod = new HashMap<>();
    /**
     * 被mock 的类
     */
    public static final Set<Class<?>> mockedClasses = new HashSet<>();
    /**
     *
     */
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    /**
     * mock过程中重新定义的类
     */
    public static final Set<Class<?>> redefineClassesInMockInit = new HashSet<>();
    /**
     * 作为标记使用
     */
    public static final Object NONE = new Object();
    /**
     * mock的方法
     */
    public static Method mockMethod;
    /**
     * 当前处于mock的过程中
     */
    public static boolean onMockProgress;

    static {
        ByteBuddyAgent.install();
    }

    /**
     * reset the mock
     */
    public static void reset() {
        if (!mockedClasses.isEmpty()) {
            for (Class<?> mockedClass : mockedClasses) {
                byteBuddy.redefine(mockedClass)
                        .make()
                        .load(mockedClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }
        }
        mockedClasses.clear();
        methodValuesDependsOnParametersOfTestMethod.clear();
        mockMethod = null;
        onMockProgress = false;
        instanceCreators.clear();
        redefineClassesInMockInit.clear();
    }
    /**
     * 缓存的构造函数
     */
    public static LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    /**
     * delegate the class method to {@link  MockInitAdvice}
     *
     * @param mockingClass the class will be redefined by byteBuddy
     * @see MockInitAdvice
     * @see ConstructorAdvice
     */
    public static void mock(Class<?> mockingClass) {

        LiAssertUtil.assertFalse(mockingClass.isArray() || mockingClass.isInterface(), "not support interface or arr");
        LiAssertUtil.assertFalse(mockingClass == HashMap.class || mockingClass == ArrayList.class, "not support HashMap or ArrayList, you can use MockMap or MockList");

        LiAssertUtil.assertFalse(mockedClasses.contains(mockingClass), "duplicate mock " + mockingClass);
        redefineClassesInMockInit.add(mockingClass);


        byteBuddy.redefine(mockingClass)
                .visit(Advice.to(MockInitAdvice.class).on(MethodDescription::isMethod))
                .visit(Advice.to(ConstructorAdvice.class).on(MethodDescription::isConstructor))
                .make()
                .load(mockingClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

    }

    /**
     * @param supplier the method call
     * @see #light(Runnable)
     */
    public static void light(Supplier<?> supplier) {

        light((Runnable) supplier::get);
    }

    /**
     * support void,primitive,pojo returnType mock, auto generate one element of method returnValue
     *
     * @param runnable the method call
     * @see MockBean#create()
     * @see PrimitiveEnum#zero_value
     */
    public static void light(Runnable runnable) {

        mockMethod = null;
        onMockProgress = true;
        runnable.run();

        Objects.requireNonNull(mockMethod);
        Class<?> returnType = TypeUtil.erase(TypeUtil.resolve(mockMethod.getDeclaringClass(), mockMethod.getGenericReturnType()));
        PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);

        Object zero_value = primitiveEnum == PrimitiveEnum.VOID || primitiveEnum == PrimitiveEnum.OBJECT ? SKIP : primitiveEnum.zero_value;
        methodValuesDependsOnParametersOfTestMethod.put(mockMethod, params -> new Object[]{zero_value});
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        onMockProgress = false;
    }


    /**
     * @param supplier   the mock method call progress
     * @param mockValues the mock method potential return values
     * @param <T>        the  mock method return type
     */
    @SafeVarargs
    public static <T> void when(Supplier<T> supplier, T... mockValues) {
        whenArgs(supplier, params -> mockValues);
    }

    /**
     * @param supplier                  the mock method call progress
     * @param mockValuesProvideByParams the mock method potential return values on specific test parameters
     * @param <T>                       the  mock method return type
     */
    public static <T> void whenArgs(Supplier<T> supplier, Function<Object[], Object[]> mockValuesProvideByParams) {


        mockMethod = null;
        onMockProgress = true;
        supplier.get();

        Objects.requireNonNull(mockMethod);
        methodValuesDependsOnParametersOfTestMethod.put(mockMethod, mockValuesProvideByParams);
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        onMockProgress = false;
    }

    /**
     * @param mockingClass mock的类
     */
    public static void ignoreTypeInitError(Class<?> mockingClass) {


        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        LiAssertUtil.assertTrue(MethodUtil.CLINIT_METHOD_NAME.equals(caller.getMethodName()), "only support call in <clinit>");
        byteBuddy.redefine(mockingClass)
                .visit(Advice.to(MockInitAdvice.class).on(MethodDescription::isMethod))
                .visit(Advice.to(MockStaticBlock.class).on(MethodDescription::isTypeInitializer))
                .visit(Advice.to(ConstructorAdvice.class).on(MethodDescription::isConstructor))
                .make()
                .load(mockingClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        try {
            Class.forName(mockingClass.getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * if  instance class is same as type, run all get and set method of  type. will ignore
     * Object method
     *
     * @param type     the cls
     * @param instance the instance
     * @see BeanInfo#getPropertyDescriptors()
     */
    public static void runGetSet(Class<?> type, Object instance) {
        Lira<PropertyDescriptor> propertyDescriptors = Lino.of(instance)
                .map(Object::getClass)
                .filter(c -> {
                    if (c == Object.class || c.isArray()) {
                        return false;
                    }
                    return c == TypeUtil.erase(type);
                })
                .mapIgnoreError(cls -> Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors())
                .toLira(PropertyDescriptor.class);

        // mock run pojo set get achieve test coverage
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value = ReflectUtil.invokeMethod(propertyDescriptor.getReadMethod(), instance).get();
            ReflectUtil.invokeMethod(propertyDescriptor.getWriteMethod(), instance, value);
        }
    }

    /**
     * mock静态代码块
     */
    public static class MockStaticBlock {
        @Advice.OnMethodExit(onThrowable = Throwable.class)
        @SuppressWarnings("all")
        public static void enter(@Advice.Thrown(readOnly = false) Throwable th) {
            th = null;
        }
    }


}
