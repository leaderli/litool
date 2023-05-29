package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.proxy.DynamicDelegation;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/7/12
 */
@SuppressWarnings("ALL")
class ReflectUtilTest {

    @Test
    void newInterfaceImpl() {

        DynamicDelegation1 delegate = new DynamicDelegation1();


        Service service = ReflectUtil.newInterfaceImpl(Service.class, delegate);
        Assertions.assertEquals("request", service.service(new Request("request")).name);

        Assertions.assertEquals(delegate.toString(), service.toString());
        Assertions.assertEquals(delegate.hashCode(), service.hashCode());

        assertThrows(AssertException.class, () -> ReflectUtil.newInterfaceImpl(Function.class, null));
    }

    interface Service {
        Request service(Request request);
    }

    class Request {
        private String name;

        public Request(String name) {
            this.name = name;
        }
    }

    class DynamicDelegation1 extends DynamicDelegation {
        @RuntimeType
        public Object apply(int arg) {
            return arg;
        }

        @RuntimeType
        public Object apply(Object arg) {
            System.out.println(origin.getReturnType());
            return arg;
        }
    }

    class DynamicDelegation2 extends DynamicDelegation {
        @RuntimeType
        public Object apply(int arg) {
            return arg;
        }

        @RuntimeType
        public Object apply(Object o1, Object o2) {
            return o1;
        }

    }

    static {
        LiConstant.WHEN_THROW = null;

    }


    @Test
    void getFields() {
        assertEquals(3, ReflectUtil.getFields(LittleBean.class).size());
        assertTrue(ReflectUtil.getFields(Test1.class).absent());
        assertTrue(ReflectUtil.getFields(Out.In.class).absent());
    }


    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        assertEquals("bean",
                ReflectUtil.getField(LittleBean.class, "name").throwable_map(f -> f.get(littleBean)).get());
        assertEquals(8, ReflectUtil.getField(LittleBean.class, "age").throwable_map(f -> f.get(littleBean)).get());

        Lino<Field> name = ReflectUtil.getField(LittleBean.class, "name", true);
        assertNull(name.throwable_map(f -> f.get(littleBean), null).get());

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
    void newInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException {

        assertTrue(ReflectUtil.newInstance(Integer.class).absent());
        assertTrue(ReflectUtil.newInstance(PrimitiveEnum.class).absent());
        assertTrue(ReflectUtil.newInstance(Integer[].class).absent());
        assertTrue(ReflectUtil.newInstance(ConstructorBean.class).present());
        assertTrue(ReflectUtil.newInstance(Bean.class).present());

        assertTrue(ReflectUtil.newInstance(Integer.class, new Object[]{}).absent());

        assertTrue(ReflectUtil.newInstance(Integer.class, (String) null).absent());
        assertTrue(ReflectUtil.newInstance(Integer.class, (Integer) null).absent());
        assertTrue(ReflectUtil.newInstance(Integer.class, 1).present());
        assertTrue(ReflectUtil.newInstance(int.class).present());

        assertTrue(ReflectUtil.newInstance(TestBean.class, (String) null).present());
        assertTrue(ReflectUtil.newInstance(TestBean2.class, (String) null).present());


        // inner class

        assertTrue(ReflectUtil.newInstance(Out.In.class).present());
        assertTrue(ReflectUtil.newInstance(Out.In.InIn.class).present());

        assertFalse(ReflectUtil.newInstance(Number.class).present());
        assertFalse(ReflectUtil.newInstance(Consumer.class).present());

        assertFalse(ReflectUtil.newInstance(Number.class, null).present());
        assertFalse(ReflectUtil.newInstance(Consumer.class, null).present());
        assertFalse(ReflectUtil.newInstance(PrimitiveEnum.class, PrimitiveEnum.INT.toString()).present());

        assertFalse(ReflectUtil.newInstance(Number.class.getConstructors()[0]).present());

        Supplier<String> supplier = () -> this + "123";

        Class<? extends Supplier> cls = supplier.getClass();

        Assertions.assertNotNull(ReflectUtil.newInstance(cls).get().get());
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
    void findAnnotationsWithMetaAnnotation() {


        assertEquals(2, ReflectUtil.findAnnotationsWithMetaAnnotation(TestBean.class, API.class).size());
        assertEquals(0, ReflectUtil.findAnnotationsWithMetaAnnotation(TestBean.class, NotNull.class).size());
    }

    @Test
    void getMethods() throws IntrospectionException {
        assertEquals(2 + Object.class.getMethods().length, ReflectUtil.getMethods(Bean.class).size());

    }

    @Test
    void getMethod() {

        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1").present());
        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m3").present());
        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1", true).absent());
    }

    @Test
    void getMemberConstructor() {

        Assertions.assertTrue(Out.class.isMemberClass() && ReflectUtil.getMemberConstructor(Out.class).absent());
        Assertions.assertTrue(ReflectUtil.getMemberConstructor(Out.In.class).present());
        Assertions.assertTrue(ReflectUtil.getMemberConstructor(Out.In2.class).present());

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
        assertEquals(1, ReflectUtil.invokeMethod(m1, null).get());
        assertEquals(2, ReflectUtil.invokeMethod(m2, null).get());
        assertEquals(null, ReflectUtil.invokeMethod(m3, null, null).get());
        assertEquals(2, ReflectUtil.invokeMethod(m3, null, 2).get());
        assertEquals(null, ReflectUtil.invokeMethod(m3, null, 1, 2).get());

        assertEquals(1, ReflectUtil.invokeMethodByName(new Static(), "m1", null).get());
        assertEquals(2, ReflectUtil.invokeMethodByName(new Static(), "m2", null).get());
        assertEquals(null, ReflectUtil.invokeMethodByName(new Static(), "m3").get());
        assertEquals(2, ReflectUtil.invokeMethodByName(new Static(), "m3", 2).get());
    }

    public static class Out {
        public class In {
            public class InIn {

            }

        }

        public class In2 {

            private In2() {
            }
        }
    }

    @NotNull("1")
    @NotNull("2")
    public static class TestBean {

        public TestBean(String s) {

        }


    }

    public static class TestBean2 {

        private TestBean2(String s) {

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

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Test1 {

    }


}
