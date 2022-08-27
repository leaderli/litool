package io.leaderli.litool.json;

import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class JsonTypeAdapterTest {


HashMap<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

@Test
void registerTypeAdapter() {


    GsonBuilder gsonBuilder = new GsonBuilder();

    gsonBuilder.registerTypeHierarchyAdapter(Father.class, new TestJsonTypeAdapter());
    Gson gson = gsonBuilder.create();

    Son son = new Son();
    son.setName("h1");

    Assertions.assertEquals("\"h1\"", gson.toJson(son));
    son = gson.fromJson("2", Son.class);
    Assertions.assertEquals("2", son.getName());
    Assertions.assertEquals("1", gson.fromJson("1", Father.class).getName());

}


static class Son extends Father {

}

static class Father {

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

private class TestJsonTypeAdapter implements JsonTypeAdapter<Father> {


    @Override
    public Father deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
        TypeToken<?> typeToken = TypeToken.get(typeOfT);
        ObjectConstructor<?> objectConstructor = constructorConstructor.get(typeToken);

        instanceCreators.put(typeOfT, (InstanceCreator<Father>) type -> (Father) objectConstructor.construct());
        Father father = (Father) objectConstructor.construct();
        father.name = context.deserialize(json, String.class);
        return father;

    }

    @Override
    public JsonElement serialize(Father src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.name);
    }


}

}

