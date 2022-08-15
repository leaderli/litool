package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.RequestElementExecutor;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class RequestElement extends SaxBean implements ElementExecutor<RequestElement, RequestElementExecutor> {


    public final EntryList entryList = new EntryList();

    public RequestElement() {
        super("request");
    }


    public void addEntry(EntryElement element) {

        entryList.lira().iterator().forEachRemaining(entry -> {
            LiAssertUtil.assertFalse(Objects.equals(entry.getKey(), element.getKey()), "duplicate key of " + element.getKey());
            LiAssertUtil.assertFalse(Objects.equals(entry.getLabel(), element.getLabel()), "duplicate label of " + element.getLabel());
        });
        this.entryList.add(element);
    }

    public EntryList getEntryList() {
        return entryList;
    }

    @Override
    public String tag() {
        return "request";
    }
}
