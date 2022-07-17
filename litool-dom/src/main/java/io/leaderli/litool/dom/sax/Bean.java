package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.LiTupleMap;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class Bean extends SaxBean {
    public String name;
    public String version = "0";


    @Override
    public LiTupleMap<String, Class<SaxBean>> support() {
        return SupportTagBuilder.build(LiTupleMap.of(), Bean.class);
    }
}
