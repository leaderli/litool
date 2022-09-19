package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        Assertions.assertEquals(3, ReflectUtil.getFields(LittleBean.class).size());
        Assertions.assertTrue(ReflectUtil.getFields(Test1.class).absent());
    }


    @Test
    void testGetClass() throws NoSuchFieldException, NoSuchMethodException {

        Assertions.assertEquals(String.class, ReflectUtil.getClass(LittleBean.class.getDeclaredField("name")));
        Assertions.assertEquals(int.class, ReflectUtil.getClass(LittleBean.class.getDeclaredField("age")));
        Assertions.assertNull(ReflectUtil.getClass((Field) null));

        Assertions.assertEquals(void.class, ReflectUtil.getClass(LittleBean.class.getDeclaredMethod("m3")));
        Assertions.assertEquals(LittleBean.class, ReflectUtil.getClass(LittleBean.class.getConstructor()));


    }

    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean",
                ReflectUtil.getField(LittleBean.class, "name").throwable_map(f -> f.get(littleBean)).get());
        Assertions.assertEquals(8,
                ReflectUtil.getField(LittleBean.class, "age").throwable_map(f -> f.get(littleBean)).get());

        Lino<Field> name = ReflectUtil.getField(LittleBean.class, "name", true);
        Assertions.assertNull(name.throwable_map(f -> f.get(littleBean), null).get());
        Assertions.assertEquals("little", name.throwable_map(f -> {
            f.setAccessible(true);
            return f.get(littleBean);
        }).get());
        Assertions.assertEquals(8,
                ReflectUtil.getField(LittleBean.class, "age", true).throwable_map(f -> f.get(littleBean)).get());
    }

    @Test
    void getFieldValue() throws NoSuchFieldException {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age").get());

        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age", true).get());


        Assertions.assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name"));
        Assertions.assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name", true));


        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, LittleBean.class.getField("name")).get());
        Assertions.assertNull(ReflectUtil.getFieldValue(littleBean, Lino.Some.class.getDeclaredField("value")).get());


        Assertions.assertEquals(1, ReflectUtil.getFieldValue(null, Static.class.getField("age")).get());
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @Test
    void setFieldValue() throws NoSuchFieldException {
        LittleBean littleBean = new LittleBean();

        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null));
        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null, true));

        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello"));
        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());

        littleBean = new LittleBean();

        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello", true));
        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        Assertions.assertFalse(ReflectUtil.setFieldValue(littleBean, "name", 123, true));

        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, LittleBean.class.getField("name"), "hello"));

        Assertions.assertFalse(ReflectUtil.setFieldValue(littleBean, Lino.Some.class.getDeclaredField("value"),
                "hello"));


    }

    @Test
    void newInstance() {

        Assertions.assertTrue(ReflectUtil.newInstance(Integer.class).absent());
        Assertions.assertTrue(ReflectUtil.newInstance(ConstructorBean.class).present());
        Assertions.assertTrue(ReflectUtil.newInstance(Bean.class).present());

        Assertions.assertTrue(ReflectUtil.newInstance(Integer.class, new Object[]{}).absent());

        Assertions.assertTrue(ReflectUtil.newInstance(Integer.class, (String) null).absent());
        Assertions.assertTrue(ReflectUtil.newInstance(Integer.class, (Integer) null).absent());
        Assertions.assertTrue(ReflectUtil.newInstance(Integer.class, 1).present());

        Assertions.assertTrue(ReflectUtil.newInstance(TestBean.class, (String) null).present());
    }


    @Test
    void findAnnotations() {

        NotNull annotation = (NotNull) ReflectUtil.findAnnotations(TestBean.class).first().get();
        Assertions.assertEquals("1", annotation.value());
        Assertions.assertSame(annotation, ReflectUtil.getAnnotation(TestBean.class, NotNull.class).get());

        Assertions.assertEquals(0, ReflectUtil.findAnnotations(TestBean.class,
                an -> an.annotationType() != NotNull.class).size());
    }


    @Test
    void findAnnotationsWithMark() {


        Assertions.assertEquals(2, ReflectUtil.findAnnotationsWithMark(TestBean.class, API.class).size());
        Assertions.assertEquals(0, ReflectUtil.findAnnotationsWithMark(TestBean.class, NotNull.class).size());
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Test1 {

    }

    @Test
    void getMethods() {
        Assertions.assertEquals(2 + Object.class.getMethods().length, ReflectUtil.getMethods(Bean.class).size());
    }

    @Test
    void getMethod() {

        Assertions.assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1").present());
        Assertions.assertTrue(ReflectUtil.getMethod(LittleBean.class, "m3").present());
        Assertions.assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1", true).absent());
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
        Assertions.assertEquals(1, ReflectUtil.getMethodValue(m1, null).get());
        Assertions.assertEquals(2, ReflectUtil.getMethodValue(m2, null).get());
        Assertions.assertEquals(1, ReflectUtil.getMethodValue(m3, null, 1).get());
        Assertions.assertEquals(2, ReflectUtil.getMethodValue(m3, null, 2).get());
    }


    @Test
    void getGenericSuperclassType() {
        Consumer<?> consumer = new StringConsumer();

        Assertions.assertEquals(String.class,
                ReflectUtil.getDeclareTypeHead(consumer.getClass(), Consumer.class).get());

        Assertions.assertTrue(ReflectUtil.getDeclareTypeAt(Object.class, Object.class, 1).absent());
        Assertions.assertTrue(ReflectUtil.getDeclareTypeAt(Object.class, null, 1).absent());
        Assertions.assertTrue(ReflectUtil.getDeclareTypeAt(null, Object.class, -1).absent());
        List<String> list = new ArrayList<String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareTypeAt(list.getClass(), ArrayList.class, 0).get());
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareTypeHead(list.getClass(), ArrayList.class).get());
        Assertions.assertTrue(ReflectUtil.getDeclareTypeAt(list.getClass(), ArrayList.class, 1).absent());
        Map<String, String> map = new HashMap<String, String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareTypeAt(map.getClass(), HashMap.class, 0).get());
        Assertions.assertSame(String.class,
                ReflectUtil.getDeclareTypeAt(map.getClass(), HashMap.class, 1).get());
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


    @SuppressWarnings({"rawtypes", "unchecked"})
    private Class[] test(Class<?> cls, final Class<?> find, LiTuple2<TypeVariable, Class>[] declareTypes) {
        if (cls == null || cls == Object.class || find == null || find.getTypeParameters().length == 0 || !find.isInterface()) {
            return null;
        }
        if (cls == find) {
            Lira<Class> map = Lira.of(declareTypes).map(LiTuple2::_2).map(TypeUtil::getClass);
            return map.toArray(Class.class);
        }

        for (Type genericInterface : cls.getGenericInterfaces()) {
            LiTuple2<TypeVariable, Class>[] declareSuperTypes =
                    ReflectUtil.toReal(genericInterface, declareTypes);
            Class<?> inter;
            if (genericInterface instanceof Class) {
                inter = (Class<?>) genericInterface;
            } else if (genericInterface instanceof ParameterizedType) {
                inter = (Class<?>) ((ParameterizedType) genericInterface).getRawType();
            } else {
                throw new UnsupportedOperationException();
            }
            Class[] test = test(inter, find, declareSuperTypes);
            if (test != null) {
                return test;
            }
        }
        ParameterizedType parameterizedType = (ParameterizedType) cls.getGenericSuperclass();
        if (parameterizedType == null) {
            return null;
        }
        LiTuple2<TypeVariable, Class>[] declareSuperTypes = ReflectUtil.toReal(parameterizedType, declareTypes);

        return test(cls.getSuperclass(), find, declareSuperTypes);
//        return null;


    }


    @Test
    void getDeclareTypes() {


        Assertions.assertArrayEquals(new Class[]{ArrayList.class, String.class},
                ReflectUtil.getDeclareTypes(In4.class, In1.class));
        Assertions.assertArrayEquals(new Class[]{List.class, String.class}, ReflectUtil.getDeclareTypes(In3.class,
                In1.class));
        Assertions.assertArrayEquals(new Class[]{Object.class, String.class}, ReflectUtil.getDeclareTypes(In2.class,
                In1.class));
        Assertions.assertArrayEquals(new Class[]{ArrayList.class}, ReflectUtil.getDeclareTypes(In4.class,
                Consumer.class));
        Assertions.assertArrayEquals(new Class[]{List.class}, ReflectUtil.getDeclareTypes(In3.class, Consumer.class));
        Assertions.assertArrayEquals(new Class[]{Object.class}, ReflectUtil.getDeclareTypes(In2.class, Consumer.class));

        Assertions.assertArrayEquals(new Class[]{ArrayList.class, String.class}, ReflectUtil.getDeclareTypes(Ge4.class,
                Ge1.class));
        Assertions.assertArrayEquals(new Class[]{ArrayList.class, String.class}, ReflectUtil.getDeclareTypes(Ge3.class,
                Ge1.class));
        Assertions.assertArrayEquals(new Class[]{List.class, String.class}, ReflectUtil.getDeclareTypes(Ge2.class,
                Ge1.class));
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
