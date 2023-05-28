package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.lean.adapters.*;

/**
 * @author leaderli
 * @since 2022/9/24 2:50 PM
 */
public class TypeAdapterFactories {

    public static final TypeAdapterFactory PRIMITIVE_FACTORY = new PrimitiveTypeAdapterFactory();
    public static final TypeAdapterFactory STRING_FACTORY = new StringTypeAdapterFactory();

    public static final TypeAdapterFactory ARRAY_FACTORY = new ArrayTypeAdapterFactory();
    public static final TypeAdapterFactory ITERABLE_FACTORY = new CollectionTypeAdapterFactory();
    public static final TypeAdapterFactory MAP_FACTORY = new MapTypeAdapterFactory();


    public static final TypeAdapterFactory OBJECT_FACTORY = new ObjectTypeAdapterFactory();
    public static final TypeAdapterFactory REFLECT_FACTORY = new ReflectTypeAdapterFactory();


}
