package io.leaderli.litool.core.type;

import io.leaderli.litool.core.text.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 该类表示方法签名，包括方法名、返回类型和参数类型。
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
     * 修饰符，如果修饰符<0，则在equals(Method)中不进行比较。
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
     * 返回严格的方法签名，将比较方法的修饰符。
     *
     * @param method 方法。
     * @return 严格的方法签名。
     */
    public static MethodSignature strict(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return strict(method, declaringClass);
    }

    /**
     * 返回严格的方法签名，将比较方法的修饰符。
     *
     * @param method  方法。
     * @param context 类型上下文。 用于将返回的参数和返回类型中的泛型替换为实际类型
     * @return 严格的方法签名。
     * @see TypeUtil#resolve(Type, Type)
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
     * 确定两个方法是否具有相同的签名。
     *
     * @param method 方法。
     * @return 两个方法是否具有相同的签名。
     */
    public boolean equals(Method method) {
        return this.equals(non_strict(method));
    }

    /**
     * 返回非严格的方法签名，这意味着modifiers=-1。
     *
     * @param method 方法。
     * @return 非严格的方法签名。
     */

    public static MethodSignature non_strict(Method method) {

        Class<?> declaringClass = method.getDeclaringClass();
        return non_strict(method, declaringClass);
    }

    /**
     * 返回非严格的方法签名，这意味着modifiers=-1。
     *
     * @param method  方法。
     * @param context 类型上下文。 用于将返回的参数和返回类型中的泛型替换为实际类型
     * @return 非严格的方法签名。
     * @see TypeUtil#resolve(Type, Type)
     */
    public static MethodSignature non_strict(Method method, Type context) {

        Type returnType = TypeUtil.resolve(context, method.getGenericReturnType());
        Type[] parameterTypes = Stream.of(method.getGenericParameterTypes())
                .map(t -> TypeUtil.resolve(context, t))
                .toArray(Type[]::new);

        return new MethodSignature(method.getName(), returnType, parameterTypes, -1);

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
