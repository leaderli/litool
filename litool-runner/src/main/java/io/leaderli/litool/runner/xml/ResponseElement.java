package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.ContextInfo;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.ResponseElementExecutor;

import java.util.Objects;

/**
 * @author leaderli
 * @since 2022/7/23
 */
public class ResponseElement extends SaxBean implements ElementExecutor<ResponseElement, ResponseElementExecutor> {

    public final EntryList entryList = new EntryList();

    public ResponseElement() {
        super("response");
    }

    public void addEntry(EntryElement element) {
        LiAssertUtil.assertFalse(Objects.equals(ContextInfo.INFO, element.getKey()), "should not use inner key " + ContextInfo.INFO);
        entryList.lira().iterator().forEachRemaining(entry -> {
            LiAssertUtil.assertFalse(Objects.equals(entry.getKey(), element.getKey()), "duplicate key of " + element.getKey());
            LiAssertUtil.assertFalse(Objects.equals(entry.getLabel(), element.getLabel()), "duplicate label of " + element.getLabel());
        });
        this.entryList.add(element);
    }

    public EntryList getEntryList() {
        return entryList;
    }


}
