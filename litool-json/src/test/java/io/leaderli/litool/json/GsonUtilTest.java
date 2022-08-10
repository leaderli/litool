package io.leaderli.litool.json;

import com.google.gson.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class GsonUtilTest {

    enum EnumTest {
        AA(1);

        EnumTest(int value) {
            this.value = value;
        }

        static EnumTest get(int i) {
            for (EnumTest enumTest : values()) {
                if (enumTest.value == i) {
                    return enumTest;
                }
            }
            return AA;
        }

        final int value;
    }

    class EnumTestTypeAdapter implements JsonTypeAdapter<EnumTest> {

        @Override
        public EnumTest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return EnumTest.get(json.getAsInt());
        }

        @Override
        public JsonElement serialize(EnumTest src, Type typeOfSrc, JsonSerializationContext context) {

            return context.serialize(src.value);
        }
    }

    @Test
    void toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EnumTest.class, new EnumTestTypeAdapter());
        Gson gson = gsonBuilder.create();
        System.out.println(gson.toJson(EnumTest.AA));
        System.out.println(gson.fromJson("1", EnumTest.class));
    }

    @Test
    void fromJson() {

    }

}
