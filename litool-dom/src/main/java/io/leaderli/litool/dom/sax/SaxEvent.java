package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class SaxEvent {

    final public Locator locator;
    final public String name;

    public SaxEvent(Locator locator, String name) {

        //保持一个当前 locator 的快照
        this.locator = new LocatorImpl(locator);
        this.name = name;
    }


    protected String description() {
        return name;
    }

    @Override
    public String toString() {

        return String.format("%s(%s)%d,%d", this.getClass().getSimpleName(), this.description(), locator.getLineNumber(), locator.getColumnNumber());
    }
}
