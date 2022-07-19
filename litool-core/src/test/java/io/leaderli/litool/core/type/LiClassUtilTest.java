package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.LiAssertException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/17
 */
class LiClassUtilTest {
    @Test
    public void test() throws Throwable {


        Assertions.assertDoesNotThrow(() -> {
            Class<CharSequence> narrow = LiClassUtil.getClass("123");
        });
        Assertions.assertThrows(NullPointerException.class, () -> {

            Class<CharSequence> narrow = LiClassUtil.getClass(null);
        });


    }

    @Test
    void box() {

        int a = 1;
        Assertions.assertEquals(Integer.class, LiClassUtil.box(a).getClass());
        Object obj = a;
        Assertions.assertEquals(Integer.class, LiClassUtil.box(obj).getClass());

    }

    @Test
    public void narrow() {


        Assertions.assertDoesNotThrow(() -> {
            Class<CharSequence> narrow = LiClassUtil.narrow(String.class);
        });
    }

    @Test
    public void primitiveToWrapper() {
        Assertions.assertNull(LiClassUtil.primitiveToWrapper(null));
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(int.class), Integer.class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(void.class), Void.class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(String.class), String.class);

        Assertions.assertSame(LiClassUtil.primitiveToWrapper(int[].class), int[].class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(int[][].class), int[][].class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(Integer[].class), Integer[].class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(Integer[][].class), Integer[][].class);
        Assertions.assertSame(LiClassUtil.primitiveToWrapper(String[].class), String[].class);

    }

    @Test
    public void wrapperToPrimitive() {
        Assertions.assertNull(LiClassUtil.wrapperToPrimitive(null));
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(Integer.class), int.class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(Void.class), void.class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(String.class), String.class);

        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(int[].class), int[].class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(int[][].class), int[][].class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(Integer[].class), Integer[].class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(Integer[][].class), Integer[][].class);
        Assertions.assertSame(LiClassUtil.wrapperToPrimitive(String[].class), String[].class);

    }

    @Test
    public void isAssignableFromOrIsWrapper() {

        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(null, null));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(null, String.class));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(String.class, null));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(String.class, CharSequence.class));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(String[].class, CharSequence.class));

        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(int.class, Integer.class));
        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(Integer.class, int.class));
        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(CharSequence.class, String.class));
        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(CharSequence[].class, String[].class));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(int[].class, Integer[].class));
        Assertions.assertFalse(LiClassUtil.isAssignableFromOrIsWrapper(int[][].class, Integer[][].class));
        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(int[].class, int[].class));


        Assertions.assertFalse(int[].class.isAssignableFrom(Integer[].class));
        Assertions.assertTrue(Integer[].class.isAssignableFrom(Integer[].class));
        Assertions.assertFalse(Integer[].class.isAssignableFrom(int[].class));


    }

    @Test
    public void getArrayClass() {

        Assertions.assertSame(LiClassUtil.getArrayClass(int.class), int[].class);
        Assertions.assertSame(LiClassUtil.getArrayClass(Integer.class), Integer[].class);
        Assertions.assertSame(LiClassUtil.getArrayClass(int[].class), int[][].class);
        Assertions.assertThrows(NullPointerException.class, () -> LiClassUtil.getArrayClass(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            LiClassUtil.getArrayClass(void.class);
            LiClassUtil.getArrayClass(Void.class);
        });
    }

    @Test
    public void getAppJars() {

        Assertions.assertTrue("file:/".matches("^[^/]++/$"));
        Assertions.assertTrue("jar:file:/".matches("^[^/]++/$"));
        Assertions.assertFalse("/jar/".matches("^[^/]++/$"));
        Assertions.assertTrue(LiClassUtil.getAppJars().size() > 0);


        System.out.println(Arrays.toString(System.getProperty("java.class.path").split(System.getProperty("path.separator"))));

    }


    @Test
    public void newArray() {

        Assertions.assertSame(Integer[].class, LiClassUtil.newArray(Integer.class, 0).getClass());
        Assertions.assertSame(Integer[].class, LiClassUtil.newArray(int.class, 0).getClass());
        Assertions.assertSame(int[][].class, LiClassUtil.newArray(int[].class, 0).getClass());
        Assertions.assertSame(Integer[][].class, LiClassUtil.newArray(Integer[].class, 0).getClass());

        CharSequence[] strings = LiClassUtil.newArray(String.class, 1);
        Assertions.assertEquals(1, strings.length);

        Assertions.assertTrue(LiClassUtil.isAssignableFromOrIsWrapper(CharSequence[].class, LiClassUtil.newArray(String.class, 0).getClass()));


        Assertions.assertThrows(NullPointerException.class, () -> LiClassUtil.newArray(null, 0));


        Assertions.assertNull(LiClassUtil.newArray(null));
        Assertions.assertNull(LiClassUtil.newArray(1));
        Assertions.assertEquals(1, LiClassUtil.newArray(new int[]{1})[0]);
        Assertions.assertEquals(1, LiClassUtil.newArray(new Integer[]{1})[0]);

    }


    @Test
    public void cast() {

        Object a = "123";
        Assertions.assertEquals("123", LiClassUtil.cast(a, String.class));
        Assertions.assertEquals("123", LiClassUtil.cast(a, CharSequence.class));
        Assertions.assertNull(LiClassUtil.cast(a, int.class));


        a = 1;
        Assertions.assertSame(Integer.class, a.getClass());
        Assertions.assertSame(Integer.class, LiClassUtil.cast(a, Integer.class).getClass());
        Assertions.assertSame(Integer.class, LiClassUtil.cast(a, int.class).getClass());

        Assertions.assertSame(Integer.class, LiClassUtil.cast(a, Integer.class).getClass());
        Assertions.assertSame(Integer.class, LiClassUtil.cast(a, int.class).getClass());

        a = new int[]{1};

        Assertions.assertSame(int[].class, LiClassUtil.cast(a, int[].class).getClass());
        Assertions.assertNull(LiClassUtil.cast(a, Integer[].class));
        Assertions.assertEquals(1, LiClassUtil.cast(a, int[].class)[0]);

        a = new String[]{"1"};

        Assertions.assertNull(LiClassUtil.cast(a, Integer[].class));
        Assertions.assertSame(String[].class, LiClassUtil.cast(a, String[].class).getClass());
        CharSequence[] cs = LiClassUtil.cast(a, CharSequence[].class);
        Assertions.assertEquals(1, cs.length);
        Object[] cast = LiClassUtil.cast(a, Object[].class);
        Assertions.assertEquals("1", cast[0]);
    }


    @Test
    public void filterCanCastMap() {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("1", "1");
        map.put(2, 2);
        Assertions.assertEquals(1, LiClassUtil.filterCanCast(map, String.class, String.class).size());
        Assertions.assertEquals(1, LiClassUtil.filterCanCast(map, int.class, int.class).size());
        Assertions.assertEquals(0, LiClassUtil.filterCanCast(map, String.class, int.class).size());

        Assertions.assertDoesNotThrow(() -> {
            Map<CharSequence, Number> actual = LiClassUtil.filterCanCast(map, String.class, int.class);
        });
    }

    @Test
    void addInterface() {

        Assertions.assertThrows(LiAssertException.class, () -> LiClassUtil.addInterface(Fuck.class, new Proxy()));
        Assertions.assertThrows(LiAssertException.class, () -> LiClassUtil.addInterface(Proxy.class, new Proxy()));
        Assertions.assertThrows(LiAssertException.class, () -> LiClassUtil.addInterface(Function.class, new Proxy()));


        LiClassUtil.addInterface(Runnable.class, 1);

        MyFunction function = LiClassUtil.addInterface(MyFunction.class, new Proxy());

        Assertions.assertSame(123, function.apply("123"));

    }

    public interface Fuck extends Function<String, Integer> {
        @Override
        Integer apply(String s);
    }

    interface MyFunction {

        Integer apply(String value);
    }

    public static class Proxy {

        public Integer apply(String s) {
            return Integer.valueOf(s);
        }

        public Object apply(Object s) {
            return s;
        }

    }

}
