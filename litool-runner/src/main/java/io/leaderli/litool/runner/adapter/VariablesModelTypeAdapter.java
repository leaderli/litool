package io.leaderli.litool.runner.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.leaderli.litool.json.JsonTypeAdapter;
import io.leaderli.litool.runner.constant.VariablesModel;

import java.lang.reflect.Type;

public class VariablesModelTypeAdapter implements JsonTypeAdapter<VariablesModel> {
    @Override
    public VariablesModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return VariablesModel.getVariableModel(json.getAsInt());
    }

    @Override
    public JsonElement serialize(VariablesModel src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.getModelType());
    }
}
