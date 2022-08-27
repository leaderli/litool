package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/27 6:49 AM
 */
class MethodSignatureTest {

@Test
void same() throws NoSuchMethodException {
    MethodSignature notify = new MethodSignature("notify", void.class, new Class[]{});

    Assertions.assertTrue(notify.same(Object.class.getMethod("notify")));
    Assertions.assertFalse(notify.same(Object.class.getMethod("notifyAll")));
}
}
