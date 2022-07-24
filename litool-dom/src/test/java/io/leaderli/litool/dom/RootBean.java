package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.NoBodyAndAttributeSaxBean;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class RootBean implements NoBodyAndAttributeSaxBean {


    public SaxList<Bean> beans = new BeanSaxList();

    public NoBean nobean;

    @Override
    public String name() {
        return "root";
    }


}
