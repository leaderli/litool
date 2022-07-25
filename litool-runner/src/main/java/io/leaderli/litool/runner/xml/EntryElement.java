package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
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
    public void body(BodyEvent bodyEvent) {



        this.key = new SaxBody(bodyEvent.description());
    }

    @Override
    public String name() {
        return "entry";
    }
}
