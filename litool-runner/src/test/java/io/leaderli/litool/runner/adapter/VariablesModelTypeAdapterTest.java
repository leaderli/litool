package io.leaderli.litool.runner.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VariablesModelTypeAdapterTest {

    @Test
    void deserialize() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(VariablesModel.class, new VariablesModelTypeAdapter()).create();
        Expression expression = gson.fromJson("{\"name\": \"123\", \"model\": 1}", Expression.class);
        LiAssertUtil.assertTrue(StringUtils.equals(expression.getName(), "123"));
        LiAssertUtil.assertTrue(expression.getModel() == VariablesModel.LITERAL);
    }

    @Test
    void serialize() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(VariablesModel.class, new VariablesModelTypeAdapter()).create();

        System.out.println(VariablesModel.REQUEST.getClass());
        System.out.println(gson.toJson(VariablesModel.REQUEST));
        Expression expression = new Expression();
        expression.setName("123");
        expression.setModel(VariablesModel.REQUEST);

        System.out.println(gson.toJson(expression));

    }

}