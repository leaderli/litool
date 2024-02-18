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
        return Stream.of(new MyTestTemplateInvocationContext(context));
    }

    private static class MyTestTemplateInvocationContext implements TestTemplateInvocationContext {
        private final ExtensionContext context;

        public MyTestTemplateInvocationContext(ExtensionContext context) {
            this.context = context;
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return invocationIndex + "";
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return new ArrayList<>();
        }
    }
}
