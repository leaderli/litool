package io.leaderli.litool.core.type;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class ReflectUtilTest {

    static {
        LiConstant.WHEN_THROW = null;

    }

    @Test
    void getField() {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean", ReflectUtil.getField(LittleBean.class, "name").throwable_map(f -> f.get(littleBean)).get());
        Assertions.assertEquals(8, ReflectUtil.getField(LittleBean.class, "age").throwable_map(f -> f.get(littleBean)).get());

        Lino<Field> name = ReflectUtil.getField(LittleBean.class, "name", true);
        Assertions.assertNull(name.throwable_map(f -> f.get(littleBean), null).get());
        Assertions.assertEquals("little", name.throwable_map(f -> {
            f.setAccessible(true);
            return f.get(littleBean);
        }).get());
        Assertions.assertEquals(8, ReflectUtil.getField(LittleBean.class, "age", true).throwable_map(f -> f.get(littleBean)).get());
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @Test
    void getFieldValue() throws NoSuchFieldException {

        LittleBean littleBean = new LittleBean();
        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age").get());

        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertEquals(8, ReflectUtil.getFieldValue(littleBean, "age", true).get());


        Assertions.assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name"));
        Assertions.assertEquals(Lino.none(), ReflectUtil.getFieldValue(null, "name", true));


        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, LittleBean.class.getField("name")).get());
        Assertions.assertNull(ReflectUtil.getFieldValue(littleBean, Lino.Some.class.getDeclaredField("value")).get());


        Assertions.assertEquals(1, ReflectUtil.getFieldValue(null, Static.class.getField("age")).get());
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @Test
    void setFieldValue() throws NoSuchFieldException {
        LittleBean littleBean = new LittleBean();

        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null));
        Assertions.assertFalse(ReflectUtil.setFieldValue(null, "name", null, true));

        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello"));
        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());

        littleBean = new LittleBean();

        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("little", ReflectUtil.getFieldValue(littleBean, "name", true).get());
        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, "name", "hello", true));
        Assertions.assertEquals("bean", ReflectUtil.getFieldValue(littleBean, "name").get());
        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        Assertions.assertFalse(ReflectUtil.setFieldValue(littleBean, "name", 123, true));

        Assertions.assertEquals("hello", ReflectUtil.getFieldValue(littleBean, "name", true).get());


        Assertions.assertTrue(ReflectUtil.setFieldValue(littleBean, LittleBean.class.getField("name"), "hello"));

        Assertions.assertFalse(ReflectUtil.setFieldValue(littleBean, Lino.Some.class.getDeclaredField("value"), "hello"));


    }

    public static class Static {
        public static int age = 1;
    }

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
        public int age = 8;
        private String name = "little";

        @Override
        public String toString() {
            return "LittleBean{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
