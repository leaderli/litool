package io.leaderli.litool.core.generate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 一个用于生产随机字符串的工具
 * 支持注册函数，通过@符号调用
 */
public class GenerateTemplate {

    private final Map<String, Function<String, String>> internal = new HashMap<>();


}
