package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.LiTuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;

class MetaAnnotationTest {


    @Test
    void test() {

        MetaAnnotation<Meta, IntValue> metaAnnotation = new MetaAnnotation<>(Meta.class, IntValue.class);
        LiTuple<IntValue, Annotation> tuple = metaAnnotation.relative(Bean.class).get();
        Assertions.assertEquals(A1.class, tuple._2.annotationType());
        tuple = metaAnnotation.relative(Bean2.class).get();
        Assertions.assertEquals(A1.class, tuple._2.annotationType());

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    private @interface Meta {
        Class<IntValue> value() default IntValue.class;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @Meta
    private @interface A1 {

        int value() default 0;
    }

    private static class IntValue implements MetaFunction<A1, Integer, Integer> {

        @Override
        public Integer apply(A1 a1, Integer integer) {
            return a1.value();
        }
    }

    @A1
    private static class Bean {

    }

    @A1(1)
    private static class Bean2 {

    }
}
