package io.leaderli.litool.runner.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.leaderli.litool.json.JsonTypeAdapter;
import io.leaderli.litool.runner.xml.funcs.FuncList;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class FuncsElementTypeAdapter implements JsonTypeAdapter<FuncsElement> {
    @Override
    public FuncsElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        FuncsElement funcsElement = new FuncsElement();
        FuncList deserialize = context.deserialize(json, FuncList.class);
        deserialize.lira().forEach(funcsElement::addFunc);
        return funcsElement;
    }

    @Override
    public JsonElement serialize(FuncsElement src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.funcList);
    }

}
