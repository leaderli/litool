package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SkipWhenJacocoExecutionCondition implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (LiMock.RUN_IN_JACOCO) {
            return ConditionEvaluationResult.disabled("skip " + context.getRequiredTestMethod().getName() + " when use jacoco");
        }
        return ConditionEvaluationResult.enabled("");
    }
}
