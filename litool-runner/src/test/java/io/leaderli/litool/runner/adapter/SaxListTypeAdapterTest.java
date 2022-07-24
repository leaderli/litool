package io.leaderli.litool.runner.adapter;

import com.google.gson.reflect.TypeToken;
import io.leaderli.litool.dom.sax.SaxBean;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class SaxListTypeAdapterTest {

    @Test
    void test() {

        System.out.println(TypeToken.getParameterized(List.class, SaxBean.class).getType());
        Type type = new TypeToken<List<SaxBean>>() {
        }.getType();
        System.out.println(type);

    }

}
