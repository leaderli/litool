package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class LiTestExtension implements BeforeEachCallback, AfterEachCallback, TestInstancePostProcessor {
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // 在每个测试方法之前执行的逻辑
        System.out.println("Before each test...");
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        // 在每个测试方法之后执行的逻辑
        System.out.println("After each test...");
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        // 在测试实例创建之后执行的逻辑
        System.out.println("Post process test instance...");
    }
}
