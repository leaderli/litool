package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class Bean implements SaxBean {
    public String name;
    public double version = 0;

    public SaxList<Bean> saxList = new BeanSaxList();


}
