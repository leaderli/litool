package io.leaderli.litool.core.type;

import io.leaderli.litool.core.lang.DisposableRunnableProxy;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.WhenThrowBehavior;
import io.leaderli.litool.core.util.ConsoleUtil;
import org.apiguardian.api.API;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
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
    static {
        WhenThrowBehavior.WHEN_THROW = null;

    }


    @Test
    void testnewInterfaceImpl() {


    }

    @Test
    void newInterfaceImpl() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Assertions.assertThrows(IllegalArgumentException.class, () -> ReflectUtil.newInterfaceImpl(LiTypeToken.of(Proxy.class), LiTypeToken.of(Proxy.class), new Proxy()));


        Assertions.assertDoesNotThrow(() -> ReflectUtil.newInterfaceImpl(LiTypeToken.of(Runnable.class), LiTypeToken.of(DisposableRunnableProxy.class), DisposableRunnableProxy.of(() -> System.out.println(123))));

        ClassUtilTest.MyFunction<String, Integer> function = ReflectUtil.newInterfaceImpl(new LiTypeToken<ClassUtilTest.MyFunction<String, Integer>>() {
        }, LiTypeToken.of(Proxy.class), new Proxy());

        Assertions.assertSame(123, function.apply("123"));
        function = ReflectUtil.newInterfaceImpl(LiTypeToken.of(ClassUtilTest.MyFunction.class), LiTypeToken.of(Proxy.class), new Proxy());


        Assertions.assertEquals(456, function.apply("123"));

        DynamicDelegation1 delegate = new DynamicDelegation1();


        Service service = ReflectUtil.newInterfaceImpl(LiTypeToken.of(Service.class), LiTypeToken.of(DynamicDelegation1.class), delegate);
        Assertions.assertEquals("request", service.service(new Request("request")).name);

        Assertions.assertEquals(delegate.toString(), service.toString());
        Assertions.assertEquals(delegate.hashCode(), service.hashCode());

        assertThrows(NullPointerException.class, () -> ReflectUtil.newInterfaceImpl(LiTypeToken.of(Function.class), LiTypeToken.of(Function.class), null));


        Service2 service2 = ReflectUtil.newInterfaceImpl(LiTypeToken.of(Service2.class), LiTypeToken.of(DynamicDelegation3.class), new DynamicDelegation3());
        Assertions.assertSame(String.class, service2.service(123).getClass());
        Service3 my3 = ReflectUtil.newInterfaceImpl(LiTypeToken.of(Service3.class), LiTypeToken.of(DynamicDelegation3.class), new DynamicDelegation3());
        Assertions.assertEquals(123, my3.service(123));

        Assertions.assertThrows(IllegalStateException.class, () -> ReflectUtil.newInterfaceImpl(LiTypeToken.of(Function.class), LiTypeToken.of(DynamicDelegation3.class), new DynamicDelegation3()));

        Assertions.assertEquals(3, ReflectUtil.newInterfaceImpl(new LiTypeToken<Function<String, Integer>>() {
        }, LiTypeToken.of(DynamicDelegation4.class), new DynamicDelegation4()).apply("123"));

        Service5 service5 = ReflectUtil.newInterfaceImpl(LiTypeToken.of(Service5.class), LiTypeToken.of(DynamicDelegation5.class), new DynamicDelegation5());
        Assertions.assertArrayEquals(new Class[0], service5.get());
        Assertions.assertArrayEquals(new Class[]{String.class}, service5.get(""));


        Service6 service6 = ReflectUtil.newInterfaceImpl(LiTypeToken.of(Service6.class), LiTypeToken.of(DynamicDelegation6.class), new DynamicDelegation6());
        Assertions.assertSame(String.class, service6.service(123).getClass());


        Method method = Service6.class.getMethod("service", Integer.class);
        Assertions.assertSame(String.class, method.invoke(service6, 123).getClass());

        // 测试抛出异常
        Function<String, Integer> illegalFunction = ReflectUtil.newInterfaceImpl(new LiTypeToken<Function<String, Integer>>() {
        }, LiTypeToken.of(DynamicDelegation7.class), new DynamicDelegation7());
        Assertions.assertThrows(IllegalArgumentException.class, () -> illegalFunction.apply(""));
    }

    @Test
    void getFields() {

        assertEquals(0, ReflectUtil.getFields(Object.class).size());
        assertEquals(5, ReflectUtil.getFields(LittleBean.class).size());
    }

    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        assertEquals("bean",
                ReflectUtil.getField(LittleBean.class, "name").mapIgnoreError(f -> f.get(littleBean)).get());
        assertEquals(8, ReflectUtil.getField(LittleBean.class, "age").mapIgnoreError(f -> f.get(littleBean)).get());


        assertEquals(8,
                ReflectUtil.getField(LittleBean.class, "age").mapIgnoreError(f -> f.get(littleBean)).get());
    }


    @Test
    void getFieldValue() throws NoSuchFieldException {

        LittleBean littleBean = new LittleBean();
        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age").get());

        assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age").get());


        assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name"));
        assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name"));


        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, LittleBean.class.getField("name")).get());
        Assertions.assertNull(ReflectUtil.getFieldValue(littleBean, Lino.Some.class.getDeclaredField("value")).get());


        assertEquals(1, ReflectUtil.getFieldValue(null, Static.class.getField("age")).get());

        assertEquals(1, (ReflectUtil.getFieldValue(littleBean, "gender").get()));

    }


    @SuppressWarnings("JavaReflectionMemberAccess")
    @Test
    void setFieldValue() throws NoSuchFieldException {
        LittleBean littleBean = new LittleBean();

        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null));
        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null));

        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello"));
        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());

        littleBean = new LittleBean();

        assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello"));
        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());


        assertFalse(ReflectUtil.setFieldValue(littleBean, "name", 123));

        assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());


        assertTrue(ReflectUtil.setFieldValue(littleBean, LittleBean.class.getField("name"), "hello"));

        assertFalse(ReflectUtil.setFieldValue(littleBean, Lino.Some.class.getDeclaredField("value"),
                "hello"));


    }


    @Test
    void test111() {

        ConsoleUtil.printArray(ReflectUtil.newInstance(Integer[].class).get());
    }

    @Test
    void newInstance() throws NoSuchMethodException, InstantiationException, IllegalAccessException {

        assertTrue(ReflectUtil.newInstance(Integer.class).present());
        assertTrue(ReflectUtil.newInstance(PrimitiveEnum.class).present());
        assertTrue(ReflectUtil.newInstance(NoneEnum.class).absent());
        assertTrue(ReflectUtil.newInstance(Integer[].class).present());
        assertTrue(ReflectUtil.newInstance(ConstructorBean.class).present());
        assertTrue(ReflectUtil.newInstance(Bean.class).present());

        assertTrue(ReflectUtil.newInstance(Integer.class, new Object[]{}).present());

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

    enum NoneEnum {
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
    void getMethod() {

        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1").present());
        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m3").present());
//        assertTrue(ReflectUtil.getMethod(LittleBean.class, "m1").absent());
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

    interface Service {
        Request service(Request request);
    }

    interface Service2 {
        String service(Integer request);
    }

    interface Service3 {
        int service(Integer request);
    }

    interface Service5 {
        Class[] get();

        Class[] get(String s);
    }

    interface Service6 {
        CharSequence service(Integer request);
    }


    public static class Proxy {

        public Object apply(Object s) {
            return 456;
        }

        public Integer apply(String s) {
            return Integer.valueOf(s);
        }

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
        private int gender = 1;

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

    class Request {
        private String name;

        public Request(String name) {
            this.name = name;
        }
    }

    class DynamicDelegation1 {
        @RuntimeMethod
        public Object apply(int arg) {
            return arg;
        }

        @RuntimeMethod
        public Object apply(Object arg) {
            return arg;
        }
    }

    class DynamicDelegation2 {
        @RuntimeMethod
        public Object apply(int arg) {
            return arg;
        }

        @RuntimeMethod
        public Object apply(Object o1, Object o2) {
            return o1;
        }

    }

    class DynamicDelegation3 {
        @RuntimeMethod
        public Object apply(int arg) {
            return arg;
        }


        @RuntimeMethod
        public String apply2(int arg) {
            return arg + "";
        }
    }

    class DynamicDelegation4 implements Function<String, Integer> {

        @Override
        public Integer apply(String s) {
            return s.length();
        }
    }

    class DynamicDelegation5 {

        @RuntimeMethod
        public Object apply(@RuntimeParameter Method origin) {
            return origin.getParameterTypes();
        }

        @RuntimeMethod
        public Object apply(@RuntimeParameter Method origin, String s) {
            return origin.getParameterTypes();
        }
    }

    class DynamicDelegation6 {
        @RuntimeMethod
        public int apply(int arg) {
            return arg;
        }


        @RuntimeMethod
        public String apply2(int arg) {
            return arg + "";
        }
    }

    class DynamicDelegation7 implements Function<String, Integer> {

        @Override
        public Integer apply(String s) {
            if (s.length() < 1) {
                throw new IllegalArgumentException("123");
            }
            return s.length();
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class Test1 {

    }


}
