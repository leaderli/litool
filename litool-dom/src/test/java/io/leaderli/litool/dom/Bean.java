package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/8 9:55 PM
 */
public class Bean extends SaxBean {
public double version = 0;
public SaxList<Bean> saxList = new BeanSaxList();
private String name;

public Bean() {
    super("bean");
}


public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}
}
