package io.leaderli.litool.core.type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * the signature of method, include method name, method returnType, method parameterTypes
 *
 * @author leaderli
 * @since 2022/7/26 10:36 AM
 */
public class MethodSignature {

    /**
     * @see Method#getName()
     */
    public final String name;
    /**
     * @see Method#getReturnType()
     */
    public final Class<?> returnType;
    /**
     * @see Method#getParameterTypes()
     */
    public final Class<?>[] parameterTypes;
    /**
     * if modifiers < 0, it means the modifiers will not be compared at {@link  #equals(Method)}
     *
     * @see Method#getModifiers()
     */
    public final int modifiers;

    public MethodSignature(String name) {
        this(name, void.class);
    }

    public MethodSignature(String name, Class<?> returnType) {
        this(name, returnType, new Class[]{});
    }

    public MethodSignature(String name, Class<?> returnType, Class<?>[] parameterTypes) {
        this(name, returnType, new Class[]{}, -1);
    }

    public MethodSignature(String name, Class<?> returnType, Class<?>[] parameterTypes, int modifiers) {
        if (returnType == null) {
            returnType = void.class;
        }
        if (parameterTypes == null) {
            parameterTypes = new Class[]{};
        }
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.modifiers = modifiers;
    }

    /**
     * Return strict signature of method, it's will compare method modifiers
     *
     * @param method the method
     * @return strict signature of method
     */
    public static MethodSignature strict(Method method) {
        return new MethodSignature(method.getName(), method.getReturnType(), method.getParameterTypes(),
                method.getModifiers());
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

    /**
     * Return no-strict signature of method, it mean {@code modifiers=-1}
     *
     * @param method the method
     * @return no-strict signature of method
     */
    public static MethodSignature non_strict(Method method) {
        return new MethodSignature(method.getName(), method.getReturnType(), method.getParameterTypes(),
                -1);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, returnType, modifiers);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodSignature that = (MethodSignature) o;
        return Objects.equals(name, that.name)
                && Objects.equals(returnType, that.returnType)
                && (modifiers < 0 || Objects.equals(modifiers, that.modifiers))
                && Arrays.equals(parameterTypes, that.parameterTypes);
    }
}
