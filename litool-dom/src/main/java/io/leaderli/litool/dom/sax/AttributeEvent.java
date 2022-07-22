package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class AttributeEvent extends SaxEvent {

    public final String value;

    public AttributeEvent(Locator locator, String name, String value) {
        super(locator, name);
        this.value = value;
    }

    @Override

    public String description() {
        return name + ":" + value;
    }
}
