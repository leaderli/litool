package io.leaderli.litool.core.lang.lean;

import io.leaderli.litool.core.lang.lean.adapters.*;

public class TypeAdapterFactories {
    public static final TypeAdapterFactories INSTANCE = new Builder().build();
    public final TypeAdapterFactory PRIMITIVE_FACTORY;
    public final TypeAdapterFactory STRING_FACTORY;
    public final TypeAdapterFactory ENUM_FACTORY;
    public final TypeAdapterFactory ARRAY_FACTORY;
    public final TypeAdapterFactory ITERABLE_FACTORY;
    public final TypeAdapterFactory MAP_FACTORY;
    public final TypeAdapterFactory OBJECT_FACTORY;
    public final TypeAdapterFactory REFLECT_FACTORY;
    public final TypeAdapterFactory NULL_FACTORY;

    private TypeAdapterFactories(Builder builder) {
        this.PRIMITIVE_FACTORY = builder.PRIMITIVE_FACTORY;
        this.STRING_FACTORY = builder.STRING_FACTORY;
        this.ENUM_FACTORY = builder.ENUM_FACTORY;
        this.ARRAY_FACTORY = builder.ARRAY_FACTORY;
        this.ITERABLE_FACTORY = builder.ITERABLE_FACTORY;
        this.MAP_FACTORY = builder.MAP_FACTORY;
        this.OBJECT_FACTORY = builder.OBJECT_FACTORY;
        this.REFLECT_FACTORY = builder.REFLECT_FACTORY;
        this.NULL_FACTORY = builder.NULL_FACTORY;
    }

    public static class Builder {
        private TypeAdapterFactory PRIMITIVE_FACTORY = new PrimitiveTypeAdapterFactory();
        private TypeAdapterFactory STRING_FACTORY = new StringTypeAdapterFactory();
        private TypeAdapterFactory ENUM_FACTORY = new EnumTypeAdapterFactory();

        private TypeAdapterFactory ARRAY_FACTORY = new ArrayTypeAdapterFactory();
        private TypeAdapterFactory ITERABLE_FACTORY = new CollectionTypeAdapterFactory();
        private TypeAdapterFactory MAP_FACTORY = new MapTypeAdapterFactory();

        private TypeAdapterFactory OBJECT_FACTORY = new ObjectTypeAdapterFactory();
        private TypeAdapterFactory REFLECT_FACTORY = new ReflectTypeAdapterFactory();
        private TypeAdapterFactory NULL_FACTORY = new NullTypeAdapterFactory();

        public Builder withPrimitiveFactory(TypeAdapterFactory PRIMITIVE_FACTORY) {
            this.PRIMITIVE_FACTORY = PRIMITIVE_FACTORY;
            return this;
        }

        public Builder withStringFactory(TypeAdapterFactory STRING_FACTORY) {
            this.STRING_FACTORY = STRING_FACTORY;
            return this;
        }

        public Builder withEnumFactory(TypeAdapterFactory ENUM_FACTORY) {
            this.ENUM_FACTORY = ENUM_FACTORY;
            return this;
        }

        public Builder withArrayFactory(TypeAdapterFactory ARRAY_FACTORY) {
            this.ARRAY_FACTORY = ARRAY_FACTORY;
            return this;
        }

        public Builder withIterableFactory(TypeAdapterFactory ITERABLE_FACTORY) {
            this.ITERABLE_FACTORY = ITERABLE_FACTORY;
            return this;
        }

        public Builder withMapFactory(TypeAdapterFactory MAP_FACTORY) {
            this.MAP_FACTORY = MAP_FACTORY;
            return this;
        }

        public Builder withObjectFactory(TypeAdapterFactory OBJECT_FACTORY) {
            this.OBJECT_FACTORY = OBJECT_FACTORY;
            return this;
        }

        public Builder withReflectFactory(TypeAdapterFactory REFLECT_FACTORY) {
            this.REFLECT_FACTORY = REFLECT_FACTORY;
            return this;
        }

        public Builder withNullFactory(TypeAdapterFactory NULL_FACTORY) {
            this.NULL_FACTORY = NULL_FACTORY;
            return this;
        }

        public TypeAdapterFactories build() {
            return new TypeAdapterFactories(this);
        }
    }
}
