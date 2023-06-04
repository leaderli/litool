/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.leaderli.litool.core.type;


import io.leaderli.litool.core.internal.ParameterizedTypeImpl;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * <p>For example, to create a type literal for {@code List<String>}, you can
 * create an empty anonymous inner class:
 *
 * <p>
 * {@code LiTypeToken<List<String>> list = new LiTypeToken<List<String>>() {};}
 *
 * <p>This syntax cannot be used to create type literals that have wildcard
 * parameters, such as {@code Class<?>} or {@code List<? extends CharSequence>}.
 *
 * @param <T> the type parameter
 * @author Bob Lee
 * @author Sven Mawson
 * @author Jesse Wilson <p> copy form gson
 */
public class LiTypeToken<T> implements ParameterizedType {
    private final Type type;
    private final Class<? super T> rawType;
    private final int hashCode;

    /**
     * Constructs a new type literal. Derives represented class from type
     * parameter.
     *
     * <p>Clients create an empty anonymous subclass. Doing so embeds the type
     * parameter in the anonymous class's type hierarchy so we can reconstitute it
     * at runtime despite erasure.
     */
    @SuppressWarnings("unchecked")
    protected LiTypeToken() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) TypeUtil.erase(type);
        this.hashCode = type.hashCode();
    }

    /**
     * Unsafe. Constructs a type literal manually.
     *
     * @param type -
     */
    @SuppressWarnings("unchecked")
    LiTypeToken(Type type) {
        this.type = TypeUtil.canonicalize(Objects.requireNonNull(type));
        this.rawType = (Class<? super T>) TypeUtil.erase(this.type);
        this.hashCode = this.type.hashCode();
    }

    /**
     * Returns the type from super class's type parameter in {@link TypeUtil#canonicalize
     * canonical form}*.
     *
     * @param subclass -
     * @return -
     */
    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return TypeUtil.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * Private helper function that performs some assignability checks for
     * the provided GenericArrayType.
     *
     * @param from -
     * @param to   -
     * @return -
     */
    private static boolean isAssignableFrom(Type from, GenericArrayType to) {
        Type toGenericComponentType = to.getGenericComponentType();
        if (toGenericComponentType instanceof ParameterizedType) {
            Type t = from;
            if (from instanceof GenericArrayType) {
                t = ((GenericArrayType) from).getGenericComponentType();
            } else if (from instanceof Class<?>) {
                Class<?> classType = (Class<?>) from;
                while (classType.isArray()) {
                    classType = classType.getComponentType();
                }
                t = classType;
            }
            return isAssignableFrom(t, (ParameterizedType) toGenericComponentType,
                    new HashMap<>());
        }
        // No generic defined on "to"; therefore, return true and let other
        // checks determine assignability
        return true;
    }

    /**
     * Private recursive helper function to actually do the type-safe checking
     * of assignability.
     *
     * @param from       -
     * @param to         -
     * @param typeVarMap -
     * @return -
     */
    private static boolean isAssignableFrom(Type from, ParameterizedType to,
                                            Map<String, Type> typeVarMap) {

        if (from == null) {
            return false;
        }

        if (to.equals(from)) {
            return true;
        }

        // First figure out the class and any type information.
        Class<?> clazz = TypeUtil.erase(from);
        ParameterizedType ptype = null;
        if (from instanceof ParameterizedType) {
            ptype = (ParameterizedType) from;
        }

        // Load up parameterized variable info if it was parameterized.
        if (ptype != null) {
            Type[] tArgs = ptype.getActualTypeArguments();
            TypeVariable<?>[] tParams = clazz.getTypeParameters();
            for (int i = 0; i < tArgs.length; i++) {
                Type arg = tArgs[i];
                TypeVariable<?> va = tParams[i];
                while (arg instanceof TypeVariable<?>) {
                    TypeVariable<?> v = (TypeVariable<?>) arg;
                    arg = typeVarMap.get(v.getName());
                }
                typeVarMap.put(va.getName(), arg);
            }

            // check if they are equivalent under our current mapping.
            if (typeEquals(ptype, to, typeVarMap)) {
                return true;
            }
        }

        for (Type itype : clazz.getGenericInterfaces()) {
            if (isAssignableFrom(itype, to, new HashMap<>(typeVarMap))) {
                return true;
            }
        }

        // Interfaces didn't work, try the superclass.
        Type sType = clazz.getGenericSuperclass();
        return isAssignableFrom(sType, to, new HashMap<>(typeVarMap));
    }

    /**
     * Checks if two parameterized types are exactly equal, under the variable
     * replacement described in the typeVarMap.
     *
     * @param from       -
     * @param to         -
     * @param typeVarMap -
     * @return -
     */
    private static boolean typeEquals(ParameterizedType from,
                                      ParameterizedType to, Map<String, Type> typeVarMap) {
        if (from.getRawType().equals(to.getRawType())) {
            Type[] fromArgs = from.getActualTypeArguments();
            Type[] toArgs = to.getActualTypeArguments();
            for (int i = 0; i < fromArgs.length; i++) {
                if (!matches(fromArgs[i], toArgs[i], typeVarMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static AssertionError buildUnexpectedTypeError(Type token, Class<?>... expected) {

        // Build exception message
        StringBuilder exceptionMessage =
                new StringBuilder("Unexpected type. Expected one of: ");
        for (Class<?> clazz : expected) {
            exceptionMessage.append(clazz.getName()).append(", ");
        }
        exceptionMessage.append("but got: ").append(token.getClass().getName())
                .append(", for type token: ").append(token).append('.');

        return new AssertionError(exceptionMessage.toString());
    }

    /**
     * Checks if two types are the same or are equivalent under a variable mapping
     * given in the type map that was provided.
     *
     * @param from    -
     * @param to      -
     * @param typeMap -
     * @return -
     */
    private static boolean matches(Type from, Type to, Map<String, Type> typeMap) {
        return to.equals(from)
                || (from instanceof TypeVariable
                && to.equals(typeMap.get(((TypeVariable<?>) from).getName())));

    }

    /**
     * Gets type literal for the parameterized type represented by applying {@code typeArguments} to
     * {@code rawType}.
     *
     * @param <T>           the type parameter
     * @param rawType       the raw type
     * @param typeArguments the type arguments
     * @return the parameterized
     */
    public static <T> LiTypeToken<T> ofParameterized(Type rawType, Type... typeArguments) {
        return ofType(LiTypes.newParameterizedTypeWithOwner(null, rawType, typeArguments));
    }


    /**
     * Gets type literal for the given {@code Type} instance.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the li type token
     */
    public static <T> LiTypeToken<T> ofType(Type type) {
        return new LiTypeToken<>(type);
    }

    /**
     * Gets type literal for the given {@code Type} instance.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the li type token
     */
    public static <T> LiTypeToken<T> of(Class<T> type) {
        return new LiTypeToken<>(type);
    }

    /**
     * Gets type literal for the array type whose elements are all instances of {@code componentType}.
     *
     * @param componentType the componentType
     * @return the array
     */
    public static LiTypeToken<?> getArray(Type componentType) {
        return new LiTypeToken<>(LiTypes.arrayOf(componentType));
    }

    @Override
    public final Type[] getActualTypeArguments() {

        if (isParameterizedType()) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return ParameterizedTypeImpl.make(rawType).getActualTypeArguments();
    }

    /**
     * Is parameterized type boolean.
     *
     * @return {@code type instanceof ParameterizedType; }
     */
    public final boolean isParameterizedType() {
        return type instanceof ParameterizedType;
    }

    /**
     * @return the raw (non-generic) type for this type.
     */
    @Override
    public final Class<? super T> getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        if (isParameterizedType()) {
            return ((ParameterizedType) type).getOwnerType();
        }
        return null;
    }

    /**
     * Gets underlying {@code Type} instance.
     *
     * @return the type
     */
    public final Type getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public final Class<T> getGenericType() {
        return (Class<T>) rawType;
    }

    /**
     * Check if this type is assignable from the given class object.
     *
     * @param cls the cls
     * @return the boolean
     * @deprecated this implementation may be inconsistent with javac for types with wildcards.
     */
    @Deprecated
    public boolean isAssignableFrom(Class<?> cls) {
        return isAssignableFrom((Type) cls);
    }

    /**
     * Check if this type is assignable from the given Type.
     *
     * @param from the from
     * @return the boolean
     * @deprecated this implementation may be inconsistent with javac for types with wildcards.
     */
    @Deprecated
    public boolean isAssignableFrom(Type from) {
        if (from == null) {
            return false;
        }

        if (type.equals(from)) {
            return true;
        }

        if (type instanceof Class<?>) {
            return rawType.isAssignableFrom(TypeUtil.erase(from));
        } else if (type instanceof ParameterizedType) {
            return isAssignableFrom(from, (ParameterizedType) type,
                    new HashMap<>());
        } else if (type instanceof GenericArrayType) {
            return rawType.isAssignableFrom(TypeUtil.erase(from))
                    && isAssignableFrom(from, (GenericArrayType) type);
        } else {
            throw buildUnexpectedTypeError(
                    type, Class.class, ParameterizedType.class, GenericArrayType.class);
        }
    }

    /**
     * Check if this type is assignable from the given type token.
     *
     * @param token the token
     * @return the boolean
     * @deprecated this implementation may be inconsistent with javac for types with wildcards.
     */
    @Deprecated
    public boolean isAssignableFrom(LiTypeToken<?> token) {
        return isAssignableFrom(token.getType());
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof LiTypeToken<?>
                && TypeUtil.equals(type, ((LiTypeToken<?>) o).type);
    }

    @Override
    public final String toString() {
        return TypeUtil.typeToString(type);
    }
}
