package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.LiConstant;
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
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/12
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "JavaReflectionMemberAccess"})
class ReflectUtilTest {

    static {
        LiConstant.WHEN_THROW = null;

    }

    @SuppressWarnings("ConstantConditions")
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

    @Test
    void getFields() {
        Assertions.assertEquals(3, ReflectUtil.getFields(LittleBean.class).size());
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
    void getSuperclassType() {

        Assertions.assertSame(0, ReflectUtil.getSuperclassType(Object.class).size());
        Assertions.assertSame(0, ReflectUtil.getSuperclassType(Function.class).size());
        List<?> list = new ArrayList<String>() {

        };
        Lira<Type> superclassType = ReflectUtil.getSuperclassType(list.getClass());
        Assertions.assertTrue(superclassType.first().get() instanceof ParameterizedType);
    }

    @Test
    void getSuperInterface() {

        Assertions.assertTrue(ReflectUtil.getInterfacesType(Object.class).absent());
        Lira<Type> superInterface = ReflectUtil.getInterfacesType(ArrayList.class);

        Assertions.assertSame(6, superInterface.size());
    }


    @Test
    void getGenericInterfacesType() {

        Consumer<?> consumer = new StringConsumer();

        Assertions.assertEquals(String.class,
                ReflectUtil.getGenericInterfacesType(consumer.getClass(), Consumer.class).get());
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Test
    void getGenericSuperclassType() {


        Assertions.assertTrue(ReflectUtil.getGenericSuperclassType(Object.class, Object.class, 1).absent());
        Assertions.assertTrue(ReflectUtil.getGenericSuperclassType(Object.class, null, 1).absent());
        Assertions.assertTrue(ReflectUtil.getGenericSuperclassType(null, Object.class, -1).absent());
        List<String> list = new ArrayList<String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getGenericSuperclassType(list.getClass(), ArrayList.class, 0).get());
        Assertions.assertSame(String.class,
                ReflectUtil.getGenericSuperclassType(list.getClass(), ArrayList.class).get());
        Assertions.assertTrue(ReflectUtil.getGenericSuperclassType(list.getClass(), ArrayList.class, 1).absent());
        Map<String, String> map = new HashMap<String, String>() {
        };
        Assertions.assertSame(String.class,
                ReflectUtil.getGenericSuperclassType(map.getClass(), HashMap.class, 0).get());
        Assertions.assertSame(String.class,
                ReflectUtil.getGenericSuperclassType(map.getClass(), HashMap.class, 1).get());
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
}
