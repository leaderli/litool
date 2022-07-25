package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/6/26 7:07 AM
 */
class MethodUtilTest {

    @Test
    void getSameSignatureMethod() throws NoSuchMethodException {
        Method method = Object.class.getMethod("toString");
        Lino<Method> same = MethodUtil.getSameSignatureMethod(method, this);
        Assertions.assertEquals("123", same.throwable_map(m -> m.invoke(this)).get());

        method = Runnable.class.getMethod("run");
        same = MethodUtil.getSameSignatureMethod(method, this);
        Assertions.assertSame(Lino.none(), same);


        method = this.getClass().getDeclaredMethod("run");
        same = MethodUtil.getSameSignatureMethod(method, Runnable.class);
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

    @Test
    void findMethod() {

        Assertions.assertTrue(MethodUtil.findMethod(MethodUtilBean.class, "test", void.class).present());
        Assertions.assertTrue(MethodUtil.findMethod(MethodUtil.class, "findMethod", Lino.class, Class.class, String.class).present());
        Assertions.assertTrue(MethodUtil.findMethod(MethodUtil.class, "findMethod", Lino.class, Class.class, String.class, Class.class, Class[].class).present());
        Assertions.assertTrue(MethodUtil.findMethod(this.getClass(), "findMethod").present());

    }
}
