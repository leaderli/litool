package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.InstanceCreator;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;
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

    public static final Object NONE = new Object() {
        @Override
        public String toString() {
            return "void";
        }
    };


    public static final Map<Method, Function<Object[], Object[]>> methodValues = new HashMap<>();
    public static final Set<Class<?>> mockedClasses = new HashSet<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Method mockMethod;
    public static boolean mockProgress;
    public static LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

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
    }

    /**
     * delegate the class method to {@link  MockInitAdvice}
     *
     * @param mockingClass the class will be redefined by byteBuddy
     */
    public static void mock(Class<?> mockingClass) {


        LiAssertUtil.assertFalse(mockedClasses.contains(mockingClass), "duplicate mock " + mockingClass);

        byteBuddy.redefine(mockingClass)
                .visit(Advice.to(MockInitAdvice.class).on(MethodDescription::isMethod))
                .make()
                .load(mockingClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

    }

    /**
     * support void,primitive,pojo returnType mock, auto generate one element of method returnValue
     *
     * @param runnable the method call
     */
    public static void light(Runnable runnable) {

        mockMethod = null;
        mockProgress = true;
        runnable.run();

        Objects.requireNonNull(mockMethod);
        Class<?> returnType = TypeUtil.erase(TypeUtil.resolve(mockMethod.getDeclaringClass(), mockMethod.getGenericReturnType()));
        PrimitiveEnum primitiveEnum = PrimitiveEnum.get(returnType);

        Object zero_value;
        if (primitiveEnum == PrimitiveEnum.VOID) {
            zero_value = NONE;
            //            case OBJECT:
//                zero_value = ReflectUtil.newInstance(returnType).get();
//                break;
        } else {
            zero_value = primitiveEnum.zero_value;
        }
        Object finalZero_value = zero_value;
        methodValues.put(mockMethod, params -> new Object[]{finalZero_value});
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        mockProgress = false;
    }

    @SafeVarargs
    public static <T> void when(Supplier<T> runnable, T... mockValues) {
        whenArgs(runnable, params -> mockValues);
    }

    public static <T> void whenArgs(Supplier<T> runnable, Function<Object[], Object[]> mockValuesProvideByParams) {


        mockMethod = null;
        mockProgress = true;
        runnable.get();

        Objects.requireNonNull(mockMethod);
        methodValues.put(mockMethod, mockValuesProvideByParams);
        mockedClasses.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        mockProgress = false;
    }

    public static class MockInitAdvice {
        /**
         * used to record the mocking class method has be called, and mark method potention return values by {@link  #when(Supplier, Object[])}
         * or disable some void-method call by {@link  #light(Runnable)}
         * <p>
         * this is a delegate for all method of mockindg class, will ignore actual inovation of all methods.
         * to prevent call actual method on the void-method, return a meaningless {@link #NONE}.
         *
         * @param origin the origin method of mocking class
         * @return return a meaningless {@link #NONE}
         */
        @SuppressWarnings("all")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin) {
            if (mockProgress) {
                mockMethod = origin;
            }
            return NONE;
        }
    }

    public static Object mockBean(Type type) {
        Object instance = MockBean.instance(type, instanceCreators).create();
        Lino.of(TypeUtil.erase(type))
                .filter(c -> c == instance.getClass())
                .throwable_map(Introspector::getBeanInfo)
                .map(BeanInfo::getPropertyDescriptors)
                .toLira(PropertyDescriptor.class)
                .filter(p -> !StringUtils.equals(p.getName(), "class"))
                .forEach(p -> ReflectUtil.invokeMethod(p.getReadMethod(),instance).ifPresent(o -> ReflectUtil.invokeMethod(p.getWriteMethod(),o)));
        return instance;
    }
}
