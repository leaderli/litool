package io.leaderli.litool.core.type;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 在被 {@link  RuntimeMethod} 注解的方法的第一个参数上可以使用该注解，该注解仅作用与为Method类型的第一个参数，实际传递时将会把源方法传递进来
 *
 * @see ReflectUtil#newInterfaceImpl(LiTypeToken, LiTypeToken, Object)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RuntimeParameter {
}
