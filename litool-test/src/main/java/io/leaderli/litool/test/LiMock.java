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
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * used to mock the method call
 */
public class LiMock {

    /**
     * it's used for mark method return void or the to be mocked bean
     */
    public static final Object SKIP = new Object() {
        @Override
        public String toString() {
            return "skip";
        }
    };


    public static final Map<Method, Function<Object[], Object[]>> methodValues = new HashMap<>();
    public static final Set<Class<?>> mockedClasses = new HashSet<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Method mockMethod;
    public static boolean mockProgress;
    public static LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
    public static final Set<Class<?>> redefineClassesInMockInit = new HashSet<>();

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
        methodValues.clear();
        mockMethod = null;
        mockProgress = false;
        instanceCreators.clear();
        redefineClassesInMockInit.clear();
    }


    public static void ignoreTypeInitError(Class<?> mockingClass) {


        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];

        LiAssertUtil.assertTrue("<clinit>".equals(caller.getMethodName()), "only support call in <clinit>");
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
     * delegate the class method to {@link  MockInitAdvice}
     *
     * @param mockingClass the class will be redefined by byteBuddy
     */
    public static void mock(Class<?> mockingClass) {


        LiAssertUtil.assertFalse(mockedClasses.contains(mockingClass), "duplicate mock " + mockingClass);
        redefineClassesInMockInit.add(mockingClass);


        byteBuddy.redefine(mockingClass)
                .visit(Advice.to(MockInitAdvice.class).on(MethodDescription::isMethod))
                .visit(Advice.to(ConstructorAdvice.class).on(MethodDescription::isConstructor))
                .make()
                .load(mockingClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

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
        mockProgress = true;
        runnable.run();

        Objects.requireNonNull(mockMethod);
        Class<?> returnType = TypeUtil.erase(TypeUtil.resolve(mockMethod.getDeclaringClass(), mockMethod.getGenericReturnType()));
        PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);

        Object zero_value = primitiveEnum == PrimitiveEnum.VOID || primitiveEnum == PrimitiveEnum.OBJECT ? SKIP : primitiveEnum.zero_value;
        methodValues.put(mockMethod, params -> new Object[]{zero_value});
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        mockProgress = false;
    }


    @SafeVarargs
    public static <T> void when(Supplier<T> supplier, T... mockValues) {
        whenArgs(supplier, params -> mockValues);
    }

    public static <T> void whenArgs(Supplier<T> supplier, Function<Object[], Object[]> mockValuesProvideByParams) {


        mockMethod = null;
        mockProgress = true;
        supplier.get();

        Objects.requireNonNull(mockMethod);
        methodValues.put(mockMethod, mockValuesProvideByParams);
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        mockProgress = false;
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
                .throwable_map(cls -> Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors())
                .toLira(PropertyDescriptor.class);

        // mock run pojo set get achieve test coverage
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Object value = ReflectUtil.invokeMethod(propertyDescriptor.getReadMethod(), instance).get();
            ReflectUtil.invokeMethod(propertyDescriptor.getWriteMethod(), instance, value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T mockInterface(Type context, Class<T> type) {

        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, origin, args) -> {
            if (MethodUtil.notObjectMethod(origin)) {

                if (mockProgress) {
                    mockMethod = origin;
                    return null;
                }
                Object value = LiTestTemplateInvocationContext.TemplateInvocationMockMethodAdvice.methodValue.get(origin);
                Class<?> returnType = origin.getReturnType();
                if (value == LiMock.SKIP) {

                    if (returnType == void.class) {
                        return null;
                    } else {
                        Type type1 = TypeUtil.resolve(context, origin.getGenericReturnType());
                        value = MockBean.mockBean(type1);
                    }
                } else {

                    PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);
                    if (value == null && primitiveEnum != PrimitiveEnum.OBJECT) {
                        value = primitiveEnum.zero_value;
                    }
                }

                return value;
            }
            return origin.invoke(NONE, args);
        });
    }


    public static class MockStaticBlock {
        @Advice.OnMethodExit(onThrowable = Throwable.class)
        @SuppressWarnings("all")
        public static void enter(@Advice.Thrown(readOnly = false) Throwable th) {
            th = null;
        }
    }

    public static final Object NONE = new Object();


    public static class MockInitAdvice {
        /**
         * used to record the mocking class method has be called, and mark method potention return values by {@link  #when(Supplier, Object[])}
         * or disable some void-method call by {@link  #light(Runnable)}
         * <p>
         * this is a delegate for all method of mockindg class, will ignore actual inovation of all methods.
         * to prevent call actual method on the void-method, return a meaningless {@link #SKIP}.
         *
         * @param origin the origin method of mocking class
         * @return return a meaningless {@link #SKIP}
         */
        @SuppressWarnings("all")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin) {
            if (mockProgress) {
                mockMethod = origin;
            }
            return SKIP;
        }
    }

}
