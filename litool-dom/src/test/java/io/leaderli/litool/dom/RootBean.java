package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.NoBodyAndAttributeSaxBean;
import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/15
 */
public class RootBean implements NoBodyAndAttributeSaxBean {


    public SaxList<Bean> beans = new BeanSaxList();

    private NoBean noBean;

    @Override
    public String tag() {
        return "root";
    }


    public NoBean getNoBean() {
        return noBean;
    }

    public void setNoBean(NoBean noBean) {
        this.noBean = noBean;
    }


    public void addBean(Bean bean) {
        this.beans.add(bean);
    }
}
