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
        Lino<Method> same = MethodUtil.getSameSignatureMethod(this, method);
        Assertions.assertEquals("123", same.throwable_map(m -> m.invoke(this)).get());

        method = Runnable.class.getMethod("run");
        same = MethodUtil.getSameSignatureMethod(this, method);
        Assertions.assertTrue(same.present());


        method = this.getClass().getDeclaredMethod("run");
        same = MethodUtil.getSameSignatureMethod(Runnable.class, method);
        Assertions.assertTrue(same.present());

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

        Assertions.assertTrue(MethodUtil.findMethod(MethodUtilBean.class, new MethodSignature("test")).present());

    }

    @Test
    void notObjectMethod() throws NoSuchMethodException {

        Assertions.assertTrue(MethodUtil.notObjectMethod(this.getClass().getDeclaredMethod("notObjectMethod")));
        Assertions.assertFalse(MethodUtil.notObjectMethod(this.getClass().getMethod("notify")));
    }

    @Test
    void belongsTo() throws NoSuchMethodException {
        Assertions.assertTrue(MethodUtil.belongsTo(this.getClass().getMethod("notify"), Object.class));
        Assertions.assertFalse(MethodUtil.belongsTo(this.getClass().getDeclaredMethod("belongsTo"), Object.class));
    }

    @Test
    void methodOfRepeatableContainer() throws NoSuchMethodException {

        Assertions.assertTrue(MethodUtil.methodOfRepeatableContainer(NotNulls.class.getMethod("value")));
        Assertions.assertFalse(MethodUtil.methodOfRepeatableContainer(NotNulls.class.getMethod("toString")));
    }
}
