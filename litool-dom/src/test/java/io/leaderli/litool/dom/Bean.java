package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class Bean extends SaxBean {
    public String name;
    public double version = 0;


    @Override
    public TupleMap<String, Class<SaxBean>> support() {
        return SupportTagBuilder.build(TupleMap.of(), Bean.class);
    }
}
