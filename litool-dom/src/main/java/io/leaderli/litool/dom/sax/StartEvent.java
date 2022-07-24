package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class StartEvent extends SaxEvent {

    private SaxBeanAdapter newSaxBean;

    public StartEvent(Locator locator, String name) {
        super(locator, name);
    }

    public SaxBeanAdapter getNewSaxBean() {
        return newSaxBean;
    }

    public void setNewSaxBean(SaxBeanAdapter newSaxBean) {
        this.newSaxBean = newSaxBean;
        this.newSaxBean.setStartEvent(this);
    }
}
