package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public abstract class SaxEvent {

public final Locator locator;
public final String name;

protected SaxEvent(Locator locator, String name) {

    //保持一个当前 locator 的快照
    this.locator = new LocatorImpl(locator);
    this.name = name;
}

@Override
public String toString() {

    return String.format("%s(%s)%d,%d", this.getClass().getSimpleName(), this.description(), locator.getLineNumber(),
            locator.getColumnNumber());
}

protected String description() {
    return name;
}


}
