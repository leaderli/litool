package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.xml.router.SequenceElement;
import io.leaderli.litool.runner.xml.router.task.GotoDestination;

public class GotoElementCheckVisitor extends CheckVisitor {


//    @Override
    public void check(GotoDestination gotoDestination, SaxBean saxBean) {
        Lino<SequenceElement> first = mainElement.getRouter().getSequenceList().lira()
                .first(sequenceElement -> StringUtils.equals(sequenceElement.getName(), gotoDestination.next));
        addErrorMsgs(first.present(), String.format("goto next [%s] is not exist", gotoDestination.next));
    }
}
