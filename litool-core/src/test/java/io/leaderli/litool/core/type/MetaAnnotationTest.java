package io.leaderli.litool.core.type;

import io.leaderli.litool.core.lang.lean.LeanField;
import io.leaderli.litool.core.lang.lean.LeanMeta;
import io.leaderli.litool.core.lang.lean.LeanName;
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
    @LeanName("abc")
    public String name;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void test() throws NoSuchFieldException, InstantiationException, IllegalAccessException {


        Field field = getClass().getField("name");

        MetaAnnotation<LeanMeta, LeanField> meta = new MetaAnnotation<>(LeanMeta.class, LeanField.class);

        LiTuple2<LeanField, Annotation> mx = meta.relative(field).get();
        Assertions.assertEquals("abc", mx.apply((a, b) -> a.apply(b, field)));
    }
}
