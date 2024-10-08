package io.leaderli.litool.test.bean;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@TestTemplate
@ExtendWith(BeanTestTestExtension.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanTest {

    /**
     * 跳过某些类，规则使用正则表达式，当正则可以替换掉类名的某一部分时跳过
     */
    String value() default "";

    /**
     * 是否允许构造器函数指令调用
     */
    boolean allowInit() default true;

    /**
     * 指定扫描包，默认为测试类同包
     */
    String scan() default "";
}
