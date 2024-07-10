package io.leaderli.litool.core.type;

import io.leaderli.litool.core.function.Chain;
import io.leaderli.litool.core.function.Filter;
import io.leaderli.litool.core.lang.FilterChain;
import io.leaderli.litool.core.text.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodFilter implements Filter<Method>, Chain<Filter<Method>> {

    private final FilterChain<Method> filter = new FilterChain<>();


    @Override
    public Boolean apply(Method method) {
        return filter.apply(method);
    }

    public static MethodFilter isMethod() {
        return new MethodFilter()._isMethod();
    }

    public static MethodFilter declare(Class<?> declare) {
        return new MethodFilter()._declare(declare);
    }

    public static MethodFilter parameterType(Class<?>... parameterTypes) {
        return new MethodFilter()._parameterType(parameterTypes);
    }

    public static MethodFilter arg0(Class<?> parameterType) {
        return new MethodFilter()._arg0(parameterType);
    }

    public static MethodFilter arg1(Class<?> parameterType) {
        return new MethodFilter()._arg1(parameterType);
    }

    public static MethodFilter arg2(Class<?> parameterType) {
        return new MethodFilter()._arg2(parameterType);
    }

    public static MethodFilter argx(int index, Class<?> parameterType) {
        return new MethodFilter()._argx(index, parameterType);
    }

    public static MethodFilter name(String name) {
        return new MethodFilter()._name(name);
    }

    public static MethodFilter names(String... names) {
        return new MethodFilter()._names(names);
    }

    public static MethodFilter isPublic() {
        return new MethodFilter()._isPublic();
    }

    public static MethodFilter of(Filter<Method> filter) {
        return new MethodFilter().add(filter);
    }

    public static MethodFilter annotated(Class<? extends Annotation> annotated) {
        return new MethodFilter()._annotated(annotated);
    }

    public static MethodFilter returnType(Class<?> returnType) {
        return new MethodFilter()._returnType(returnType);
    }

    public static MethodFilter length(int length) {
        return new MethodFilter()._length(length);
    }

    public static MethodFilter modifiers(int modifiers) {
        return new MethodFilter()._modifiers(modifiers);
    }

    @Override
    public MethodFilter addHead(Filter<Method> filter) {
        this.filter.addHead(filter);
        return this;
    }

    @Override
    public MethodFilter add(Filter<Method> filter) {
        this.filter.add(filter);
        return this;
    }

    public MethodFilter _isMethod() {
        add(Objects::nonNull);
        return this;
    }

    public MethodFilter _declare(Class<?> declare) {
        add(m -> m.getDeclaringClass() == declare);
        return this;
    }

    public MethodFilter _parameterType(Class<?>... parameterTypes) {
        add(m -> Arrays.equals(m.getParameterTypes(), parameterTypes));
        return this;
    }

    public MethodFilter _name(String name) {
        add(m -> m.getName().equals(name));
        return this;
    }

    public MethodFilter _names(String... names) {
        add(m -> StringUtils.equalsAny(m.getName(), names));
        return this;
    }

    public MethodFilter _isPublic() {
        add(ModifierUtil::isPublic);
        return this;
    }

    public MethodFilter _of(Filter<Method> filter) {
        add(filter);
        return this;
    }

    public MethodFilter _annotated(Class<? extends Annotation> annotated) {
        add(m -> m.isAnnotationPresent(annotated));
        return this;
    }

    public MethodFilter _returnType(Class<?> returnType) {
        add(m -> m.getReturnType() == returnType);
        return this;
    }

    public MethodFilter _arg0(Class<?> parameterType) {
        return _argx(0, parameterType);
    }

    public MethodFilter _arg1(Class<?> parameterType) {
        return _argx(1, parameterType);
    }

    public MethodFilter _arg2(Class<?> parameterType) {
        return _argx(2, parameterType);
    }

    public MethodFilter _argx(int index, Class<?> parameterType) {
        add(m -> m.getParameterTypes().length > index && m.getParameterTypes()[index] == parameterType);
        return this;
    }

    public MethodFilter _length(int length) {
        add(m -> m.getParameterTypes().length == length);
        return this;
    }

    public MethodFilter _modifiers(int modifiers) {
        add(m -> (m.getModifiers() & modifiers) == modifiers);
        return this;
    }
}
