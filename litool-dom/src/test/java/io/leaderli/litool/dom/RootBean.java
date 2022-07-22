package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class RootBean extends SaxBean {


    @Override
    public TupleMap<String, Class<SaxBean>> support() {

        return SupportTagBuilder.of(super.support())
                .add(Bean.class)
                .add(NoBean.class)
                .build();

    }

    @Override
    public String tagName() {
        return "root";
    }
}
