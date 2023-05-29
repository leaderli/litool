package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.collection.CollectionUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.lang.lean.adapters.CustomTypeAdapterFactory;
import io.leaderli.litool.core.lang.lean.adapters.ReflectTypeAdapterFactory;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * liTODO
 * 枚举支持
 * 添加执行的细节
 * 用于对象转换的工具类
 *
 * @since 2022/9/24 9:39 AM
 */
public class Lean {
    public final Lira<LeanKeyHandler> leanKeyHandlers;
    public final Map<Class<? extends TypeAdapter<?>>, LiTuple2<TypeAdapter<?>, Type>> leanValueHandlers = new HashMap<>();
    private final List<TypeAdapterFactory> typeAdapterFactories = new ArrayList<>();
    private final Map<LiTypeToken<?>, TypeAdapter<?>> typeTokenAdapterCache = new ConcurrentHashMap<>();
    private final ConstructorConstructor constructorConstructor;
    public final CustomTypeAdapterFactory customTypeAdapterFactory = new CustomTypeAdapterFactory();


    private static Lira<LeanKeyHandler> initLeanKeyHandlers(List<LeanKeyHandler> reflect_name_handlers) {
        return CollectionUtils.union(LeanKeyHandler.class, reflect_name_handlers, defaultLeanKeyHandlers());
    }

    private static List<LeanKeyHandler> defaultLeanKeyHandlers() {
        LeanKeyHandler leanKeyHandler = field -> ReflectUtil.getAnnotation(field, LeanKey.class).map(LeanKey::value).get();
        LeanKeyHandler fieldNameAdapter = Field::getName;
        return CollectionUtils.toList(leanKeyHandler, fieldNameAdapter);
    }

    public Lean() {
        this(new LinkedHashMap<>(), null);
    }

    public Lean(LinkedHashMap<Type, InstanceCreator<?>> instanceCreators, List<LeanKeyHandler> leanKeyHandlers) {

        this(instanceCreators, leanKeyHandlers, false);
    }

    /**
     * @param instanceCreators 自定义的构造器
     * @param leanKeyHandlers  key的解析函数
     * @param strict           当处于严格模式，当类无{@link  TypeAdapter}时，会抛出 {@link  IllegalArgumentException}
     * @see #getTypeAdapter(LiTypeToken)
     */
    public Lean(LinkedHashMap<Type, InstanceCreator<?>> instanceCreators, List<LeanKeyHandler> leanKeyHandlers, boolean strict) {

        this.constructorConstructor = new ConstructorConstructor(instanceCreators);

        typeAdapterFactories.add(TypeAdapterFactories.PRIMITIVE_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.STRING_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.ENUM_FACTORY);

        typeAdapterFactories.add(TypeAdapterFactories.ARRAY_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.ITERABLE_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.MAP_FACTORY);

        typeAdapterFactories.add(customTypeAdapterFactory);

        typeAdapterFactories.add(TypeAdapterFactories.OBJECT_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.OBJECT_FACTORY);
        typeAdapterFactories.add(TypeAdapterFactories.REFLECT_FACTORY);

        if (!strict) {
            typeAdapterFactories.add(TypeAdapterFactories.NULL_FACTORY);
        }

        this.leanKeyHandlers = initLeanKeyHandlers(leanKeyHandlers);
    }


    @SafeVarargs
    public final <T> void addCustomTypeAdapter(TypeAdapter<T> typeAdapter, Class<? super T>... classes) {
        for (Class<? super T> cls : classes) {
            customTypeAdapterFactory.put(LiTypeToken.of(cls), typeAdapter);
        }

    }


    /**
     * 从源对象中创建目标对象
     *
     * @param source     源对象
     * @param targetType 目标对象的类型
     * @param <T>        目标对象的类型
     * @return 一个目标类型的实例，属性值与源对象相同
     * @see #getTypeAdapter(Type)
     */
    @SuppressWarnings({"unchecked"})
    public <T> T fromBean(Object source, Class<T> targetType) {
        return (T) getTypeAdapter(targetType).read(source, this);
    }


