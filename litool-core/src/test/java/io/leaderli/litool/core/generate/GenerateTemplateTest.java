package io.leaderli.litool.core.generate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenerateTemplateTest {


    @Test
    void test() {

        GenerateTemplate generateTemplate = new GenerateTemplate();

        Assertions.assertTrue(generateTemplate.generate("aaa@date()bbb@date( HH:mm:ss )").startsWith("aaa20"));

    }

}
