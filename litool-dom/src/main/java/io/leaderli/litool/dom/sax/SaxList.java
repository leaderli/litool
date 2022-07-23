package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.meta.Lino;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于多个不同标签名的集合类标签
 */
public abstract class SaxList implements SupportTag {


    public final List<SaxBean> children = new ArrayList<>();


    public void add(SaxBean t) {

        Lino.of(t).ifPresent(children::add);
    }

}
