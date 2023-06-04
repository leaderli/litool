import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.core.type.TypeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/6/27
 */
@SuppressWarnings("ALL")
class TypeUtilTest {


    static class Z1<T extends List> {

    }

    static class Z2<T extends List<String>> {

    }

    static class Z3<T> {

    }

    static class Z4<T extends Z3<Z4>> {

    }

    static class Z5<T extends Z5> {
    }


    @Test
    void resolveTypeVariable() {

        Type type = ReflectUtil.getField(T4.T3.class, "t").get().getGenericType();
        Assertions.assertSame(Object.class, TypeUtil.resolveTypeVariable(T4.class, (TypeVariable<?>) type));
        Assertions.assertSame(String.class, TypeUtil.resolveTypeVariable(T5.class, (TypeVariable<?>) type));
//        Assertions.assertSame(T1[].class, TypeUtil.resolveTypeVariable(T2.class, List.class.getTypeParameters()[0]));
        Assertions.assertSame(ArrayList.class, TypeUtil.erase(TypeUtil.resolveTypeVariable(T2.class, T1.class.getTypeParameters()[0])));

        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, Object.class), TypeUtil.resolveTypeVariable(Z1.class, Z1.class.getTypeParameters()[0]));
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, String.class), TypeUtil.resolveTypeVariable(Z2.class, Z2.class.getTypeParameters()[0]));
    }

    @Test
    void test1() {
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, String.class), TypeUtil.resolveTypeVariable(Z2.class, Z2.class.getTypeParameters()[0]));

    }


    @Test
    void expansionTypeVariable() {

        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();
        TypeUtil.expandTypeVariables(T2.class.getGenericInterfaces()[0], visitedTypeVariables);

        Assertions.assertEquals(4, visitedTypeVariables.size());


        visitedTypeVariables.clear();
        Type declare = ParameterizedTypeImpl.make(Z2.class);
        TypeUtil.expandTypeVariables(declare, visitedTypeVariables);
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, String.class), visitedTypeVariables.get(Z2.class.getTypeParameters()[0]));
        Assertions.assertDoesNotThrow(() -> TypeUtil.expandTypeVariables(Z4.class, new HashMap<>()));
        Assertions.assertDoesNotThrow(() -> TypeUtil.expandTypeVariables(Z5.class.getTypeParameters()[0], new HashMap<>()));

    }

    @Test
    void test3() {
        TypeUtil.expandTypeVariables(Z4.class, new HashMap<>());

    }


    @Test
    void isUnknown() {

        Assertions.assertTrue(TypeUtil.isUnknown(Consumer.class.getTypeParameters()[0]));
        Assertions.assertFalse(TypeUtil.isUnknown(Consumer.class));
    }

    @Test
    void erase() {


        assertSame(null, TypeUtil.erase(null));
        assertSame(Object.class, TypeUtil.erase(Object.class));
        assertSame(Object[].class, TypeUtil.erase(Object[].class));

        ParameterizedType genericSuperclass = (ParameterizedType) TestType.class.getGenericInterfaces()[0];
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        assertSame(String.class, TypeUtil.erase(actualTypeArgument));


        Assertions.assertSame(Supplier.class, TypeUtil.erase(T1.class.getGenericInterfaces()[0]));

        Assertions.assertSame(Consumer.class, TypeUtil.erase(T1.class.getGenericInterfaces()[1]));


        Field cls = ReflectUtil.getField(T1.class, "cls").get();
        Assertions.assertSame(Class.class, TypeUtil.erase(cls.getGenericType()));

        cls = ReflectUtil.getField(T1.class, "cls2").get();
        Assertions.assertSame(Class.class, TypeUtil.erase(cls.getGenericType()));


    }

    @Test
    void resolve() throws NoSuchFieldException {

        assertArrayEquals(new Object[]{ArrayList.class},
                TypeUtil.resolve2Parameterized(T2.class, T1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class},
                TypeUtil.resolve2Parameterized(T2.class, T1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve2Parameterized(In4.class,
                In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class, String.class}, TypeUtil.resolve2Parameterized(In3.class,
                In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{Object.class, String.class}, TypeUtil.resolve2Parameterized(In2.class,
                In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class},
                TypeUtil.resolve2Parameterized(In4.class, Consumer.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class},
                TypeUtil.resolve2Parameterized(In3.class, Consumer.class).getActualClassArguments());
        assertArrayEquals(new Object[]{Object.class},
                TypeUtil.resolve2Parameterized(In2.class, Consumer.class).getActualClassArguments());

        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve2Parameterized(Ge4.class,
                Ge1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve2Parameterized(Ge3.class,
                Ge1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class, String.class}, TypeUtil.resolve2Parameterized(Ge2.class,
                Ge1.class).getActualClassArguments());


        assertArrayEquals(new Object[]{Object.class, Consumer.class}, TypeUtil.resolve2Parameterized(In5.class,
                In1.class).getActualClassArguments());


        Consumer<?> consumer = new StringConsumer();


        List<String> list = new ArrayList<String>() {
        };
        Map<String, String> map = new HashMap<String, String>() {
        };

        assertEquals(String.class,
                TypeUtil.resolve2Parameterized(consumer.getClass(), Consumer.class).getActualClassArgument().get());
        assertSame(String.class,
                TypeUtil.resolve2Parameterized(list.getClass(), ArrayList.class).getActualClassArgument().get());
        assertTrue(TypeUtil.resolve2Parameterized(Object.class, Object.class).getActualClassArgument(1).absent());
        assertThrows(NullPointerException.class,
                () -> TypeUtil.resolve2Parameterized(Object.class, null).getActualClassArgument(1).absent());
        assertTrue(TypeUtil.resolve2Parameterized(null, Object.class).getActualClassArgument(-1).absent());
        assertSame(String.class,
                TypeUtil.resolve2Parameterized(list.getClass(), ArrayList.class).getActualClassArgument(0).get());
        assertTrue(TypeUtil.resolve2Parameterized(list.getClass(), ArrayList.class).getActualClassArgument(1).absent());
        assertSame(String.class,
                TypeUtil.resolve2Parameterized(map.getClass(), HashMap.class).getActualClassArgument(0).get());
        assertSame(String.class,
                TypeUtil.resolve2Parameterized(map.getClass(), HashMap.class).getActualClassArgument(1).get());


    }

    @Test
    void resolve2() throws NoSuchFieldException, NoSuchMethodException {
        Type declare = new Li<String>() {
        }.getClass();

        Type t = Li.class.getField("t").getGenericType();
        Type ts = Li.class.getField("ts").getGenericType();
        Type tss = Li.class.getField("tss").getGenericType();
        Type lt = Li.class.getField("lt").getGenericType();
        Type mt = Li.class.getMethod("li").getGenericReturnType();


        Assertions.assertEquals(String.class, TypeUtil.resolve(declare, t));
        Assertions.assertEquals(String.class, TypeUtil.resolve(declare, mt));
        Assertions.assertEquals(String[].class, TypeUtil.resolve(declare, ts));
        Assertions.assertEquals(String[][].class, TypeUtil.resolve(declare, tss));


        ParameterizedTypeImpl resolve = (ParameterizedTypeImpl) TypeUtil.resolve(declare, lt);
        ParameterizedTypeImpl make = ParameterizedTypeImpl.make(null, List.class, String.class);

        Assertions.assertEquals(make, resolve);

    }

    @Test
    void checkNotPrimitive() {

        Assertions.assertThrows(RuntimeException.class, () -> TypeUtil.checkNotPrimitive(int.class));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(Integer.class));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(null));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(ParameterizedTypeImpl.make(null,
                ArrayList.class, String.class)));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(List.class.getTypeParameters()[0]));
    }

    @Test
    void testEquals() {


        assertFalse(TypeUtil.equals(Consumer.class.getTypeParameters()[0], Supplier.class.getTypeParameters()[0]));
        ParameterizedType left = (ParameterizedType) ArrayList.class.getGenericInterfaces()[0];
        ParameterizedType right = (ParameterizedType) AbstractList.class.getGenericInterfaces()[0];
        assertFalse(TypeUtil.equals(left, right));
        assertTrue(TypeUtil.equals(String.class, String.class));
        assertTrue(TypeUtil.equals(null, null));
        assertFalse(TypeUtil.equals(null, String.class));

    }

    @Test
    void field() throws NoSuchFieldException {

        TypeVariable<Class<T7>> typeParameter = T7.class.getTypeParameters()[0];
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, Object.class), TypeUtil.resolveTypeVariable(Object.class, typeParameter));
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, Object.class),
                TypeUtil.resolve(Object.class, typeParameter));

        Type resolve = TypeUtil.resolve(Li.class, Li.class.getTypeParameters()[0]);

        Type make = Object.class;
        Assertions.assertEquals(make, resolve);

        Type gt = Li.class.getField("lt").getGenericType();

        resolve = TypeUtil.resolve(new Li<String>() {
        }.getClass(), gt);
        make = ParameterizedTypeImpl.make(null, List.class, String.class);
        Assertions.assertEquals(make, resolve);

        Type ts = Li.class.getField("ts").getGenericType();
        resolve = TypeUtil.resolve(new Li<String>() {
        }.getClass(), ts);
        Assertions.assertEquals(String[].class, resolve);

        ParameterizedTypeImpl make1 = ParameterizedTypeImpl.make(null, List.class, List.class);

        Assertions.assertEquals(List.class,
                TypeUtil.resolve2Parameterized(make1, List.class).getActualClassArgument().get());
        Assertions.assertEquals(List.class, TypeUtil.erase(TypeUtil.resolve(make1, List.class.getTypeParameters()[0])));


    }

    class T1<T extends List> implements Supplier<List<? extends T>>, Consumer<T[]> {
        public Class<?> cls;
        public Class<? super T> cls2;


        @Override
        public List<T> get() {
            return null;
        }

        @Override
        public void accept(T[] ts) {

        }
    }

    private interface T2 extends Supplier<List<T1<ArrayList>[]>[]> {

    }

    private class T4<T> {
        private class T3 {

            T t;
        }
    }

    private class T5<T> extends T4<String> {

    }

    private interface T6<T> {

    }

    private class T7<T extends List> {

    }


    private interface T8 extends T6<String> {

    }

    private static interface In1<A, B> {

    }

    private static interface In2<A> extends In1<A, String>, Consumer<A> {

    }

    private static interface In5<A> extends In1<A, Consumer<? extends Number>> {

    }


    private static interface Con<A> extends In1<A, Consumer<A>> {

    }


    private static interface Con2 extends Con<String> {
    }

    private static class TestType implements Consumer<String> {


        @Override
        public void accept(String s) {

        }
    }

    private static class Li<T> {
        public List<T> lt;
        public T[][] tss;
        public T[] ts;
        public T t;

        public T li() {
            return t;
        }

    }

    private static class StringConsumer implements Consumer<String> {
        @Override
        public void accept(String o) {

        }
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

    ;

    private static class Ge2<C extends List> extends Ge1<C, String> {

    }

    private static class Ge3 extends Ge2<ArrayList> {

    }

    private static class Ge4 extends Ge3 {

    }


    @Test
    void test() {
        Type getResponse = ReflectUtil.getMethod(ZA.class, "getResponse").get().getGenericReturnType();
        Assertions.assertEquals(ParameterizedTypeImpl.make(ZB.class, ZB.ZB_Response.class, ZD.ZD_Body.class), ParameterizedTypeImpl.make(TypeUtil.resolve(ZD.class, getResponse)));

    }

    private static class ZD extends ZB<ZD.ZD_Body> {

        private static class ZD_Body {

        }
    }

    private static class ZB<RB> extends ZA<ZB.ZB_Response<RB>> {

        private static class ZB_Response<ZBR> {

            public ZBR getBody() {
                return null;
            }
        }
    }

    private static class ZA<R> {
        public R getResponse() {
            return null;
        }
    }
}
