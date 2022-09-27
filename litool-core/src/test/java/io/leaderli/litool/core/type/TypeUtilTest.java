import io.leaderli.litool.core.internal.ParameterizedTypeImpl;
import io.leaderli.litool.core.type.TypeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    void checkNotPrimitive() {

        Assertions.assertThrows(RuntimeException.class, () -> TypeUtil.checkNotPrimitive(int.class));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(Integer.class));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(null));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(ParameterizedTypeImpl.make(null, ArrayList.class, String.class)));
        Assertions.assertDoesNotThrow(() -> TypeUtil.checkNotPrimitive(List.class.getTypeParameters()[0]));
    }


    @Test
    void isUnknown() {

        Assertions.assertTrue(TypeUtil.isUnknown(Consumer.class.getTypeParameters()[0]));
    }

    @Test
    void erase() {


        assertSame(null, TypeUtil.erase(null));
        assertSame(Object.class, TypeUtil.erase(Object.class));
        assertSame(Object[].class, TypeUtil.erase(Object[].class));

        ParameterizedType genericSuperclass = (ParameterizedType) TestType.class.getGenericInterfaces()[0];
        Type actualTypeArgument = genericSuperclass.getActualTypeArguments()[0];
        assertSame(String.class, TypeUtil.erase(actualTypeArgument));


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


    private static class TestType implements Consumer<String> {


        @Override
        public void accept(String s) {

        }
    }

    @Test
    void resolve() throws NoSuchFieldException {


        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve(In4.class, In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class, String.class}, TypeUtil.resolve(In3.class, In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{Object.class, String.class}, TypeUtil.resolve(In2.class, In1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class}, TypeUtil.resolve(In4.class, Consumer.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class}, TypeUtil.resolve(In3.class, Consumer.class).getActualClassArguments());
        assertArrayEquals(new Object[]{Object.class}, TypeUtil.resolve(In2.class, Consumer.class).getActualClassArguments());

        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve(Ge4.class, Ge1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{ArrayList.class, String.class}, TypeUtil.resolve(Ge3.class, Ge1.class).getActualClassArguments());
        assertArrayEquals(new Object[]{List.class, String.class}, TypeUtil.resolve(Ge2.class, Ge1.class).getActualClassArguments());


        assertArrayEquals(new Object[]{Object.class, Consumer.class}, TypeUtil.resolve(In5.class, In1.class).getActualClassArguments());


        Consumer<?> consumer = new StringConsumer();


        List<String> list = new ArrayList<String>() {
        };
        Map<String, String> map = new HashMap<String, String>() {
        };

        assertEquals(String.class, TypeUtil.resolve(consumer.getClass(), Consumer.class).getActualClassArgument().get());
        assertSame(String.class, TypeUtil.resolve(list.getClass(), ArrayList.class).getActualClassArgument().get());
        assertTrue(TypeUtil.resolve(Object.class, Object.class).getActualClassArgument(1).absent());
        assertThrows(NullPointerException.class, () -> TypeUtil.resolve(Object.class, null).getActualClassArgument(1).absent());
        assertTrue(TypeUtil.resolve(null, Object.class).getActualClassArgument(-1).absent());
        assertSame(String.class, TypeUtil.resolve(list.getClass(), ArrayList.class).getActualClassArgument(0).get());
        assertTrue(TypeUtil.resolve(list.getClass(), ArrayList.class).getActualClassArgument(1).absent());
        assertSame(String.class, TypeUtil.resolve(map.getClass(), HashMap.class).getActualClassArgument(0).get());
        assertSame(String.class, TypeUtil.resolve(map.getClass(), HashMap.class).getActualClassArgument(1).get());


    }

    @Test
    void test() throws NoSuchFieldException {
        Type declare = new Li<String>() {
        }.getClass();

        Type t = Li.class.getField("t").getGenericType();
        Type ts = Li.class.getField("ts").getGenericType();
        Type tss = Li.class.getField("tss").getGenericType();
        Type lt = Li.class.getField("lt").getGenericType();


        Assertions.assertEquals(String.class, TypeUtil.resolve(declare, t));
        Assertions.assertEquals(String[].class, TypeUtil.resolve(declare, ts));
        Assertions.assertEquals(String[][].class, TypeUtil.resolve(declare, tss));
        Assertions.assertEquals(ParameterizedTypeImpl.make(null, List.class, String.class), TypeUtil.resolve(declare, lt));

    }

    @Test
    void field() throws NoSuchFieldException {
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
//        Assertions.assertEquals(List.class, TypeUtil.resolve(make1, List.class).getActualClassArgument().get());

        ParameterizedTypeImpl make1 = ParameterizedTypeImpl.make(null, List.class, List.class);

        Assertions.assertEquals(List.class, TypeUtil.resolve(make1, List.class).getActualClassArgument().get());
        Assertions.assertEquals(List.class, TypeUtil.resolve(make1, List.class.getTypeParameters()[0]));


    }


    private static class Li<T> {
        public List<T> lt;
        public T[][] tss;
        public T[] ts;
        public T t;
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

    ;

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

    private static class Ge2<C extends List> extends Ge1<C, String> {

    }

    private static class Ge3 extends Ge2<ArrayList> {

    }

    private static class Ge4 extends Ge3 {

    }
}
