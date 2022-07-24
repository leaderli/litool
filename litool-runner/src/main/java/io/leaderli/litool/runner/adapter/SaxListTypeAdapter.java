package io.leaderli.litool.runner.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.json.JsonTypeAdapter;
import io.leaderli.litool.runner.xml.MainElement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/7/24
 * <p>
 * 重新定义 {@link SaxList} 的序列化、反序列化
 */
class SaxListTypeAdapter implements JsonTypeAdapter<SaxList<?>> {

    public static final Map<Type, Supplier<SaxList<?>>> instanceCreators = new HashMap<>();

    static {
        init();
    }

    @SuppressWarnings("rawtypes")
    private static void init() {
        // 所有 SaxList 的子类，重新定义序列化反序列化的逻辑
        ConstructorConstructor constructorConstructor = new ConstructorConstructor(new HashMap<>());
        ClassScanner.getSubTypesOf(MainElement.class, SaxList.class).forEach(cls -> {
            TypeToken<SaxList> typeToken = TypeToken.get(cls);
            ObjectConstructor<SaxList> constructor = constructorConstructor.get(typeToken);
            instanceCreators.put(typeToken.getType(), constructor::construct);
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public SaxList<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SaxList saxList = instanceCreators.get(typeOfT).get();
        Type type = TypeToken.getParameterized(List.class, saxList.componentType()).getType();
        List<SaxBean> deserialize = context.deserialize(json, type);
        deserialize.forEach(saxList::add);
        return saxList;

    }

    @Override
    public JsonElement serialize(SaxList<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.copy());
    }

}
