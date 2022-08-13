package io.leaderli.litool.runner.check;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxEventHandler;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.runner.xml.MainElement;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/8/13 2:36 PM
 */
public class MainCheckVisitor extends ElementCheckVisitor {


    public MainCheckVisitor(MainElement mainElement, List<String> parseErrorMsgs) {
        this.mainElement = mainElement;
        this.parseErrorMsgs = parseErrorMsgs;
    }






}
