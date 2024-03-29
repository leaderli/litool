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
        return new MethodFilter().add(Objects::nonNull);
    }

    public static MethodFilter declare(Class<?> declare) {
        return new MethodFilter().add(m -> m.getDeclaringClass() == declare);
    }

    public static MethodFilter parameterType(Class<?>... parameterTypes) {
        return new MethodFilter().add(m -> Arrays.equals(m.getParameterTypes(), parameterTypes));
    }

    public static MethodFilter name(String name) {
        return new MethodFilter().add(m -> m.getName().equals(name));
    }

    public static MethodFilter names(String... names) {
        return new MethodFilter().add(m -> StringUtils.equalsAny(m.getName(), names));
    }

    public static MethodFilter isPublic() {
        return new MethodFilter().add(ModifierUtil::isPublic);
    }

    public static MethodFilter of(Filter<Method> filter) {
        return new MethodFilter().add(filter);
    }

    public static MethodFilter annotated(Class<? extends Annotation> annotated) {
        return new MethodFilter().add(m -> m.isAnnotationPresent(annotated));
    }

    public static MethodFilter returnType(Class<?> returnType) {
        return new MethodFilter().add(m -> m.getReturnType() == returnType);
    }

    public static MethodFilter length(int length) {
        return new MethodFilter().add(m -> m.getParameterTypes().length == length);
    }

    public static MethodFilter modifiers(int modifiers) {
        return new MethodFilter().add(m -> (m.getModifiers() & modifiers) == modifiers);
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

    public Builder builder() {
        return new Builder(this);
    }

    public static class Builder {
        public final MethodFilter methodFilter;

        public Builder(MethodFilter methodFilter) {
            this.methodFilter = methodFilter;
        }

        public Builder isMethod() {
            methodFilter.add(Objects::nonNull);
            return this;
        }

        public Builder name(String name) {
            methodFilter.add(m -> m.getName().equals(name));
            return this;
        }

        public Builder isPublic() {
            methodFilter.add(ModifierUtil::isPublic);
            return this;
        }

        public MethodFilter of(Filter<Method> filter) {
            return methodFilter.add(filter);
        }

        public Builder annotated(Class<? extends Annotation> annotated) {
            methodFilter.add(m -> m.isAnnotationPresent(annotated));
            return this;
        }

        public Builder returnType(Class<?> returnType) {
            methodFilter.add(m -> m.getReturnType() == returnType);
            return this;
        }

        public Builder length(int length) {
            methodFilter.add(m -> m.getParameterTypes().length == length);
            return this;
        }

        public Builder declareBy(Class<?> declare) {
            methodFilter.add(m -> m.getDeclaringClass() == declare);
            return this;
        }

        public Builder names(String... names) {
            methodFilter.add(m -> StringUtils.equalsAny(m.getName(), names));
            return this;
        }


        public Builder parameterType(Class<?>[] parameterTypes) {
            methodFilter.add(m -> Arrays.equals(m.getParameterTypes(), parameterTypes));
            return this;
        }

        public Builder modifiers(int modifiers) {
            methodFilter.add(m -> (m.getModifiers() & modifiers) == modifiers);
            return this;
        }
    }

}
