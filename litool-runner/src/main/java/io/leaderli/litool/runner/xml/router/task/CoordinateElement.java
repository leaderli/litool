package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.router.task.CoordinateElementExecutor;

import java.util.List;

import static io.leaderli.litool.runner.constant.VariablesModel.*;


public class CoordinateElement extends BaseElement<CoordinateElement, CoordinateElementExecutor> {

private Expression x;

private Expression y;

private TdList tdList = new TdList();

public CoordinateElement() {
    super("coordinate");
}

public void addTd(TdElement tdElement) {
    tdList.add(tdElement);
}

@Override
public void end_check(List<String> parseErrorMsgs) {

    String id = getId();
    id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
    Lira<TdElement> tdElements = tdList.lira();
    Lira<List<String>> lira = tdElements.map(TdElement::getValue);
    int size = lira.map(List::size).distinct().size();
    Lira<String> xLira = lira.first().toLira(String.class);
    int xSize = xLira.skip(1).distinct().size();
    Lira<String> yLira = lira.map(l -> Lira.of(l).first().get());
    int ySize = yLira.skip(1).distinct().size();
    SaxEventHandler.addErrorMsgs(parseErrorMsgs, tdElements.size() > 1, String.format("coordinate td should have two " +
            "or more %s", id));
    SaxEventHandler.addErrorMsgs(parseErrorMsgs, size == 1, String.format("coordinate td should have same size %s",
            id));
    SaxEventHandler.addErrorMsgs(parseErrorMsgs, xSize == xLira.size() - 1, String.format("x-coordinate should not " +
            "repeated %s", id));
    SaxEventHandler.addErrorMsgs(parseErrorMsgs, ySize == yLira.size() - 1, String.format("y-coordinate should not " +
            "repeated %s", id));
}

public Expression getX() {
    return x;
}

public void setX(Expression x) {
    assertExpression(x.getModel());
    this.x = x;
}

private void assertExpression(VariablesModel model) {
    LiAssertUtil.assertTrue(model == REQUEST || model == RESPONSE || model == LITERAL, "coordinate params expression " +
            "must be request or response or literal");
}

public Expression getY() {
    return y;
}

public void setY(Expression y) {
    assertExpression(y.getModel());
    this.y = y;
}

public TdList getTdList() {
    return tdList;
}

public void setTdList(TdList tdList) {
    this.tdList = tdList;
}


}
