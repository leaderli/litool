package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class RequestElement implements SaxBean {


    public final EntryList entryList = new EntryList();


    public void addEntry(EntryElement element) {

        LiAssertUtil.assertFalse(entryList.lira().contains(element), "duplicate key of " + element.getKey());
        this.entryList.add(element);
    }

    @Override
    public String name() {
        return "request";
    }


}
