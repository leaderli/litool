package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.type.ReflectUtil;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

import static io.leaderli.litool.test.LiMock.NONE;

/**
 * @author leaderli
 * @since 2022/9/30 5:16 PM
 */
public class LiTestAssert {

    public static final Set<Class<?>> assertClasses = new HashSet<>();
    private static final Map<Method, LiTuple2<Object[], Object>> assert_method_call_records = new HashMap<>();
    public static Method assertMethod;
    public static Object assertObj;

    @SuppressWarnings("unchecked")
    public static <T> T recording(Class<T> assertClass) {

        LiAssertUtil.assertFalse(assertClasses.contains(assertClass), "duplicate record class");

        LiMock.byteBuddy.redefine(assertClass)
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
        methodCall.run();
        LiTuple2<Object[], Object> argsReturn = assert_method_call_records.get(assertMethod);
        Objects.requireNonNull(argsReturn);
        assertLambda.accept(argsReturn._1, argsReturn._2);
    }

    public static void assertArgs(Runnable methodCall, Object... expectArgs) {
        assertion(methodCall, (args, ret) -> Assertions.assertArrayEquals(expectArgs, args));
    }

    public static void assertCalled(Runnable methodCall) {
        methodCall.run();
        Assertions.assertNotNull(assert_method_call_records.get(assertMethod));
    }


    public static void assertNotCalled(Runnable methodCall) {
        methodCall.run();
        Assertions.assertNull(assert_method_call_records.get(assertMethod));
    }

    public static class RecordAdvice {

        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin, @Advice.This Object _this) {
            if (assertObj == _this) {
                assertMethod = origin;
                return NONE;
            }
            return null;
        }

        @SuppressWarnings("all")
        @Advice.OnMethodExit
        public static void exit(
                @Advice.Return Object _return,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] args,
                @Advice.This Object _this) {

            if (assertObj == _this) {
                return;
            }

            LiTuple2<Object[], Object> argsReturn = LiTuple.of(args, _return);
            assert_method_call_records.put(origin, argsReturn);
            assert_method_call_records.put(origin, argsReturn);

        }
    }

}
