package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.ReflectUtil;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author leaderli
 * @since 2022/9/30 5:16 PM
 */
public class CartesianTestAssert {

    public static final Set<Class<?>> assertClasses = new HashSet<>();
    public static final Map<Method, LiTuple<Object[], Object>> assert_method_call_records = new HashMap<>();
    public static Method assertMethod;
    public static Object assertObj;
    public static boolean staticRecord;

    @SuppressWarnings("unchecked")
    public static <T> T recording(Class<T> assertClass) {

        LiAssertUtil.assertFalse(assertClasses.contains(assertClass), "duplicate record class");

        CartesianMock.byteBuddy.redefine(assertClass)
                .visit(Advice.to(RecordAdvice.class).on(MethodDescription::isMethod))
                .make()
                .load(assertClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        assertClasses.add(assertClass);
        assertObj = ReflectUtil.newInstance(assertClass).get();
        return (T) assertObj;
    }

    public static void reset() {
        assertClasses.clear();
        assert_method_call_records.clear();
    }

    public static void assertReturn(Runnable methodCall, Object expectRet) {
        assertion(methodCall, (args, ret) -> Assertions.assertEquals(expectRet, ret));
    }

    public static void assertion(Runnable methodCall, BiConsumer<Object[], Object> assertLambda) {
        methodCall(methodCall);
        LiTuple<Object[], Object> argsReturn = assert_method_call_records.get(assertMethod);
        Objects.requireNonNull(argsReturn);
        assertLambda.accept(argsReturn._1, argsReturn._2);
    }

    public static void assertArgs(Runnable methodCall, Object... expectArgs) {
        assertion(methodCall, (args, ret) -> Assertions.assertArrayEquals(expectArgs, args));
    }

    private static void methodCall(Runnable methodCall) {

        staticRecord = true;
        methodCall.run();
        staticRecord = false;
    }

    public static void assertCalled(Runnable methodCall) {
        methodCall(methodCall);
        Assertions.assertNotNull(assert_method_call_records.get(assertMethod));
    }


    public static void assertNotCalled(Runnable methodCall) {
        methodCall(methodCall);
        Assertions.assertNull(assert_method_call_records.get(assertMethod));
    }


    public static class RecordAdvice {


        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin,
                                   @Advice.This(optional = true) Object _this) {
            if (assertObj == _this || _this == null && staticRecord) {
                assertMethod = origin;
                return CartesianMock.SKIP;
            }
            return null;
        }

        @SuppressWarnings("all")
        @Advice.OnMethodExit
        public static void exit(
                @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object _return,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] args,
                @Advice.This(optional = true) Object _this) {

            if (assertObj == _this || _this == null && staticRecord) {
                return;
            }

            LiTuple<Object[], Object> argsReturn = LiTuple.of(args, _return);
            assert_method_call_records.put(origin, argsReturn);
        }
    }

}
