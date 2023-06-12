package io.leaderli.litool.core.type;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 运行时类型注解，用于在代理方法中使用。当原始方法的参数类型和返回类型无法匹配时，
 * 使用具有此注解的方法将用于代理原始方法。
 *
 * @see ReflectUtil#newInterfaceImpl(LiTypeToken, LiTypeToken, Object)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RuntimeMethod {
}
