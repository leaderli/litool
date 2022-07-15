package io.leaderli.litool.dom.sax;

import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class BodyEvent extends SaxEvent {

    private String text = "";

    public BodyEvent(Locator locator) {
        super(locator, "");
    }

    public void append(String str) {
        this.text += str.trim();
    }

    @Override
    public String description() {
        return text;
    }
}
