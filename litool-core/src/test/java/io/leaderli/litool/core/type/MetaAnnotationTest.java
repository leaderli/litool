package io.leaderli.litool.core.type;

import io.leaderli.litool.core.lang.lean.LeanFieldFunction;
import io.leaderli.litool.core.lang.lean.LeanFieldMetaAnnotation;
import io.leaderli.litool.core.lang.lean.LeanFieldName;
import io.leaderli.litool.core.meta.LiTuple2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/9/26 1:05 PM
 */
class MetaAnnotationTest {
    @LeanFieldName("abc")
    public String name;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void test() throws NoSuchFieldException, InstantiationException, IllegalAccessException {


        Field field = getClass().getField("name");

        MetaAnnotation<LeanFieldMetaAnnotation, LeanFieldFunction> meta = new MetaAnnotation<>(LeanFieldMetaAnnotation.class, LeanFieldFunction.class);

        LiTuple2<LeanFieldFunction, Annotation> mx = meta.relative(field).get();
        Assertions.assertEquals("abc", mx.apply((a, b) -> a.apply(b, field)));
    }
}
