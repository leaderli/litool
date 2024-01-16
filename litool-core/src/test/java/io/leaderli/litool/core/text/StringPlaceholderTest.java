package io.leaderli.litool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringPlaceholderTest {

    public String format(String str, Object... variables) {
        StringBuilderPlaceholderFunction place = new StringBuilderPlaceholderFunction() {


            @Override
            public void variable(StringBuilder variable) {
                if (variables.length == 1) {

                    append(1);
                } else {

                    append(variable);
                }

            }
        };
        new StringPlaceholder.Builder().escap('`').build().parse(str, place);
        return place.toString();


    }

    @Test
    void test() {

        Assertions.assertEquals("`", format("``"));
        Assertions.assertEquals("{", format("`{"));
        Assertions.assertEquals("{1", format("`{{1}", 1));
        Assertions.assertEquals("{1}", format("`{{1}}", 1));
        Assertions.assertEquals("{1}", format("`{{1}`}", 1));
        Assertions.assertEquals("1}", format("{`{1}}", 1));
    }

    @Test
    void test2() {


    }
}
