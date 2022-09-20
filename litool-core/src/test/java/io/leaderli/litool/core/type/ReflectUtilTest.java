package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.Lino;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/7/12
 */
@SuppressWarnings("ALL")
class ReflectUtilTest {

    static {
        LiConstant.WHEN_THROW = null;

    }

    @Test
    void getFields() {
        assertEquals(3, ReflectUtil.getFields(LittleBean.class).size());
        assertTrue(ReflectUtil.getFields(Test1.class).absent());
    }


    @Test
    void testGetClass() throws NoSuchFieldException, NoSuchMethodException {

        assertEquals(String.class, ReflectUtil.getClass(LittleBean.class.getDeclaredField("name")));
        assertEquals(int.class, ReflectUtil.getClass(LittleBean.class.getDeclaredField("age")));
        Assertions.assertNull(ReflectUtil.getClass((Field) null));

        assertEquals(void.class, ReflectUtil.getClass(LittleBean.class.getDeclaredMethod("m3")));
        assertEquals(LittleBean.class, ReflectUtil.getClass(LittleBean.class.getConstructor()));


    }

    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        assertEquals("bean",
                ReflectUtil.getField(LittleBean.class, "name").throwable_map(f -> f.get(littleBean)).get());
        assertEquals(8,
                ReflectUtil.getField(LittleBean.class, "age").throwable_map(f -> f.get(littleBean)).get());

