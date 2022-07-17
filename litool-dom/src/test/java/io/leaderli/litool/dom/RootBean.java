package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.LiTupleMap;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class RootBean extends SaxBean {


    @Override
    public LiTupleMap<String, Class<SaxBean>> support() {
        LiTupleMap<String, Class<SaxBean>> pairs = LiTupleMap.of();

        return SupportTagBuilder.of(pairs)
                .add(Bean.class)
                .add(NoBean.class)
                .build();

    }

    @Override
    public String tagName() {
        return "root";
    }
}
