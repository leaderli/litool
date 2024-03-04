package io.leaderli.litool.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.leaderli.litool.core.internal.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class GsonUtil {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(new SpecailMapValueTypeAdapterFactory())
//            .setPrettyPrinting()
            .create();
    private static final Type SPECIAL_MAP_TYPE = ParameterizedTypeImpl.make(null, Map.class, String.class, SpecialMapValue.class);

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * 返回一个特殊的map，会将json的值中基本类型转换为Strig类型，null会转换为空字符串
     */
    public static Map<String, Object> toSpecialMap(String json) {
        if (json == null) {
            return new HashMap<>();
        }
        return GSON.fromJson(json, SPECIAL_MAP_TYPE);
    }


    private static class SpecialMapValue {
        private SpecialMapValue() {
            throw new IllegalStateException("just use as the class");
        }
    }

    private static class SpecialMapTypeAdapter extends TypeAdapter<Object> {

        @Override
        public void write(JsonWriter out, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:
                    Map<String, Object> map = new LinkedTreeMap<>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:
                case NUMBER:
                case BOOLEAN:
                    return in.nextString();

                case NULL:
                    in.nextNull();
                    return "";

                default:
                    throw new IllegalStateException();
            }
        }

    }

    private static class SpecailMapValueTypeAdapterFactory implements TypeAdapterFactory {


        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (type.getRawType() == SpecialMapValue.class) {
                return ((TypeAdapter<T>) new SpecialMapTypeAdapter());
            }
            return null;
        }
    }
}
