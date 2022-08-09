package io.leaderli.litool.runner.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import io.leaderli.litool.json.JsonTypeAdapter;
import io.leaderli.litool.runner.xml.router.IfTaskElement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TaskTypeAdapter implements JsonTypeAdapter<IfTaskElement> {

    private static Map<String, Class<? extends IfTaskElement>> TASK_TYPE = new HashMap<>();

    public void addTaskType(String type, Class<? extends IfTaskElement> clazz) {

    }

    @Override
    public IfTaskElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(IfTaskElement src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}
