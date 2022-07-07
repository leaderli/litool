package io.leaderli.litool.dom;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/8 10:17 PM
 */
class LiDomTagBeanTest {


    @Test
    public void test() {
        Class<?> bean = LiDomTagBean.getTagBeanClass(null, "bean");

        System.out.println(bean);
    }
}
