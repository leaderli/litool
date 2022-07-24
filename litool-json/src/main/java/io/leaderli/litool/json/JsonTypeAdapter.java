package io.leaderli.litool.json;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public interface JsonTypeAdapter<T> extends JsonDeserializer<T>, JsonSerializer<T> {


}
