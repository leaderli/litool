package io.leaderli.litool.core.generate;

import org.junit.jupiter.api.Test;

class GenerateTemplateTest {


    @Test
    void test() {

        GenerateTemplate generateTemplate = new GenerateTemplate();

        System.out.println(generateTemplate.generate("aaa@date()bbb@date( HH:mm:ss )"));

    }

}
