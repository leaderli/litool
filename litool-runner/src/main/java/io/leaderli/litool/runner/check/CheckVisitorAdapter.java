package io.leaderli.litool.runner.check;

import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.runner.xml.MainElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/13 4:28 PM
 */
public abstract class CheckVisitorAdapter implements CheckVisitor {
    protected MainElement mainElement;
    protected List<String> parseErrorMsgs;

    @Override
    public void setMainElement(MainElement mainElement) {
        this.mainElement = mainElement;
    }

    public void setParseErrorMsgs(List<String> parseErrorMsgs) {
        this.parseErrorMsgs = parseErrorMsgs;
    }

    public final void addErrorMsgs(boolean success, String error) {
        SaxEventHandler.addErrorMsgs(parseErrorMsgs, success, error);
    }

}
