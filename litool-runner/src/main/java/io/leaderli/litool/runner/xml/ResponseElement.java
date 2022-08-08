package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class ResponseElement implements SaxBean {


    public EntryList entryList = new EntryList();

    public void addEntry(EntryElement element) {
        this.entryList.add(element);
    }

    @Override
    public String name() {
        return "response";
    }


}