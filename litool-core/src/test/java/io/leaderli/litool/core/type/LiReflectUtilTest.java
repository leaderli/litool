package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class LiReflectUtilTest {

    public static class Bean {

        public String name = "bean";
        private int age = 80;


        @Override
        public String toString() {
            return "Bean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

  public static class LittleBean extends Bean {
        private String name = "little";
        public int age = 8;

        @Override
        public String toString() {
            return "LittleBean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean", LiReflectUtil.getField(LittleBean.class, "name").throwable_map(f -> f.get(littleBean)).get());
        Assertions.assertEquals(8, LiReflectUtil.getField(LittleBean.class, "age").throwable_map(f -> f.get(littleBean)).get());

        Lino<Field> name = LiReflectUtil.getField(LittleBean.class, "name", true);
        Assertions.assertNull(name.throwable_map(f -> f.get(littleBean), null).get());
        Assertions.assertEquals("little", name.throwable_map(f -> {
            f.setAccessible(true);
            return f.get(littleBean);
        }).get());
        Assertions.assertEquals(8, LiReflectUtil.getField(LittleBean.class, "age", true).throwable_map(f -> f.get(littleBean)).get());
    }


    @Test
    void getFieldValue() {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean", LiReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals(8, LiReflectUtil.getFieldValue(littleBean, "age").get());

        Assertions.assertEquals("little", LiReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertEquals(8, LiReflectUtil.getFieldValue(littleBean, "age", true).get());
    }
}
