package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class EndEvent extends SaxEvent {

    private SaxBeanAdapter saxBeanAdapter;
    private SaxBean father;

    public EndEvent(Locator locator, String name) {
        super(locator, name);
    }

    public SaxBean getFather() {
        return father;
    }

    public void setFather(SaxBean father) {
        this.father = father;
    }

    public SaxBeanAdapter getSaxBeanWrapper() {
        return saxBeanAdapter;
    }

    public void setSaxBeanWrapper(SaxBeanAdapter saxBeanAdapter) {
        this.saxBeanAdapter = saxBeanAdapter;
    }


}
