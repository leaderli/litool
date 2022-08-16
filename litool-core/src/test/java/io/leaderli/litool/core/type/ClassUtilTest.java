package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/17
 */
class ClassUtilTest {
    @Test
    void testGetClass() {

        //noinspection ConstantConditions
        Assertions.assertNull(ClassUtil.getClass(null));

        Assertions.assertEquals(Integer.class, ClassUtil.getClass(1));
    }

    @Test
    void box() {

        int a = 1;
        Assertions.assertEquals(Integer.class, ClassUtil.box(a).getClass());
        Object obj = a;
        Assertions.assertEquals(Integer.class, ClassUtil.box(obj).getClass());

    }

    @Test
    public void narrow() {


        Assertions.assertDoesNotThrow(() -> {
            Class<CharSequence> narrow = ClassUtil.narrow(String.class);
        });
    }

    @Test
    public void primitiveToWrapper() {
        Assertions.assertNull(ClassUtil.primitiveToWrapper(null));
        Assertions.assertSame(ClassUtil.primitiveToWrapper(int.class), Integer.class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(void.class), Void.class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(String.class), String.class);

        Assertions.assertSame(ClassUtil.primitiveToWrapper(int[].class), int[].class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(int[][].class), int[][].class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(Integer[].class), Integer[].class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(Integer[][].class), Integer[][].class);
        Assertions.assertSame(ClassUtil.primitiveToWrapper(String[].class), String[].class);

    }

    @Test
    public void wrapperToPrimitive() {
        Assertions.assertNull(ClassUtil.wrapperToPrimitive(null));
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(Integer.class), int.class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(Void.class), void.class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(String.class), String.class);

