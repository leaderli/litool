package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.LiPrintUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 */
public abstract class SaxBean implements SupportTag {

    public String body = "";

    public List<Object> children = new ArrayList<>();


    public void add(Object t) {

        Lino.of(t).ifPresent(children::add);
    }


    @Override
    public String toString() {

        return String.format("%s{%s}", this.tagName(), LiPrintUtil.getFieldsToString(this));
    }
}
