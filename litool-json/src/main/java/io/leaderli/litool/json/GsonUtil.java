package io.leaderli.litool.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.core.util.ConsoleUtil;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class GsonUtil {

public static final Gson GSON = new Gson();
public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();

public static String toJson(Object obj) {

    return GSON.toJson(obj);
}

public static void print(Object src) {
    ConsoleUtil.print(PRETTY_GSON.toJson(src));
}

}
