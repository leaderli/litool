package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/27 6:49 AM
 */
class MethodSignatureTest {

    @Test
    void same() throws NoSuchMethodException {
        Method notify_method = Object.class.getMethod("notify");

        MethodSignature no_strict_signature = new MethodSignature("notify");
        Assertions.assertTrue(no_strict_signature.equals(notify_method));

        MethodSignature strict_signature = MethodSignature.strict(notify_method);
        Assertions.assertFalse(strict_signature.equals(notify_method));

        Assertions.assertFalse(no_strict_signature.equals(Object.class.getMethod("notifyAll")));

        Method method = MyFunction.class.getMethod("apply", String.class);

        Assertions.assertEquals(new MethodSignature("apply", Integer.class, new Class[]{String.class}), MethodSignature.non_strict(method));
    }

    static class MyFunction implements Function<String, Integer> {

        @Override
        public Integer apply(String s) {
            return null;
        }
    }
}
