package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class RequestElement implements SaxBean {


    public EntryList entryList = new EntryList();

    @Override
    public String name() {
        return "request";
    }


}
