package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author leaderli
 * @since 2022/9/24 9:39 AM
 */
public class Lean {
    private final Map<LiTypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();
    public final Lira<LeanFieldKey> reflect_name_handlers;
    private final ConstructorConstructor constructorConstructor;
    private final List<TypeAdapterFactory> factories;


    public Lean() {
        this(new LinkedHashMap<>(), null);
    }

    public Lean(LinkedHashMap<Type, InstanceCreator<?>> instanceCreators, List<LeanFieldKey> reflect_name_handlers) {

        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
        this.factories = initFactories();
        this.reflect_name_handlers = initLeanKeyHandlers(reflect_name_handlers);

    }

    private static Lira<LeanFieldKey> initLeanKeyHandlers(List<LeanFieldKey> reflect_name_handlers) {
        return CollectionUtils.union(reflect_name_handlers, defaultLeanKeyHandlers());
    }

    private static List<LeanFieldKey> defaultLeanKeyHandlers() {
        LeanFieldKey leanKey = field -> ReflectUtil.getAnnotation(field, LeanKey.class).map(LeanKey::value).get();
        LeanFieldKey fieldName = Field::getName;
        return CollectionUtils.of(leanKey, fieldName);
    }

    private static List<TypeAdapterFactory> initFactories() {
        List<TypeAdapterFactory> factories = new ArrayList<>();
        factories.add(TypeAdapters.PRIMITIVE_FACTORY);
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.ITERABLE_FACTORY);
        factories.add(TypeAdapters.MAP_FACTORY);
        factories.add(TypeAdapters.OBJECT_FACTORY);
        factories.add(TypeAdapters.REFLECT_FACTORY);
        return factories;
    }


    /**
     * @param source          the source bean
     * @param targetTypeToken the target bean typeToken
     * @param <T>             the parameter of {@link  LiTypeToken}
     * @return a new instance with type {@link LiTypeToken#getType()} which created by copy the properties of source deeply
     * @see #getAdapter(LiTypeToken)
     */
    public <T> T fromBean(Object source, LiTypeToken<T> targetTypeToken) {
        return getAdapter(targetTypeToken).read(source);
    }

    /**
     * @param type the key
     * @param <T>  the parameter of {@link  LiTypeToken}
     * @return try to get the value cached by {@link  #typeTokenCache}, if not cached
     * foreach {@link #factories} to find the a {@link  TypeAdapter} and cache
     * LiTypeToken-TypeAdapter to reuse
     */
    public <T> TypeAdapter<T> getAdapter(LiTypeToken<T> type) {
        Objects.requireNonNull(type);
        TypeAdapter<T> cached = getCacheAdapter(type);
        if (cached != null) {
            return cached;
        }


        for (TypeAdapterFactory factory : this.factories) {
            TypeAdapter<T> candidate = factory.create(this, type);
            if (candidate != null) {
                synchronized (typeTokenCache) {
                    cached = getCacheAdapter(type);
                    if (cached != null) {
                        return cached;
                    }
                    typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
        }

        throw new IllegalArgumentException("Lean cannot handle " + type);


    }

    /**
     * @param type the key
     * @param <T>  the parameter of {@link  LiTypeToken}
     * @return get the value from {@link #typeTokenCache} by the key
     */
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getCacheAdapter(LiTypeToken<T> type) {
        return (TypeAdapter<T>) typeTokenCache.get(type);
    }

    /**
     * @param source     the source bean
     * @param targetType the target bean type
     * @param <T>        the parameter of {@link  LiTypeToken}
     * @return a new instance created by copy the properties of source deeply
     * @see #getAdapter(Type)
     */
    @SuppressWarnings({"unchecked"})
    public <T> T fromBean(Object source, Type targetType) {
        return (T) getAdapter(targetType).read(source);
    }

    /**
     * {@code return getAdapter(LiTypeToken.of(type))}
     *
     * @param type the key
     * @param <T>  the parameter of {@link  LiTypeToken}
     * @return {@link  #getAdapter(LiTypeToken)}
     * @see #getAdapter(LiTypeToken)
     */
    public <T> TypeAdapter<T> getAdapter(Type type) {

        return getAdapter(LiTypeToken.of(type));
    }

    /**
     * {@code   constructorConstructor.get(typeToken)}
     *
     * @param typeToken the type token
     * @param <T>       the parameter of {@link  LiTypeToken}
     * @return a constructor like function provide a instance that match the {@link  LiTypeToken}
     * @see #constructorConstructor
     */
    public <T> ObjectConstructor<T> getConstructor(LiTypeToken<T> typeToken) {
        return constructorConstructor.get(typeToken);
    }


}
