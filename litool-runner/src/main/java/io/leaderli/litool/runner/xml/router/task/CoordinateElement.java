package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.executor.CoordinateElementExecutor;

import java.util.List;


public class CoordinateElement extends TaskElement<CoordinateElement, CoordinateElementExecutor>{

    private Expression x;

    private Expression y;

    private TdList tdList = new TdList();

    public void addTd(TdElement tdElement) {
        tdList.add(tdElement);
    }

    @Override
    public void end_check(List<String> parseErrorMsgs) {

        String id = id();
        id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
        Lira<TdElement> tdElements = tdList.lira();
        Lira<List<String>> lira = tdElements.map(TdElement::getValue);
        int size = lira.map(List::size).distinct().size();
        Lira<String> xLira = lira.first().toLira(String.class);
        int xSize = xLira.skip(1).distinct().size();
        Lira<String> yLira = lira.map(l -> Lira.of(l).first().get());
        int ySize = yLira.skip(1).distinct().size();
        SaxEventHandler.addErrorMsgs(parseErrorMsgs,tdElements.size() > 1,String.format("coordinate td should have two or more %s",id));
        SaxEventHandler.addErrorMsgs(parseErrorMsgs,size == 1,String.format("coordinate td should have same size %s",id));
        SaxEventHandler.addErrorMsgs(parseErrorMsgs,xSize == xLira.size() - 1,String.format("x-coordinate should not repeated %s",id));
        SaxEventHandler.addErrorMsgs(parseErrorMsgs,ySize == yLira.size() - 1,String.format("y-coordinate should not repeated %s",id));
    }

    public Expression getX() {
        return x;
    }

    public void setX(Expression x) {
        this.x = x;
    }

    public Expression getY() {
        return y;
    }

    public void setY(Expression y) {
        this.y = y;
    }

    public TdList getTdList() {
        return tdList;
    }

    public void setTdList(TdList tdList) {
        this.tdList = tdList;
    }

    @Override
    public CoordinateElementExecutor executor() {
        return new CoordinateElementExecutor(this);
    }


    @Override
    public String name() {
        return "coordinate";
    }
}
