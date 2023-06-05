package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/26 7:07 AM
 */
class MethodUtilTest {

    @Test
    void getSameSignatureMethod() throws NoSuchMethodException {
        Method method = Object.class.getMethod("toString");
        Method same = MethodUtil.getSameSignatureMethod(LiTypeToken.of(MethodUtilTest.class), method);

        Assertions.assertEquals("123", ReflectUtil.invokeMethod(same, this).get());

        method = Runnable.class.getMethod("run");
        same = MethodUtil.getSameSignatureMethod(LiTypeToken.of(MethodUtilTest.class), method);
        Assertions.assertNotNull(same);


        method = this.getClass().getDeclaredMethod("run");
        same = MethodUtil.getSameSignatureMethod(LiTypeToken.of(Runnable.class), method);
        Assertions.assertNotNull(same);
        method = this.getClass().getMethod("apply", String.class);

        same = MethodUtil.getSameSignatureMethod(new LiTypeToken<Function<String, Integer>>() {
        }, method);
        Assertions.assertNotNull(same);

        Assertions.assertNotNull(MethodUtil.getSameSignatureMethod(LiTypeToken.of(MethodUtilBean.class), new MethodSignature("test")));

    }

    @SuppressWarnings("Convert2Lambda")
    @Test
    void test() throws NoSuchMethodException {

        Method method = this.getClass().getMethod("apply", String.class);
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return null;
            }
        };
        System.out.println(MethodUtil.getSameSignatureMethod(
                new LiTypeToken<Function<String, Integer>>() {
                }
                , method));
    }

    public Integer apply(String msg) {
        return 123;
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
    void shortString() throws NoSuchMethodException {

        Method method = this.getClass().getMethod("apply", String.class);

        Assertions.assertEquals("MethodUtilTest#apply(String):Integer", MethodUtil.shortString(method));
        Assertions.assertEquals("MethodUtilTest#apply", MethodUtil.veryShortString(method));
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
