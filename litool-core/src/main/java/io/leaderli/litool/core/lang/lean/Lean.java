package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.BeanPath;
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
    private final List<TypeAdapterFactory> factories = new ArrayList<>();
    private final Map<LiTypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();
    private final ConstructorConstructor constructorConstructor;

    public Lean() {
        this(new LinkedHashMap<>());
    }

    public Lean(LinkedHashMap<Type, InstanceCreator<?>> instanceCreators) {

        this.constructorConstructor = new ConstructorConstructor(instanceCreators);

        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.ITERABLE_FACTORY);
        factories.add(TypeAdapters.MAP_FACTORY);
        factories.add(TypeAdapters.OBJECT_FACTORY);
        factories.add(TypeAdapters.REFLECT_FACTORY);

    }

    public void populate(Type declare, Object bean, Object properties) {

        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            BeanPath.parse(properties, field.getName())
                    .map(value -> parser(value, TypeUtil.resolve(declare, field.getGenericType())))
                    .ifPresent(v -> ReflectUtil.setFieldValue(bean, field, v));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getCacheAdapter(LiTypeToken<T> type) {
        return (TypeAdapter<T>) typeTokenCache.get(type);
    }

    public <T> TypeAdapter<T> getAdapter(Type type) {

        return getAdapter(LiTypeToken.of(type));
    }

    @SuppressWarnings({"unchecked"})
    public <T> TypeAdapter<T> getAdapter(LiTypeToken<T> type) {
        Objects.requireNonNull(type);
        TypeAdapter<?> cached = typeTokenCache.get(type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }

        synchronized (typeTokenCache) {

            for (TypeAdapterFactory factory : this.factories) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
        }

        throw new IllegalArgumentException("Lean cannot handle " + type);


    }

    public <T> T parser(Object o, LiTypeToken<T> typeToken) {
        return getAdapter(typeToken).read(o);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> T parser(Object o, Type parser) {
        for (TypeAdapterFactory factory : factories) {
            LiTypeToken liTypeToken = LiTypeToken.of(parser);
            TypeAdapter<T> typeAdapter = factory.create(this, liTypeToken);
            if (typeAdapter != null) {
                return typeAdapter.read(o);
            }
        }

        return null;


    }

    public <T> ObjectConstructor<T> get(LiTypeToken<T> typeToken) {
        return constructorConstructor.get(typeToken);
    }


}
