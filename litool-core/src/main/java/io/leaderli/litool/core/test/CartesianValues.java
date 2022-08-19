package io.leaderli.litool.core.test;

import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/17 8:14 PM
 */
public class CartesianValues {

    private static final Map<Class<?>, Annotation> TYPE_VALUABLE = new HashMap<>();

    static {

//        System.out.println(IntValues.class.isAnnotationPresent(Valuable.class));
        Map<Method, LiTuple2<? extends Class<?>, ? extends ThrowableSupplier<?>>> value = ClassScanner.getClassOfAnnotated(Valuable.class, Valuable.class)
                .filter(Class::isAnnotation)
                .debug()
                .map(an -> ReflectUtil.getMethod(an, "value", true)
                        .filter(me -> me.getParameterTypes().length == 0)
                        .tuple(me -> LiTuple.of(me.getReturnType().getComponentType(), (ThrowableSupplier<?>) () -> me.invoke(an))).get()
                ).toMap(t -> t);
        System.out.println(value);


//        ClassScanner classScanner = new ClassScanner(Valuable.class, new Filter<Class<?>>() {
//            @Override
//            public boolean accept(Class<?> cls) {
//                return cls.isAnnotationPresent(Valuable.class);
//            }
//        });
    }

    public static void main(String[] args) {

    }
}