        Assertions.assertSame(ClassUtil.wrapperToPrimitive(int[].class), int[].class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(int[][].class), int[][].class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(Integer[].class), Integer[].class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(Integer[][].class), Integer[][].class);
        Assertions.assertSame(ClassUtil.wrapperToPrimitive(String[].class), String[].class);

    }

    @Test
    public void isAssignableFromOrIsWrapper() {

        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(null, null));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(null, String.class));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(String.class, null));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(String.class, CharSequence.class));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(String[].class, CharSequence.class));

        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(int.class, Integer.class));
        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(Integer.class, int.class));
        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(CharSequence.class, String.class));
        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(CharSequence[].class, String[].class));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(int[].class, Integer[].class));
        Assertions.assertFalse(ClassUtil.isAssignableFromOrIsWrapper(int[][].class, Integer[][].class));
        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(int[].class, int[].class));


        Assertions.assertFalse(int[].class.isAssignableFrom(Integer[].class));
        Assertions.assertTrue(Integer[].class.isAssignableFrom(Integer[].class));
        Assertions.assertFalse(Integer[].class.isAssignableFrom(int[].class));


        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(List.class, ArrayList.class));


    }

    @Test
    public void _instanceof() {

        Assertions.assertFalse(ClassUtil._instanceof(null, null));
        Assertions.assertFalse(ClassUtil._instanceof(null, Integer.class));
        Assertions.assertFalse(ClassUtil._instanceof(1, null));

        Assertions.assertTrue(ClassUtil._instanceof(1, Integer.class));
        Assertions.assertTrue(ClassUtil._instanceof(1, int.class));
        Assertions.assertTrue(ClassUtil._instanceof(1, Number.class));

        Assertions.assertFalse(ClassUtil._instanceof(1, String.class));

        Assertions.assertTrue(ClassUtil._instanceof(new int[]{1}, int[].class));
        Assertions.assertFalse(ClassUtil._instanceof(new Integer[]{1}, int[].class));
        Assertions.assertFalse(ClassUtil._instanceof(new int[]{1}, Integer[].class));
        Assertions.assertTrue(ClassUtil._instanceof(new String[]{"1"}, CharSequence[].class));

    }

    @Test
    public void getArrayClass() {

        Assertions.assertSame(ClassUtil.getArrayClass(int.class), int[].class);
        Assertions.assertSame(ClassUtil.getArrayClass(Integer.class), Integer[].class);
        Assertions.assertSame(ClassUtil.getArrayClass(int[].class), int[][].class);
        Assertions.assertThrows(NullPointerException.class, () -> ClassUtil.getArrayClass(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {

            ClassUtil.getArrayClass(void.class);
            ClassUtil.getArrayClass(Void.class);
        });
    }

    @Test
    public void getAppJars() {

        Assertions.assertTrue("file:/".matches("^[^/]++/$"));
        Assertions.assertTrue("jar:file:/".matches("^[^/]++/$"));
        Assertions.assertFalse("/jar/".matches("^[^/]++/$"));
        Assertions.assertTrue(ClassUtil.getAppJars().size() > 0);

        Lira<String> a = ClassUtil.getJavaClassPaths();
        Lira<String> b = Lira.of(ClassUtil.getAppJars());


    }


    @Test
    public void newArray() {

        Assertions.assertSame(Integer[].class, ClassUtil.newArray(Integer.class, 0).getClass());
        Assertions.assertSame(Integer[].class, ClassUtil.newArray(int.class, 0).getClass());
        Assertions.assertSame(int[][].class, ClassUtil.newArray(int[].class, 0).getClass());
        Assertions.assertSame(Integer[][].class, ClassUtil.newArray(Integer[].class, 0).getClass());

        CharSequence[] strings = ClassUtil.newArray(String.class, 1);
        Assertions.assertEquals(1, strings.length);

        Assertions.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(CharSequence[].class, ClassUtil.newArray(String.class, 0).getClass()));


        Assertions.assertThrows(NullPointerException.class, () -> ClassUtil.newArray(null, 0));


        Assertions.assertNull(ClassUtil.newArray(null));
        Assertions.assertNull(ClassUtil.newArray(1));
        Assertions.assertEquals(1, ClassUtil.newArray(new int[]{1})[0]);
        Assertions.assertEquals(1, ClassUtil.newArray(new Integer[]{1})[0]);

    }


    @Test
    public void cast() {

        Object a = "123";
        Assertions.assertEquals("123", ClassUtil.cast(a, String.class));
        Assertions.assertEquals("123", ClassUtil.cast(a, CharSequence.class));
        Assertions.assertNull(ClassUtil.cast(a, int.class));


        a = 1;
        Assertions.assertSame(Integer.class, a.getClass());
        Assertions.assertSame(Integer.class, ClassUtil.cast(a, Integer.class).getClass());
        Assertions.assertSame(Integer.class, ClassUtil.cast(a, int.class).getClass());

        Assertions.assertSame(Integer.class, ClassUtil.cast(a, Integer.class).getClass());
        Assertions.assertSame(Integer.class, ClassUtil.cast(a, int.class).getClass());

        a = new int[]{1};

        Assertions.assertSame(int[].class, ClassUtil.cast(a, int[].class).getClass());
        Assertions.assertNull(ClassUtil.cast(a, Integer[].class));
        Assertions.assertEquals(1, ClassUtil.cast(a, int[].class)[0]);

        a = new String[]{"1"};

        Assertions.assertNull(ClassUtil.cast(a, Integer[].class));
        Assertions.assertSame(String[].class, ClassUtil.cast(a, String[].class).getClass());
        CharSequence[] cs = ClassUtil.cast(a, CharSequence[].class);
        Assertions.assertEquals(1, cs.length);
        Object[] cast = ClassUtil.cast(a, Object[].class);
        Assertions.assertEquals("1", cast[0]);


        Type genericInterface = Param.class.getGenericInterfaces()[0];

        Assertions.assertNull(Lino.of(String.class).cast(ParameterizedType.class).get());

    }

    @Test
    public void filterCanCastMap() {

        HashMap<Object, Object> map = new HashMap<>();
        map.put("1", "1");
        map.put(2, 2);
        Assertions.assertEquals(1, ClassUtil.filterCanCast(map, String.class, String.class).size());
        Assertions.assertEquals(1, ClassUtil.filterCanCast(map, int.class, int.class).size());
        Assertions.assertEquals(0, ClassUtil.filterCanCast(map, String.class, int.class).size());

        Assertions.assertDoesNotThrow(() -> {
            Map<CharSequence, Number> actual = ClassUtil.filterCanCast(map, String.class, int.class);
        });
    }

    @Test
    void addInterface() {

        Assertions.assertThrows(AssertException.class, () -> ClassUtil.addInterface(Fuck.class, new Proxy()));
        Assertions.assertThrows(AssertException.class, () -> ClassUtil.addInterface(Proxy.class, new Proxy()));
        Assertions.assertThrows(AssertException.class, () -> ClassUtil.addInterface(Function.class, new Proxy()));


        ClassUtil.addInterface(Runnable.class, 1);

        MyFunction function = ClassUtil.addInterface(MyFunction.class, new Proxy());

        Assertions.assertSame(123, function.apply("123"));

    }

    @Test
    void test() {


    }

    @Test
    void isPrimitiveOrWrapper() {
        Assertions.assertFalse(ClassUtil.isPrimitiveOrWrapper(null));
        Assertions.assertFalse(ClassUtil.isPrimitiveOrWrapper(Object.class));
        Assertions.assertTrue(ClassUtil.isPrimitiveOrWrapper(int.class));
        Assertions.assertTrue(ClassUtil.isPrimitiveOrWrapper(Integer.class));
        Assertions.assertTrue(ClassUtil.isPrimitiveOrWrapper(void.class));
        Assertions.assertTrue(ClassUtil.isPrimitiveOrWrapper(Void.class));
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

    private class Param implements Function<String, String> {

        @Override
        public String apply(String s) {
            return null;
        }
    }

}
