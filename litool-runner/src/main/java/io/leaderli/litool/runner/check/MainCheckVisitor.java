package io.leaderli.litool.runner.check;

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
