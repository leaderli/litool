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


    @Test
    void resolveTypeVariable() {

        Type type = ReflectUtil.getField(T4.T3.class, "t").get().getGenericType();
        Assertions.assertSame(Object.class, TypeUtil.resolveTypeVariable(T4.class, (TypeVariable<?>) type));
        Assertions.assertSame(String.class, TypeUtil.resolveTypeVariable(T5.class, (TypeVariable<?>) type));
        Assertions.assertSame(T1[].class, TypeUtil.resolveTypeVariable(T2.class, List.class.getTypeParameters()[0]));
        Assertions.assertSame(ArrayList.class, TypeUtil.resolveTypeVariable(T2.class, T1.class.getTypeParameters()[0]));
    }

    @Test
    void expansionTypeVariable() {

        Map<TypeVariable<?>, Type> visitedTypeVariables = new HashMap<>();
        TypeUtil.expandTypeVariables(T2.class.getGenericInterfaces()[0], visitedTypeVariables);
        Assertions.assertEquals(3, visitedTypeVariables.size());
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
    void resolve2() throws NoSuchFieldException {
        Type declare = new Li<String>() {
        }.getClass();

        Type t = Li.class.getField("t").getGenericType();
        Type ts = Li.class.getField("ts").getGenericType();
        Type tss = Li.class.getField("tss").getGenericType();
        Type lt = Li.class.getField("lt").getGenericType();


        Assertions.assertEquals(String.class, TypeUtil.resolve(declare, t));
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
        Assertions.assertEquals(List.class, TypeUtil.resolveTypeVariable(Object.class, typeParameter));
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
        Assertions.assertEquals(List.class, TypeUtil.resolve(make1, List.class.getTypeParameters()[0]));


    }

    private interface T2 extends Supplier<List<T1<ArrayList>[]>[]> {

    }

    private interface T6<T> {

    }

    private interface T8 extends T6<String> {

    }

    private static interface In2<A> extends In1<A, String>, Consumer<A> {

    }

    private static interface In5<A> extends In1<A, Consumer<? extends Number>> {

    }

    private static interface In1<A, B> {

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

    private class T7<T extends List> {

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

    private class T5<T> extends T4<String> {

    }

    private class T4<T> {
        private class T3 {

            T t;
        }
    }
}
