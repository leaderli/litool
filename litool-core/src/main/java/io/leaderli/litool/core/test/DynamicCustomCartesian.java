package io.leaderli.litool.core.test;

import io.leaderli.litool.core.meta.ra.LiraRuntimeException;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.ReflectUtil;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * @author leaderli
 * @since 2022/8/21
 */
public class DynamicCustomCartesian implements CustomValuable<DynamicValues> {
    @Override
    public Object[] cartesian(Class<?> type, DynamicValues annotation, AnnotatedElement annotatedElement, CartesianContext context) {

        Class<?> declaringClass = null;
        if (annotatedElement instanceof Parameter) {
            declaringClass = ((Parameter) annotatedElement).getDeclaringExecutable().getDeclaringClass();
        } else if (annotatedElement instanceof Field) {
            declaringClass = ((Field) annotatedElement).getDeclaringClass();
        }
        return ReflectUtil.getMethod(declaringClass, annotation.value())
                .assertTrue(ModifierUtil::isStatic, new LiraRuntimeException("only support static method"))
                .unzip(m -> ReflectUtil.invokeMethod(m, null))
                .cast(Object[].class)
                .get();
    }
}
