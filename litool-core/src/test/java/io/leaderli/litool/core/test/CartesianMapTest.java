package io.leaderli.litool.core.test;

import com.google.gson.Gson;
import io.leaderli.litool.core.collection.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CartesianMapTest {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void test() {

        Gson gson = new Gson();
        //language=JSON
        String json = "{\"height\": 188, \"gender\": [true, false], \"age\": [1, 2]}";

        Map map = gson.fromJson(json, Map.class);

        CartesianMap cartesianMap = new CartesianMap(HashMap::new, map);
        Assertions.assertEquals("[{gender=true, age=1.0}, {gender=true, age=2.0}, {gender=false, age=1.0}, {gender=false, age=2.0}]", ArrayUtils.toString(cartesianMap.cartesian()));

        json = " {\"gender\": [true, false], \"age\": [1]}";

        map = gson.fromJson(json, Map.class);

        cartesianMap = new CartesianMap(HashMap::new, map);
        Assertions.assertEquals("[{gender=true, age=1.0}, {gender=false, age=1.0}]", ArrayUtils.toString(cartesianMap.cartesian()));

    }

}
