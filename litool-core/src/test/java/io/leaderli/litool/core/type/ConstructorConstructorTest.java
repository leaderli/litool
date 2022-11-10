package io.leaderli.litool.core.type;

import io.leaderli.litool.core.collection.LiMapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author leaderli
 * @since 2022/9/25
 */
class ConstructorConstructorTest {

    @Test
    void test() {
        LinkedHashMap<Type, InstanceCreator<?>> head = LiMapUtil.newLinkedHashMap(LinkedHashMap.class, (InstanceCreator<LinkedHashMap<?, ?>>) type -> new LinkedHashMap<>());
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(head, new LinkedHashMap<>());
        Assertions.assertSame(LinkedHashMap.class, constructorConstructor.get(LiTypeToken.of(Map.class)).get().getClass());

        LinkedHashMap<Type, InstanceCreator<?>> tail = LiMapUtil.newLinkedHashMap(HashMap.class, (InstanceCreator<HashMap<?, ?>>) type -> new HashMap<>());
        constructorConstructor = new ConstructorConstructor(tail);
        Assertions.assertSame(LinkedHashMap.class, constructorConstructor.get(LiTypeToken.of(Map.class)).get().getClass());

        constructorConstructor = new ConstructorConstructor();
        Assertions.assertSame(HashMap.class, constructorConstructor.get(LiTypeToken.of(Map.class)).get().getClass());

        tail = LiMapUtil.newLinkedHashMap(LiTypeToken.getParameterized(Map.class, String.class, String.class), (InstanceCreator<MyMap>) type -> new MyMap());
        constructorConstructor = new ConstructorConstructor(tail);
        Assertions.assertSame(MyMap.class, constructorConstructor.get(LiTypeToken.getParameterized(Map.class, String.class, String.class)).get().getClass());
    }

    @Test
    void get() {
        LinkedHashMap<Type, InstanceCreator<?>> factories = new LinkedHashMap<>();
        factories.put(AbstractList.class, (InstanceCreator<List<Object>>) type -> {
            List<Object> list = new ArrayList<>();
            list.add("");
            return list;
        });
        factories.put(ArrayList.class, (InstanceCreator<List<Object>>) type -> new ArrayList<>());
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(factories);

        ObjectConstructor<ArrayList<String>> arrayListSupplier = constructorConstructor.get(LiTypeToken.of(ArrayList.class));
        List<String> actual = arrayListSupplier.get();
        Assertions.assertTrue(actual.isEmpty());

        ObjectConstructor<List<String>> objectObjectConstructor = constructorConstructor.get(new LiTypeToken<List<String>>() {
        });

        List<String> list = objectObjectConstructor.get();
        Assertions.assertTrue(list.iterator().hasNext());
        Assertions.assertDoesNotThrow(() -> list.iterator().next().getBytes());

        Assertions.assertNull(constructorConstructor.get(LiTypeToken.of(A.class)));

    }

    private static class A {
    }

    private static class MyMap extends HashMap<String, String> {

    }

}
