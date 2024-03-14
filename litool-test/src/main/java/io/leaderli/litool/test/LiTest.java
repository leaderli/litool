package io.leaderli.litool.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({LiMockBeforeEachCallback.class, LiMockAfterEachCallback.class, LiParameterResolver.class})
@Test
public @interface LiTest {
}

