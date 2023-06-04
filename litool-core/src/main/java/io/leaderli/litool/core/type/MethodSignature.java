package io.leaderli.litool.core.type;

import io.leaderli.litool.core.text.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * the signature of method, include method name, method returnType, method parameterTypes
 *
 * @author leaderli
 * @since 2022/7/26 10:36 AM
 */
public class MethodSignature {

    public static final String LAMBDA_METHOD_PREFIX = "lambda$";
    /**
     * @see Method#getName()
     */
    public final String name;
    /**
     * @see Method#getReturnType()
     */
    public final Type returnType;
    /**
     * @see Method#getParameterTypes()
     */
    public final Type[] parameterTypes;
    /**
     * if {@code modifiers < 0}, it means the modifiers will not be compared at {@link  #equals(Method)}
     *
     * @see Method#getModifiers()
     */
    public final int modifiers;

    public MethodSignature(String name) {
        this(name, void.class);
    }

    public MethodSignature(String name, Type returnType) {
        this(name, returnType, new Class[]{});
    }

    public MethodSignature(String name, Type returnType, Type[] parameterTypes) {
        this(name, returnType, parameterTypes, -1);
    }

    public MethodSignature(String name, Type returnType, Type[] parameterTypes, int modifiers) {
        if (returnType == null) {
            returnType = void.class;
        }
        if (parameterTypes == null) {
            parameterTypes = new Class[]{};
        }
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        for (int i = 0; i < this.parameterTypes.length; i++) {
            Type parameterType = this.parameterTypes[i];
            if (PrimitiveEnum.isPrimitive(parameterType)) {
                this.parameterTypes[i] = PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.get(parameterType);
            }
        }
        this.modifiers = modifiers;
    }

    /**
     * Return strict signature of method, it's will compare method modifiers
     *
     * @param method the method
     * @return strict signature of method
     */
    public static MethodSignature strict(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return strict(method, declaringClass);
    }

    /**
     * Return no-strict signature of method, it mean {@code modifiers=-1}
     *
     * @param method the method
     * @return no-strict signature of method
     */
    public static MethodSignature non_strict(Method method) {

        Class<?> declaringClass = method.getDeclaringClass();
        return non_strict(method, declaringClass);
    }

    /**
     * Return strict signature of method, it's will compare method modifiers
     *
     * @param method the method
     * @return strict signature of method
     */
    public static MethodSignature strict(Method method, Type context) {
        Type returnType = TypeUtil.resolve(context, method.getGenericReturnType());
        Type[] parameterTypes = Stream.of(method.getGenericParameterTypes())
                .map(t -> TypeUtil.resolve(context, t))
                .toArray(Type[]::new);

        return new MethodSignature(method.getName(), returnType, parameterTypes,
                method.getModifiers());
    }

    /**
     * Return no-strict signature of method, it mean {@code modifiers=-1}
     *
     * @param method the method
     * @return no-strict signature of method
     */
    public static MethodSignature non_strict(Method method, Type context) {

        Type returnType = TypeUtil.resolve(context, method.getGenericReturnType());
        Type[] parameterTypes = Stream.of(method.getGenericParameterTypes())
                .map(t -> TypeUtil.resolve(context, t))
                .toArray(Type[]::new);

        return new MethodSignature(method.getName(), returnType, parameterTypes, -1);

    }

    /**
     * Return whether two method have same signature
     *
     * @param method a  method
     * @return whether two method have same signature
     * @see #name
     * @see #returnType
     * @see #parameterTypes
     */
    public boolean equals(Method method) {
        return this.equals(non_strict(method));
    }


    @Override
    public int hashCode() {
        int result = Objects.hash(name, returnType, modifiers);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodSignature that = (MethodSignature) o;
        return Objects.equals(name, that.name)
                && Objects.equals(returnType, that.returnType)
                && (modifiers < 0 || Objects.equals(modifiers, that.modifiers))
                && Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public String toString() {


        return TypeUtil.typeToString(returnType) +
                " " +
                name +
                '(' +
                StringUtils.join0(",", parameterTypes) +
                ')';

    }
}