        Lino<Field> name = ReflectUtil.getField(LittleBean.class, "name", true);
        Assertions.assertNull(name.throwable_map(f -> f.get(littleBean), null).get());
        assertEquals("little", name.throwable_map(f -> {
            f.setAccessible(true);
            return f.get(littleBean);
        }).get());
        assertEquals(8,
                ReflectUtil.getField(LittleBean.class, "age", true).throwable_map(f -> f.get(littleBean)).get());
    }

    @Test
    void getFieldValue() throws NoSuchFieldException {

        LittleBean littleBean = new LittleBean();
        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age").get());

        assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age", true).get());


        assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name"));
        assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name", true));


        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, LittleBean.class.getField("name")).get());
        Assertions.assertNull(ReflectUtil.getFieldValue(littleBean, Lino.Some.class.getDeclaredField("value")).get());


        assertEquals(1, ReflectUtil.getFieldValue(null, Static.class.getField("age")).get());
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @Test
    void setFieldValue() throws NoSuchFieldException {
        LittleBean littleBean = new LittleBean();

        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null));
        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null, true));

        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello"));
        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());

        littleBean = new LittleBean();

        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello", true));
        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        assertFalse(ReflectUtil.setFieldValue(littleBean, "name", 123, true));

        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        assertTrue(ReflectUtil.setFieldValue(littleBean, LittleBean.class.getField("name"), "hello"));

        assertFalse(ReflectUtil.setFieldValue(littleBean, Lino.Some.class.getDeclaredField("value"),
                "hello"));


    }

    @Test
    void newInstance() {

        assertTrue(ReflectUtil.newInstance(Integer.class).absent());
        assertTrue(ReflectUtil.newInstance(ConstructorBean.class).present());
        assertTrue(ReflectUtil.newInstance(Bean.class).present());

        assertTrue(ReflectUtil.newInstance(Integer.class, new Object[]{}).absent());

        assertTrue(ReflectUtil.newInstance(Integer.class, (String) null).absent());
        assertTrue(ReflectUtil.newInstance(Integer.class, (Integer) null).absent());
        assertTrue(ReflectUtil.newInstance(Integer.class, 1).present());

        assertTrue(ReflectUtil.newInstance(TestBean.class, (String) null).present());
    }


    @Test
    void findAnnotations() {

        NotNull annotation = (NotNull) ReflectUtil.findAnnotations(TestBean.class).first().get();
        assertEquals("1", annotation.value());
        Assertions.assertSame(annotation, ReflectUtil.getAnnotation(TestBean.class, NotNull.class).get());

        assertEquals(0, ReflectUtil.findAnnotations(TestBean.class,
                an -> an.annotationType() != NotNull.class).size());
    }


    @Test
    void findAnnotationsWithMark() {


        assertEquals(2, ReflectUtil.findAnnotationsWithMark(TestBean.class, API.class).size());
        assertEquals(0, ReflectUtil.findAnnotationsWithMark(TestBean.class, NotNull.class).size());
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Test1 {

    }

    @Test
    void getMethods() {
        assertEquals(2 + Object.class.getMethods().length, ReflectUtil.getMethods(Bean.class).size());
    }

    @Test
    void getMethod() {

        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1").present());
        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m3").present());
        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1", true).absent());
    }

    @Test
    void getConstructors() throws InvocationTargetException, IllegalAccessException, InstantiationException {


        Constructor<ConstructorBean> constructor = ReflectUtil.getConstructor(ConstructorBean.class).get();
        constructor.setAccessible(true);
        ConstructorBean constructorBean = constructor.newInstance();
        Assertions.assertNotNull(constructorBean);


    }

    @Test
    void getMethodValue() {

        Method m1 = ReflectUtil.getMethod(Static.class, "m1").get();
        Method m2 = ReflectUtil.getMethod(Static.class, "m2").get();
        Method m3 = ReflectUtil.getMethod(Static.class, "m3").get();
        assertEquals(1, ReflectUtil.getMethodValue(m1, null).get());
        assertEquals(2, ReflectUtil.getMethodValue(m2, null).get());
        assertEquals(1, ReflectUtil.getMethodValue(m3, null, 1).get());
        assertEquals(2, ReflectUtil.getMethodValue(m3, null, 2).get());
    }

    @Test
    void getDeclareTypeHead() {
        Consumer<?> consumer = new StringConsumer();

        assertEquals(String.class,
                ReflectUtil.getDeclareClassHead(consumer.getClass(), Consumer.class).get());

        List<String> list = new ArrayList<String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareClassHead(list.getClass(), ArrayList.class).get());
    }

    @Test
    void getDeclareTypeAt() {


        assertTrue(ReflectUtil.getDeclareClassAt(Object.class, Object.class, 1).absent());
        assertTrue(ReflectUtil.getDeclareClassAt(Object.class, null, 1).absent());
        assertTrue(ReflectUtil.getDeclareClassAt(null, Object.class, -1).absent());
        List<String> list = new ArrayList<String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareClassAt(list.getClass(), ArrayList.class, 0).get());

        assertTrue(ReflectUtil.getDeclareClassAt(list.getClass(), ArrayList.class, 1).absent());
        Map<String, String> map = new HashMap<String, String>() {
        };
        assertSame(String.class,
                ReflectUtil.getDeclareClassAt(map.getClass(), HashMap.class, 0).get());
        assertSame(String.class,
                ReflectUtil.getDeclareClassAt(map.getClass(), HashMap.class, 1).get());
    }


    @NotNull("1")
    @NotNull("2")
    public static class TestBean {

        public TestBean(String s) {

        }


    }

    public static class Static {
        public static int age = 1;

        private static int m1() {
            return 1;
        }

        public static int m2() {
            return 2;
        }

        public static int m3(int age) {
            return age;
        }
    }

    @SuppressWarnings("FieldCanBeLocal")
    public static class Bean {

        private final int age = 80;
        public String name = "bean";

        public void m1() {

        }

        private void m2() {

        }

        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static class LittleBean extends Bean {
        private final String name = "little";
        public int age = 8;

        private void m3() {

        }

        @Override
        public String toString() {
            return "LittleBean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    static class ConstructorBean {


        private ConstructorBean(String name) {

        }

        private ConstructorBean() {

        }
    }

    private static class StringConsumer implements Consumer<String> {
        @Override
        public void accept(String o) {

        }
    }


    @Test
    void getDeclareClasses() {


        assertArrayEquals(new Object[]{ArrayList.class, String.class}, ReflectUtil.getDeclareClasses(In4.class, In1.class).toArray());
        assertArrayEquals(new Object[]{List.class, String.class}, ReflectUtil.getDeclareClasses(In3.class, In1.class).toArray());
        assertArrayEquals(new Object[]{Object.class, String.class}, ReflectUtil.getDeclareClasses(In2.class, In1.class).toArray());
        assertArrayEquals(new Object[]{ArrayList.class}, ReflectUtil.getDeclareClasses(In4.class, Consumer.class).toArray());
        assertArrayEquals(new Object[]{List.class}, ReflectUtil.getDeclareClasses(In3.class, Consumer.class).toArray());
        assertArrayEquals(new Object[]{Object.class}, ReflectUtil.getDeclareClasses(In2.class, Consumer.class).toArray());

        assertArrayEquals(new Object[]{ArrayList.class, String.class}, ReflectUtil.getDeclareClasses(Ge4.class, Ge1.class).toArray());
        assertArrayEquals(new Object[]{ArrayList.class, String.class}, ReflectUtil.getDeclareClasses(Ge3.class, Ge1.class).toArray());
        assertArrayEquals(new Object[]{List.class, String.class}, ReflectUtil.getDeclareClasses(Ge2.class, Ge1.class).toArray());


        assertArrayEquals(new Object[]{Object.class, Consumer.class}, ReflectUtil.getDeclareClasses(In5.class, In1.class).toArray());

        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return Consumer.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        System.out.println(parameterizedType);

        ParameterizedType genericInterface = (ParameterizedType) In5.class.getGenericInterfaces()[0];
        System.out.println(genericInterface.getOwnerType());

    }


    private static interface In5<A> extends In1<A, Consumer<? extends Number>> {

    }

    private static interface In1<A, B> {

    }

    private static interface In2<A> extends In1<A, String>, Consumer<A> {

    }

    private static class In3<C extends List> implements In2<C> {

        @Override
        public void accept(C c) {

        }
    }

    private static class In4 extends In3<ArrayList> {

    }

    private static class Ge1<A, B> {

        public void log(A a, B b) {

        }

    }

    private static class Ge2<C extends List> extends Ge1<C, String> {

    }

    private static class Ge3 extends Ge2<ArrayList> {

    }

    private static class Ge4 extends Ge3 {

    }
}
