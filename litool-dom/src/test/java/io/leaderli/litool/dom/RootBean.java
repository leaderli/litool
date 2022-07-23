package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxBody;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class RootBean implements SaxBean {


    public SaxList beans = new BeanSaxList();

    public NoBean nobean;
    public SaxBody body;

    @Override
    public String name() {
        return "root";
    }

}
