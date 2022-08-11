package io.leaderli.litool.runner.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.leaderli.litool.json.JsonTypeAdapter;
import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.instruct.Instruct;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/8/12
 */
public class InstructTypeAdapter implements JsonTypeAdapter<Instruct> {
    @Override
    public Instruct deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String name = json.getAsString();
        return InstructContainer.getInnerMethodByAlias(name);
    }

    @Override
    public JsonElement serialize(Instruct src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.name());
    }
}
