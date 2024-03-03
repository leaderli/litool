package io.leaderli.litool.core.type;

import io.leaderli.litool.core.function.Chain;
import io.leaderli.litool.core.function.Filter;
import io.leaderli.litool.core.lang.FilterChain;

import java.lang.reflect.Method;
import java.util.Objects;

public class MethodFilter implements Filter<Method>, Chain<Filter<Method>> {

    private final FilterChain<Method> filter = new FilterChain<>();


    @Override
    public Boolean apply(Method method) {
        return filter.apply(method);
    }


    //    public static class Builder {
//
//
    public static MethodFilter isMethod() {
        return new MethodFilter().add(Objects::nonNull);
    }

    public static MethodFilter name(String name) {
        return new MethodFilter().add(m -> m.getName().equals(name));
    }

    public static MethodFilter isPublic() {
        return new MethodFilter().add(ModifierUtil::isPublic);
    }

    public static MethodFilter of(Filter<Method> filter) {
        return new MethodFilter().add(filter);
    }

    //    public MethodFilter name(String name) {
//        return new MethodFilter().add(m -> m.getName().equals(name));
//    }
    //
//        public void returnType(Class<?> returnType) {
//            this.returnType = returnType;
//        }
//
//        public void parameterType(Class<?>[] parameterType) {
//            this.parameterType = parameterType;
//        }
//
//        public void parameterlenth(Class<?>[] parameterlenth) {
//            this.parameterlenth = parameterlenth;
//        }
//
//        public void annotated(Class<? extends Annotation> annotated) {
//            this.annotated = annotated;
//        }
//
    public void modifiers(int modifiers) {
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


//        public void isAbstract() {
//            this.modifiers |= Modifier.ABSTRACT;
//        }
//
//        public void isStatic() {
//            this.modifiers |= Modifier.STATIC;
//        }


//    }


}
