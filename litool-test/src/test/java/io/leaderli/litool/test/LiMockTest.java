package io.leaderli.litool.test;

import com.google.gson.Gson;
import io.leaderli.litool.core.io.IOUtils;
import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiTypeToken;
import io.leaderli.litool.core.type.MethodFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;

import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class LiMockTest {

    static {
        LiMock.skipClassInitializer(Error.class);
    }


    @LiTest
    void skipClassConstructors() {

        Assertions.assertEquals(2, new Foo().a);
        LiMock.skipClassConstructors(Foo.class);
        Assertions.assertEquals(0, new Foo().a);

        Assertions.assertEquals(2, new Foo2(2).a);
        LiMock.skipClassConstructors(Foo2.class);
        Assertions.assertEquals(0, new Foo2(2).a);


    }

    @Test
    void testGetClass() {

        Assertions.assertThrows(RuntimeException.class, () -> LiMock.getCtClass(null));

    }

    @LiTest
    void skipClassConstructors3() {

        Assertions.assertEquals(2, new Foo().a);
        LiMock.skipClassConstructors(Foo.class);
        Assertions.assertEquals(0, new Foo().a);
        Assertions.assertEquals(1, new Foo().m1());
        LiMock.mock(Foo.class, MethodFilter.isMethod(), (method, args) -> 2, false);
        Assertions.assertEquals(0, new Foo().a);
        Assertions.assertEquals(2, new Foo().m1());
        LiMock.mock(Foo.class, MethodFilter.isMethod(), (method, args) -> 3, true);
        Assertions.assertEquals(2, new Foo().a);
        Assertions.assertEquals(3, new Foo().m1());
        LiMock.skipClassConstructors(Foo.class, false);
        Assertions.assertEquals(0, new Foo().a);
        Assertions.assertEquals(3, new Foo().m1());
        LiMock.skipClassConstructors(Foo.class);
        Assertions.assertEquals(0, new Foo().a);
        Assertions.assertEquals(1, new Foo().m1());
    }

    @Test
    void testArg0() {
        LiMock.mocker(Error.class).call(Error.m1(1))
                .arg0(1)
                .then(100)
                .arg0(2)
                .then(200)
                .other(300)
                .build();
        Assertions.assertEquals(100, Error.m1(1));
        Assertions.assertEquals(200, Error.m1(2));
        Assertions.assertEquals(300, Error.m1(3));

        LiMock.mocker(Error.class).call(Error.m1(1))
                .arg0(1)
                .then(100)
                .arg0(2)
                .then(200)
                .build();
        Assertions.assertEquals(100, Error.m1(1));
        Assertions.assertEquals(200, Error.m1(2));
        Assertions.assertEquals(4, Error.m1(3));
    }

    @LiTest
    void skipClassConstructors2() {
//        Assertions.assertEquals(2, new Foo3(2).a);
        LiMock.skipClassConstructors(Foo3.class);
        Assertions.assertEquals(0, new Foo3(2).a);
        LiMock.skipClassConstructors(Foo5.class);
        Assertions.assertEquals(0, new Foo5(5, "5").a);

        Assertions.assertEquals(2, new Foo7(1, 1).a);
        LiMock.skipClassConstructors(Foo7.class);
        Assertions.assertEquals(0, new Foo7(1, 1).a);
    }

    @LiTest
    public void testMockStatic() {

        LiMock.mockStatic(Error.class, MethodFilter.isMethod(), (method, args) -> 100);
        Assertions.assertEquals(100, Error.m3());
        Assertions.assertEquals(100, Error.m1());
        Assertions.assertEquals(100, Error.m4());
        LiMock.mockStatic(Error.class, MethodFilter.name("m1"),
                (method, args) -> {
                    if (args.length == 0) {
                        return 30;
                    }
                    return (int) args[0] + 40;
                }
        );
        Assertions.assertEquals(30, Error.m1());
        Assertions.assertEquals(44, Error.m1(4));
        // 重新代理后，m4 应该不被代理
        Assertions.assertEquals(4, Error.m4());
        Assertions.assertNotEquals(100, Error.m4());


        Assertions.assertTrue(Either1.m1(1).isRight());
        LiMock.mockStatic(Either1.class, MethodFilter.isMethod(), (m, args) -> {

            if ((int) args[0] == 0) {
                return Either.none();
            }
            return Either.right(Either.right(2));
        });
        Assertions.assertEquals(1, Either1.m1(0).get());
        Assertions.assertEquals(2, Either1.m1(3).get());

    }

    @Test
    void testMockReset() {
        LiMock.mockStatic(Error.class, MethodFilter.name("m1"), (method, args) -> 100, false);
        LiMock.mockStatic(Error.class, MethodFilter.name("m4"), (method, args) -> 200, false);
        Assertions.assertEquals(100, Error.m1());
        Assertions.assertEquals(200, Error.m4());
        LiMock.mockStatic(Error.class, MethodFilter.name("m4"), (method, args) -> 200, true);
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(200, Error.m4());

    }

    @LiTest
    void testGetInvoke() {
        MockMethodInvoker.invokers.clear();
        LiMock.mockStatic(Error.class, MethodFilter.isMethod(), (method, args) -> 100);
        Assertions.assertEquals(4, MockMethodInvoker.invokers.size());
    }

    @LiTest
    void testMocker() {

        LiMock.mocker(Error.class).supplier(Error::m1).then(101).build();
        Assertions.assertEquals(101, Error.m1());
        Assertions.assertEquals(4, Error.m4());
        LiMock.mocker(Error.class).call(Error.m1(1)).then(11).other(12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
        LiMock.mocker(Error.class).call(Error.m1(1)).then(11).other(12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.mocker(Error.class).call(Error.m1(1)).other((m, args) -> {
            if ((int) args[0] == 1) {
                return 21;
            }
            return 22;
        }).build();
        Assertions.assertEquals(21, Error.m1(1));
        Assertions.assertEquals(22, Error.m1(2));
        Bean bean = new Bean();


    }

    @LiTest
    void testWhen() {
        LiMock.mocker(Error.class).call(Error.m1(1)).then(11).other(12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
        LiMock.mocker(Error.class).call(Error.m1(1)).then(11).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(3, Error.m1(2));

        LiMock.mocker(Error.class).call(Error.m1(1)).then(10)
                .call(Error.m1()).then(10)
                .build();
        Assertions.assertEquals(10, Error.m1(1));
        Assertions.assertEquals(3, Error.m1(2));
        Assertions.assertEquals(10, Error.m1());
        LiMock.mocker(Error.class).call(Error.m1(1)).then(20).build();
        Assertions.assertEquals(20, Error.m1(1));
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(3, Error.m1(2));
        LiMock.reset();
        LiMock.mocker(Error.class).call(Error.m1(1)).then(20).build();
        Assertions.assertEquals(20, Error.m1(1));
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(3, Error.m1(2));


        Foo1 foo1 = () -> "";
        LiMock.mocker(Foo1.class).call(foo1.get2()).then("123").build();
        Assertions.assertEquals("123", foo1.get2());

        Assertions.assertThrows(IllegalStateException.class, () -> LiMock.mocker(Foo1.class).call(foo1.get()).then("123").build());

    }

    @Test
    void testWhen2() {
        LiMock.mocker(Error.class).call(Error.m1(1)).then(11).other(12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.mocker(Error.class, false).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
    }

    @Test
    void testWhen3() throws UnmodifiableClassException, ClassNotFoundException {
        LiMock.mocker(Error.class)
                .call(Error.m1(1)).then(11).other(12)
                .call(Error.m1(), 10)
                .build();
        Assertions.assertEquals(10, Error.m1());
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.mocker(Error.class, false).call(Error.m1(1), 111).build();
        Assertions.assertEquals(111, Error.m1(1));
        Assertions.assertEquals(10, Error.m1());


        LiMock.mocker(Void2.class).run(() -> Void2.m1(1))
                .build();

        Void2.m1(2);
        Assertions.assertEquals(1, Void2.a);
        LiMock.detach(Void2.class);
        Void2.m1(2);
        Assertions.assertEquals(2, Void2.a);
    }

    @Test
    void testWhen4() {

        LiMock.mocker(Error.class).call(Error.m1()).then(null).build();
        Assertions.assertDoesNotThrow(() -> Error.m1());
        LiMock.reset();
        LiMock.mocker(Error.class).call(Error.m1()).other((Integer) null).build();
        Assertions.assertDoesNotThrow(() -> Error.m1());
        LiMock.reset();
        LiMock.mocker(Error.class).call(Error.m1(1)).then(100).call(Error.m1(2)).then(200).build();
        Assertions.assertEquals(100, Error.m1(1));
        Assertions.assertEquals(200, Error.m1(2));

    }

    @LiTest
    void testWhenThenOther() {

        LiMock.mocker(Error.class)
                .call(Error.m1(1))
                .then(11)
                .other(12)
                .call(Error.m1())
                .then(11)
                .build();
        System.out.println(Error.m1(1));
        System.out.println(Error.m1(2));
        System.out.println(Error.m1());
    }

    @LiTest
    void testWhenBean2() {

        Bean1 foo = new Bean1();
        Assertions.assertEquals(1, foo.m1());
        LiMock.mockerBean(Bean1.class).when(Bean1::m1, 2).build();
        Assertions.assertEquals(2, foo.m1());
    }

    @ExtendWith(SkipWhenJacocoExecutionCondition.class)
    @LiTest
    void testWhenBean3() {

        Supplier<Integer> supplier = LiMock.mockerBean(Supplier1.class).other(Supplier::get, (m, a) -> 100).build();
        Assertions.assertEquals(100, supplier.get());
        Assertions.assertEquals(100, new Supplier1().get());

    }

    @Test
    @ExtendWith(SkipWhenJacocoExecutionCondition.class)
    void testMockerBeans() {

        Supplier<Integer> supplier = LiMock.mockerBeans(Supplier1.class, Supplier2.class).other(Supplier::get, (m, a) -> 100).build();
        Assertions.assertEquals(100, supplier.get());
        Assertions.assertEquals(100, new Supplier1().get());
        Assertions.assertEquals(100, new Supplier2().get());
//
    }

    @Test
    @ExtendWith(SkipWhenJacocoExecutionCondition.class)
    void testRecordBeans() {

        Supplier<Integer> supplier = LiMock.mockerBeans(Supplier1.class, Supplier2.class).other(Supplier::get, (m, a) -> 100).build();
        LiMock.recordBeans(Supplier1.class, Supplier2.class).consume(Supplier::get).assertReturn(100).build();
        Assertions.assertEquals(100, supplier.get());
        Assertions.assertEquals(100, new Supplier1().get());
        Assertions.assertEquals(100, new Supplier2().get());
//
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void testWhenInterface() {
        MockInterface<Supplier<Integer>> supplierMockInterface = LiMock.mockerInterface(new LiTypeToken<Supplier<Integer>>() {
        });
        Supplier<Integer> supplier = supplierMockInterface
                .consume(Supplier::get)
                .then(0)
                .then(1)
                .build();

        Assertions.assertEquals(1, supplier.get());


        supplierMockInterface
                .function(Supplier::get)
                .then(2)
                .build();
        Assertions.assertEquals(2, supplier.get());

        supplierMockInterface
                .consume(Supplier::get)
                .then(2)
                .build();
        Assertions.assertEquals(2, supplier.get());

        Function function = LiMock.mockerInterface(Function.class).when(f -> f.apply(1), 1).build();
        Assertions.assertEquals(1, function.apply(1));
        Assertions.assertNull(function.apply(2));

        function = LiMock.mockerInterface(Function.class).otherMethod((m, args) -> 1).build();
        Assertions.assertEquals(1, function.apply(2));

    }

    @LiTest
    void testSkipClassInitializer() {
        Assertions.assertDoesNotThrow(() -> Error.m1());
    }


    @LiTest
    void testRecordStatic() {

        Assertions.assertEquals(2, Error.m1(1));
        LiMock.mocker(Error.class).call(Error.m1(1), 11, 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.recordStatic(Error.class, MethodFilter.isPublic(), (m, args, value) -> {
            if (args.length == 1) {
                if ((int) args[0] == 1) {
                    Assertions.assertEquals(11, value);
                } else {
                    Assertions.assertEquals(12, value);
                }
            }
        });


    }

    @LiTest
    void testRecordVoid() {

        LiMock.recorder(Void1.class).run(() -> Void1.m1(1))
                .called()
                .assertReturn(null)
                .args(1)
                .arg(0, 1)

                .build();

        Assertions.assertThrows(AssertionFailedError.class, LiMock::assertMethodCalled);
        Void1.m1(1);
        Assertions.assertDoesNotThrow(LiMock::assertMethodCalled);
        LiMock.reset();
        LiMock.recorder(Void1.class)
                .run(() -> Void1.m1(1))
                .argAssert(0, v -> {
                }, Boolean.class)
                .build();
        Assertions.assertThrows(AssertionFailedError.class, () -> Void1.m1(1));
        LiMock.reset();
        LiMock.recorder(Void1.class)
                .run(() -> Void1.m1(1))
                .argAssert(0, v -> Assertions.assertEquals(2, v), Integer.class)
                .build();
        Assertions.assertThrows(AssertionFailedError.class, () -> Void1.m1(1));
        LiMock.reset();
        LiMock.recorder(Void1.class)
                .run(() -> Void1.m1(1))
                .argAssert(0, v -> Assertions.assertEquals(1, v), Integer.class)
                .build();
        Assertions.assertDoesNotThrow(() -> Void1.m1(1));
    }

    @Test
    void testReset() {
        Bean1 bean1 = new Bean1();
        LiMock.mockerBean(Bean1.class).when(Bean1::m1, 2).build();
        Assertions.assertEquals(2, bean1.m1());
        LiMock.reset();
        LiMock.recordBean(Bean1.class).function(Bean1::m1).build();

        Assertions.assertEquals(1, bean1.m1());


    }

    @LiTest
    void testRecordBean() {
        LiMock.mockerBean(Bean1.class).when(Bean1::m1, 2).build();

        LiMock.reset();
        Bean1 bean = LiMock.recordBean(Bean1.class).function(Bean1::m1)
                .called()
                .assertReturn(1)
                .build();
        Assertions.assertThrows(AssertionFailedError.class, LiMock::assertMethodCalled);

        bean.m1();
        Assertions.assertDoesNotThrow(LiMock::assertMethodCalled);
        LiMock.recordBean(Bean1.class).function(Bean1::m1)
                .called()
                .assertReturn(2)
                .build();
        Assertions.assertThrows(AssertionFailedError.class, LiMock::assertMethodCalled);

        Assertions.assertThrows(AssertionFailedError.class, bean::m1);

    }

    @LiTest
    void testMockRecord() {

        Bean2 bean2 = LiMock.mockerBean(Bean2.class).when(b -> b.m1(1), "2").build();
        LiMock.recordBean(Bean2.class).function(b -> b.m1(1)).called().build();

        bean2.m1(1);


    }

    @Test
    void testRecord() {

        LiBox<Method> box = LiBox.none();
        LiMock.record(Bean.class, MethodFilter.name("m1"), (m, args, _return) -> box.value(m));
        Bean bean = new Bean();
        bean.m1();
        Assertions.assertEquals("m1", box.value().getName());

    }


    @LiTest
    void testRecordInLira() {

        System.setErr(IOUtils.emptyPrintStream());
        Lira1 lira1 = new Lira1();
        Assertions.assertDoesNotThrow(() -> Lira.of(1, 2, 3).mapIgnoreError(lira1::m1).get());
        LiMock.recorder(Lira1.class).when(lira1.m1(1)).assertReturn(100).build();
        Assertions.assertDoesNotThrow(() -> {
            Lira.of(1, 2, 3).mapIgnoreError(lira1::m1).get();
        });

        Assertions.assertThrows(Throwable.class, LiMock::assertDoesNotThrow);

    }

    @Test
    void testDelegate() {

        Assertions.assertEquals(1, new Foo().m1());
        LiMock.mock(Foo.class, Delegate.class);
        Assertions.assertEquals(2, new Foo().m1());
        LiMock.record(Foo.class, DelegateRecord.class);
        Assertions.assertEquals(2, new Foo().m1());
    }

    @Test
    void testRecordMethodCall() {

        StringBuilder out = new StringBuilder();
        LiMock.recordMethodCall(out::append, Gson.class);
        new Gson().fromJson("{\"a\":1}", Map.class);
        Assertions.assertTrue(out.toString().contains("ObjectTypeAdapter.getFactory(DOUBLE)"));

    }

    static class Delegate {
        public static int m1() {
            return 2;
        }
    }

    static class DelegateRecord {
        public static void m1(Foo foo, int _return) {
            Assertions.assertEquals(2, _return);
        }
    }

    @SuppressWarnings("ConstantValue")
    static class Error {
        static {
            if (1 == 1) {
                throw new IllegalStateException("1 == 1");
            }
        }

        public static int m1() {
            return 1;
        }

        public static int m1(int a) {
            return a + 1;
        }

        public static int m3() {
            return m4();
        }

        private static int m4() {
            return 4;
        }


    }

    static class Bean {
        public int m1() {
            return 1;
        }

        public int m1(int a) {
            return a + 1;
        }

        public int m3() {
            return m4();
        }

        private int m4() {
            return 4;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    static class Foo {
        public int a = 1;

        public Foo() {
            a = 2;
        }

        public int m1() {
            return 1;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    static class Foo2 {
        public int a = 1;

        public Foo2(int a) {
            this.a = a;
        }
    }

    static class Foo3 extends Foo2 {

        public Foo3(int a) {
            super(a);
        }
    }

    static class Foo4 {
        public int a;

        public Foo4(int a, String b) {
        }
    }

    static class Foo5 extends Foo4 {


        public Foo5(int a, String b) {
            super(a, b);
        }
    }

    static class Foo6 {
        int a;

        Foo6(int a) {
            this.a = 1;
        }

        Foo6() {
            this.a = 0;
        }

        Foo6(int a, int b) {
            this.a = 2;
        }
    }

    static class Foo7 extends Foo6 {
        public Foo7(int a, int b) {
            super(a, b);
        }
    }

    interface Foo1 {
        Object get();

        default Object get2() {
            return get();
        }
    }

    static class Either1 {
        public static Either<?, ?> m1(int i) {
            return Either.right(1);
        }
    }

    static class Void1 {
        static void m1(int a) {
            System.out.println();
        }
    }

    static class Void2 {
        public static int a = 1;

        static void m1(int a) {
            Void2.a = a;
        }
    }

    static class Bean1 {
        int m1() {
            return 1;
        }

        void m2(int a) {
        }
    }

    static class Bean2 {
        String m1(int a) {
            return String.valueOf(a);
        }
    }

    static class Supplier1 implements Supplier<Integer> {

        @Override
        public Integer get() {
            return 1;
        }

        public void m1() {

        }
    }

    abstract static class Supplier0 implements Supplier<Integer> {
        public void m1() {

        }


    }

    static class Supplier2 implements Supplier<Integer> {

        @Override
        public Integer get() {
            return 2;
        }
    }

    static class Lira1 {
        int m1(int a) {
            return a;
        }
    }
}
