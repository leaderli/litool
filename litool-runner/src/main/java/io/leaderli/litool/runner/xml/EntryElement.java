package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxBody;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryElement implements SaxBean {

    public String label;
    public SaxBody key;
    public String def = "";
    public String type = "str";

    @Override
    public String name() {
        return "entry";
    }
}
