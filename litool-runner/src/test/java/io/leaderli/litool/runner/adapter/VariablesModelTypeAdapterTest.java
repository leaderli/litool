package io.leaderli.litool.runner.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VariablesModelTypeAdapterTest {

    @Test
    void deserialize() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(VariablesModel.class, new VariablesModelTypeAdapter()).create();
        Expression expression = gson.fromJson("{\"name\": \"123\", \"model\": 1}", Expression.class);
        Assertions.assertEquals("123", expression.getName());
        Assertions.assertSame(expression.getModel(), VariablesModel.LITERAL);
    }

    @Test
    void serialize() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(VariablesModel.class, new VariablesModelTypeAdapter()).create();

        Expression expression = new Expression();
        expression.setName("123");
        expression.setModel(VariablesModel.REQUEST);

        Assertions.assertEquals("{\"name\":\"123\",\"model\":2}", gson.toJson(expression));

    }

}
