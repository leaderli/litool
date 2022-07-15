package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class StartEvent extends SaxEvent {
    public StartEvent(Locator locator, String name) {
        super(locator, name);
    }

}
