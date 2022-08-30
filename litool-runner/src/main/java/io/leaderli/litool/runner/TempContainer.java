package io.leaderli.litool.runner;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/11
 */
public class TempContainer {


    private final Map<TempNameEnum, Object> tempMap = new EnumMap<>(TempNameEnum.class);

    public TempContainer() {

        for (TempNameEnum temp : TempNameEnum.values()) {
            tempMap.put(temp, temp.def);
        }
    }

    public void put(String key, Object value) {
        tempMap.put(TempNameEnum.valueOf(key), value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) tempMap.get(TempNameEnum.valueOf(key));
    }

}
