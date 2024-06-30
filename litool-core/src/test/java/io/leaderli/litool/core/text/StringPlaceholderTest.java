package io.leaderli.litool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringPlaceholderTest {

    public String format(String str, Object... variables) {
        StringBuilderPlaceholderFunction place = new StringBuilderPlaceholderFunction() {


            @Override
            public void variable(StringBuilder variable) {
                append(variables[0]);

            }
        };
        new StringPlaceholder.Builder().escap('`').build().parse(str, place);
        return place.toString();


    }

    public String format2(String str, Object... variables) {
        StringBuilderPlaceholderFunction place = new StringBuilderPlaceholderFunction() {


            @Override
            public void variable(StringBuilder variable) {


                    append(variable);

            }
        };
        new StringPlaceholder.Builder().escap('`').variable_prefix("##").variable_suffix("##").build().parse(str, place);
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


        Assertions.assertEquals("#`", format2("#``"));
        Assertions.assertEquals("##", format2("#`#"));
        Assertions.assertEquals("aa#b", format2("##aa#b##"));
    }

    @Test
    void test2() {


    }
}
