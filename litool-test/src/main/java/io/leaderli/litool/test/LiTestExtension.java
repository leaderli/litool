package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LiTestExtension implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return true;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        return Stream.of(new MyTestTemplateInvocationContext());
    }

    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {
        @Override
        public String getDisplayName(int invocationIndex) {
            return TestTemplateInvocationContext.super.getDisplayName(invocationIndex);
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            List<Extension> list = new ArrayList<>();
            return list;
        }
    }
}