    /**
     * 从源对象中创建目标对象
     *
     * @param source     源对象
     * @param targetType 目标对象的类型
     * @param <T>        目标对象的类型
     * @return 一个目标类型的实例，属性值与源对象相同
     * @see #getTypeAdapter(Type)
     */
    @SuppressWarnings({"unchecked"})
    public <T> T fromBean(Object source, Type targetType) {
        return (T) getTypeAdapter(targetType).read(source, this);
    }

    /**
     * 从源对象中创建目标对象
     *
     * @param source          源对象
     * @param targetTypeToken 目标对象的 TypeToken
     * @param <T>             目标对象的类型
     * @return 一个新的实例，类型为 targetTypeToken 的类型，属性值与源对象相同
     * @see #getTypeAdapter(LiTypeToken)
     */
    public <T> T fromBean(Object source, LiTypeToken<T> targetTypeToken) {
        return getTypeAdapter(targetTypeToken).read(source, this);
    }

    /**
     * 从源对象中复制属性到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T>    目标对象的类型
     */
    public <T> void copyBean(Object source, T target, LiTypeToken<T> typeToken) {

        TypeAdapter<T> adapter = getTypeAdapter(typeToken);
        LiAssertUtil.assertTrue(adapter instanceof ReflectTypeAdapterFactory.ReflectAdapter, "only support copy to pojo bean");
        //noinspection unchecked
        ((ReflectTypeAdapterFactory.ReflectAdapter<Object>) adapter).populate(source, target, this);
    }


    /**
     * Lean对象的类型适配器方法，用于获取指定类型的适配器
     *
     * @param type 要获取适配器的类型
     * @param <T>  类型参数
     * @return 指定类型的适配器
     * @see #getTypeAdapter(LiTypeToken)
     */
    public <T> TypeAdapter<T> getTypeAdapter(Type type) {

        return getTypeAdapter(LiTypeToken.of(type));
    }

    /**
     * Lean对象的类型适配器方法，用于获取指定类型的适配器
     *
     * @param typeToken 类型令牌，用于获取适配器
     * @param <T>       类型参数
     * @return 指定类型的适配器
     * @throws IllegalArgumentException 如果无法获取指定类型的适配器，则抛出此异常
     */
    public <T> TypeAdapter<T> getTypeAdapter(LiTypeToken<T> typeToken) {
        Objects.requireNonNull(typeToken);
        TypeAdapter<T> cachedAdapter = getCacheTypeAdapter(typeToken);
        if (cachedAdapter != null) {
            return cachedAdapter;
        }


        for (TypeAdapterFactory typeAdapterFactory : this.typeAdapterFactories) {
            TypeAdapter<T> candidate = typeAdapterFactory.create(this, typeToken);
            if (candidate != null) {
                synchronized (typeTokenAdapterCache) {
                    cachedAdapter = getCacheTypeAdapter(typeToken);
                    if (cachedAdapter != null) {
                        return cachedAdapter;
                    }
                    typeTokenAdapterCache.put(typeToken, candidate);
                    return candidate;
                }
            }
        }

        throw new IllegalArgumentException("Lean cannot handle " + typeToken);


    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getCacheTypeAdapter(LiTypeToken<T> type) {
        return (TypeAdapter<T>) typeTokenAdapterCache.get(type);
    }


    /**
     * 获取用于创建指定类型对象的构造函数
     *
     * @param typeToken 类型令牌，用于获取构造函数
     * @param <T>       类型参数
     * @return 构造函数，用于创建指定类型的对象
     * @see #constructorConstructor
     */
    public <T> ObjectConstructor<T> getConstructor(LiTypeToken<T> typeToken) {
        return constructorConstructor.get(typeToken);
    }


}
