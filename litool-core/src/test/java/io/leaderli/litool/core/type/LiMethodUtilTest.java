package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/6/26 7:07 AM
 */
class LiMethodUtilTest {

    @Test
    void getSameSignatureMethod() throws NoSuchMethodException {
        Method method = Object.class.getMethod("toString");
        Lino<Method> same = LiMethodUtil.getSameSignatureMethod(method, this);
        Assertions.assertEquals("123", same.throwable_map(m -> m.invoke(this)).get());

        method = Runnable.class.getMethod("run");
        same = LiMethodUtil.getSameSignatureMethod(method, this);
        Assertions.assertSame(Lino.none(), same);


        method = this.getClass().getDeclaredMethod("run");
        same = LiMethodUtil.getSameSignatureMethod(method, Runnable.class);
        Assertions.assertSame(Lino.none(), same);
    }

    @Override
    public String toString() {
        return "123";
    }

    protected void run() {

    }

    public void run(String age) {

    }
}
