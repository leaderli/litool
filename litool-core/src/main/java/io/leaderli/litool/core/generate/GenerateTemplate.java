package io.leaderli.litool.core.generate;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.lang.RegexUtils;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个用于生产随机字符串的工具
 * 支持注册函数，通过@符号调用
 */
public class GenerateTemplate {

    private static final String VARIABLE_REGEX = "@(\\w+)\\(([^()]*)\\)";
    private final Map<String, GenerateFunction<String, String>> internal = new HashMap<>();

    public GenerateTemplate() {

        this.internal.put("date", new GenerateFunction<String, String>() {
            @Override
            public String apply() {
                return apply("yyyyMMdd");
            }

            @Override
            public String apply(String format) {
                return DateUtil.now(format);
            }
        });
    }


    public void register(String name, GenerateFunction<String, String> generateFunction) {
        this.internal.put(name, generateFunction);
    }


    public String generate(String template) {

        return RegexUtils.replacePatternByFunction(template, VARIABLE_REGEX, variable -> {

            String[] name_args = RegexUtils.matchGroup(variable, VARIABLE_REGEX);

            String name = name_args[0];
            String args = name_args[1];
            GenerateFunction<String, String> func = this.internal.get(name);
            LiAssertUtil.assertNotNull(func, UnsupportedOperationException::new, "not support" + name + "(...)");
            if (StringUtils.isEmpty(args)) {
                return func.apply();
            } else {
                String[] split = args.split(",");
                switch (split.length) {
                    case 1:
                        return func.apply(split[0]);
                    case 2:
                        return func.apply(split[0], split[1]);
                    case 3:
                        return func.apply(split[0], split[1], split[2]);
                    case 4:
                        return func.apply(split[0], split[1], split[2], split[3]);
                    default:
                        throw new IllegalArgumentException("not support func with " + split.length + " arguments");
                }
            }
        });
    }

}
