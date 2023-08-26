package io.leaderli.litool.core.generate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GenerateTemplateTest {


    public static void main(String[] args) {
        String input = "Hello #na2me, welc1ome!";
        String output = replaceWithFunction(input, "\\d");
        System.out.println(output);
    }

    public static String replaceWithFunction(String input, String regex) {
        Pattern pattern = Pattern.compile("(" + regex + ")");
        Matcher matcher = pattern.matcher(input);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String match = matcher.group(1); // 获取匹配的字符串
            String replacement = customFunction(match); // 调用其他函数进行处理
            matcher.appendReplacement(result, replacement);
            System.out.println(result);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static String customFunction(String input) {
        // 在这里调用其他函数对输入进行处理
        return "_" + input.toUpperCase() + "_";
    }
}
