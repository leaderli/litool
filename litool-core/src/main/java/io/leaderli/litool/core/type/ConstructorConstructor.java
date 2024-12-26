package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.lang.reflect.Type;
import java.util.*;


/**
 * 构造函数生成器，用于存储与{@link LiTypeToken}匹配的构造函数对象。如果构造函数生成器中不存在
 * 匹配的{@link LiTypeToken}，则会遍历所有的键，寻找第一个键的类型是{@link LiTypeToken#getRawType()}的超类。
 * 因此，构造函数生成器使用{@link LinkedHashMap}存储键值对以确保可以顺序。
 *
 * @author leaderli
 * @since 2022/9/25
 */
@SuppressWarnings("java:S1319")
public final class ConstructorConstructor {

    private final LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();

    public ConstructorConstructor() {
        this(new LinkedHashMap<>());
    }

    /**
     * 构造函数生成器的带参构造函数。
     * 通过调用 {@link #ConstructorConstructor(LinkedHashMap, LinkedHashMap)} 构造函数初始化内部字段。
     *
     * @param tail 存储在构造函数生成器中的尾部的键值对
     */
    public ConstructorConstructor(LinkedHashMap<Type, InstanceCreator<?>> tail) {
        this(new LinkedHashMap<>(), tail);
    }

    /**
     * 构造函数生成器的带参构造函数。
     * 按照顺序加载
     * <ol>
     *     <li>
     *         head
     *   </li>
     *     <li>
     *        {@link #generateDefaultInstanceCreators()}
     *     </li>
     *     <li>
     *        tail
     *     </li>
     * </ol>
     * 后续的key不会覆盖前面已经加载的key
     *
     * @param head 存储在构造函数生成器中的头部的键值对
     * @param tail 存储在构造函数生成器中的尾部的键值对
     * @see Map#putIfAbsent(Object, Object)
     */
    public ConstructorConstructor(LinkedHashMap<Type, InstanceCreator<?>> head, LinkedHashMap<Type, InstanceCreator<?>> tail) {

        if (head != null) {
            head.forEach(instanceCreators::putIfAbsent);
        }
        generateDefaultInstanceCreators().forEach(instanceCreators::putIfAbsent);
        if (tail != null) {
            tail.forEach(instanceCreators::putIfAbsent);
        }

    }

    /**
     * 生成默认的键值对，包括
     * <ol>
     *     <li>
     * {@link ArrayList}
     *     </li>
     *     <li>
     * {@link HashSet}
     *     </li>
     *     <li>
     * {@link LinkedHashSet}
     *     </li>
     *     <li>
     * {@link HashMap}
     *     </li>
     *     <li>
     *          {@link LinkedHashMap}
     *     </li>
     * </ol>
     *
     * @return 默认的键值对
     */
    private static LinkedHashMap<Type, InstanceCreator<?>> generateDefaultInstanceCreators() {
        LinkedHashMap<Type, InstanceCreator<?>> instanceCreators = new LinkedHashMap<>();
        instanceCreators.put(Object.class, type -> new Object());
        instanceCreators.put(ArrayList.class, (InstanceCreator<List<Object>>) type -> new ArrayList<>());
        instanceCreators.put(LinkedList.class, (InstanceCreator<List<Object>>) type -> new LinkedList<>());
        instanceCreators.put(HashSet.class, (InstanceCreator<HashSet<Object>>) type -> new HashSet<>());
        instanceCreators.put(LinkedHashSet.class, (InstanceCreator<LinkedHashSet<Object>>) type -> new LinkedHashSet<>());
        instanceCreators.put(HashMap.class, (InstanceCreator<HashMap<Object, Object>>) type -> new HashMap<>());
        instanceCreators.put(LinkedHashMap.class, (InstanceCreator<HashMap<Object, Object>>) type -> new LinkedHashMap<>());
        return instanceCreators;
    }

    /**
     * 获取与给定 {@link LiTypeToken} 匹配的构造函数对象。
     *
     * @param <T>       构造函数对象的类型
     * @param typeToken 用于匹配构造函数对象的 {@link LiTypeToken}
     * @return 匹配的构造函数对象
     */
    @SuppressWarnings("unchecked")
    public <T> ObjectConstructor<T> get(LiTypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        InstanceCreator<T> typeCreator = (InstanceCreator<T>) instanceCreators.get(type);
        if (typeCreator != null) {
            return () -> typeCreator.createInstance(type);
        }

        Lino<ObjectConstructor<T>> rawConstructor = Lira.of(instanceCreators.keySet())
                .cast(Class.class)
                .filter(rawType::isAssignableFrom)
                .first()
                .map(instanceCreators::get)
                .map(r -> () -> (T) r.createInstance(type));

        return rawConstructor.get();
    }
}
