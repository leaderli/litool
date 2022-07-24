package io.leaderli.litool.json;

import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.leaderli.litool.core.util.ConsoleUtil;
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

//        Gson gson1 = new Gson();
//        Father father = gson1.fromJson("{name:\"1\"}", Son.class);
//        System.out.println(father);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeHierarchyAdapter(Father.class, new TestJsonTypeAdapter());
//        new TestJsonTypeAdapter().registerTypeAdapter(gsonBuilder);
        Gson gson = gsonBuilder.create();


//        Father src = new Father();
//        src.name = "f";
//        System.out.println(gson.toJson(src));
//
//        Father aa = gson.fromJson("aa", Father.class);
//        System.out.println(aa.name);
//

        Son son = new Son();

//        System.out.println(gson.toJson(son));

        System.out.println(gson.fromJson("2", Son.class));
        System.out.println(gson.fromJson("2", Son.class));
        System.out.println(gson.fromJson("1", Father.class));

    }

    private class FatherTypeAdapter extends TypeAdapter<Father> {


        @Override
        public void write(JsonWriter out, Father value) {

            ConsoleUtil.print("write", value, value.getClass());

//                        adapter.write(out, value);

        }

        @Override
        public Father read(JsonReader in) {
            ConsoleUtil.print("read", in);
            return null;
        }
    }

    private class TestJsonTypeAdapter implements JsonTypeAdapter<Father> {


        @Override
        public Father deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators);
            TypeToken<?> typeToken = TypeToken.get(typeOfT);
            ObjectConstructor<?> objectConstructor = constructorConstructor.get(typeToken);
            System.out.println("----------1");

            instanceCreators.put(typeOfT, (InstanceCreator<Father>) type -> {
                System.out.println("----------");
                return (Father) objectConstructor.construct();
            });
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

class Son extends Father {

}

class Father {

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
