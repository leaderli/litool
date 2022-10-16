package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.type.MethodUtil;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author leaderli
 * @since 2022/9/30 5:16 PM
 */
public class LiTestAssert {

    public static final Set<Class<?>> recordClasses = new HashSet<>();

    private static final Map<String, LiTuple2<Object[], Object>> method_call_history = new HashMap<>();


    public static void recording(Class<?> recordClass) {

        LiAssertUtil.assertFalse(recordClasses.contains(recordClass), "duplicate record class");

        LiMock.byteBuddy.redefine(recordClass)
                .visit(Advice.to(RecordAdvice.class).on(MethodDescription::isMethod))
                .make()
                .load(recordClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        recordClasses.add(recordClass);
    }

    public static void reset() {
        recordClasses.clear();
        method_call_history.clear();
    }

    public static void assertReturn(String shortName, Object expectRet) {
        assertion(shortName, (args, ret) -> Assertions.assertEquals(expectRet, ret));
    }

    public static void assertion(String shortName, BiConsumer<Object[], Object> assertLambda) {
        LiTuple2<Object[], Object> argsReturn = method_call_history.get(shortName);
        Objects.requireNonNull(argsReturn);
        assertLambda.accept(argsReturn._1, argsReturn._2);
    }

    public static void assertArgs(String shortName, Object... expectArgs) {
        assertion(shortName, (args, ret) -> Assertions.assertArrayEquals(expectArgs, args));
    }

    public static void assertCalled(String shortName) {
        Assertions.assertNotNull(method_call_history.get(shortName));
    }

    public static void assertNotCalled(String shortName) {
        Assertions.assertNull(method_call_history.get(shortName));
    }

    public static class RecordAdvice {


        @SuppressWarnings("all")
        @Advice.OnMethodExit
        public static void exit(
                @Advice.Return Object _return,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] args) {

            LiTuple2<Object[], Object> argsReturn = LiTuple.of(args, _return);
            method_call_history.put(MethodUtil.veryShortString(origin), argsReturn);
            method_call_history.put(MethodUtil.shortString(origin), argsReturn);

        }
    }

}
