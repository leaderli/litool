package io.leaderli.litool.test.cartesian;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

/**
 * @author leaderli
 * @since 2023/7/21 10:26 AM
 */
public class LiTestInstanceFactory implements TestInstanceFactory {
    @Override
    public Object createTestInstance(TestInstanceFactoryContext factoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
        try {
            return factoryContext.getTestClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new TestInstantiationException(e.getMessage());
        }
    }
}
