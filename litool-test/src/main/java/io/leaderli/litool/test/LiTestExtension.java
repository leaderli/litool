package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.*;

import java.util.Collections;
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

        public MyTestTemplateInvocationContext(ExtensionContext context) {
        }

        @Override
        public String getDisplayName(int invocationIndex) {
            return invocationIndex + "";
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return Collections.singletonList((AfterEachCallback) context -> LiMock.reset());
        }
    }
}
