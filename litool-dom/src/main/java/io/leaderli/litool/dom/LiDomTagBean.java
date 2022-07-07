package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.meta.Lino;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/8 8:58 PM
 */
public class LiDomTagBean {


    private static final Map<String, Class<? extends SaxBean<?>>> GLOBAL = new HashMap<>();
    private static final Map<Class<?>, Map<String, Class<? extends SaxBean<?>>>> LOCAL = new HashMap<>();

    static {

        GLOBAL.put("bean", Bean.class);
    }

    private static void setTagBeanClass(String tagName, Class<?> parent, Class<? extends SaxBean<?>> tagBeanType) {

        LiAssertUtil.assertTrue(RuntimeExceptionTransfer.get(tagBeanType::getConstructor).isAccessible());


        Lino.of(parent)
                .map(p -> LOCAL.computeIfAbsent(p, pk -> new HashMap<>()))
                .or(GLOBAL)
                .ifPresent(m -> m.put(tagName, tagBeanType));
        if (parent == null) {
            GLOBAL.put(tagName, tagBeanType);
        } else {
            LOCAL.computeIfAbsent(parent, k -> new HashMap<>()).put(tagName, tagBeanType);
        }

    }


    public static Class<? extends SaxBean<?>> getTagBeanClass(Class<?> parent, String tagName) {

        return Lino.of(LOCAL.get(parent)).or(GLOBAL).map(m -> m.get(tagName)).get();

    }


}
