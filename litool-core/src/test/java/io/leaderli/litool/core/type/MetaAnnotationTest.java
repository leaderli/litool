package io.leaderli.litool.core.type;

import io.leaderli.litool.core.lang.lean.LeanFieldName;
import org.junit.jupiter.api.Test;

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

//        MetaAnnotation<LeanFieldMetaAnnotation, LeanFieldFunction> meta = new

//        LiTuple2<LeanFieldFunction, Annotation> mx = meta.relative(field).get();
//        System.out.println(mx);
//        System.out.println((String) mx.apply((a, b) -> a.apply(b, field)));
    }
}
