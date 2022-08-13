package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.ExpressionList;
import io.leaderli.litool.runner.executor.EchoElementExecutor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoElement extends TaskElement<EchoElement, EchoElementExecutor> {
    private static final String EXPRESSION_PATTERN = "\\{[^}]*\\}";

    private ExpressionList expressionList = new ExpressionList();

    private String value;

    public ExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(ExpressionList expressionList) {
        this.expressionList = expressionList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        String message = bodyEvent.description();
        this.value = solveMessage(message);
    }
    private String solveMessage(String message) {

        Pattern pattern = Pattern.compile(EXPRESSION_PATTERN);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String origin = matcher.group(0);
            String expression = StringUtils.substring(origin, 1, origin.length() - 1);
            expressionList.getExpressionList().add(new Expression(expression));
            message = message.replaceFirst(EXPRESSION_PATTERN,"%s");
        }
        return message;
    }



    @Override
    public String tag() {
        return "echo";
    }



}
