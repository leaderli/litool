package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.TypeAlias;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class FuncElement implements SaxBean {

    private String label;
    private String name;
    private String instruct;
    private String type;

    private ParamList paramList = new ParamList();

    public void addParam(ParamElement paramElement) {
        paramList.add(paramElement);
    }

    @Override
    public void end(EndEvent endEvent) {


        Method method = InstructContainer.getInnerMethodByAlias(instruct);

        final Class<?>[] paramListTypes = paramList.lira()
                .map(p -> TypeAlias.getType(p.getType())).cast(Class.class)
                .toArray(Class.class);

        Class<?>[] methodParameterTypes = method.getParameterTypes();

        if (methodParameterTypes.length > 0) {
            final Class<?> lastParameterType = methodParameterTypes[methodParameterTypes.length - 1];
            // 当实际方法最后一位为可选参数时，将其平铺成与标签 param 类型数组数量相同的数组
            if (lastParameterType.isArray()) {

                Class<?>[] flat = new Class[paramListTypes.length - methodParameterTypes.length + 1];
                for (int i = 0; i < flat.length; i++) {
                    flat[i] = lastParameterType.getComponentType();
                }
                methodParameterTypes = ArrayUtils.combination(ArrayUtils.sub(methodParameterTypes, 0, -1), flat);
            }
        }

        LiAssertUtil.assertTrue(Objects.deepEquals(paramListTypes, methodParameterTypes), () -> String.format("the func [%s] parameterType is  not match clazz [%s] parameterType \r\n\t%s\r\n\t%s\r\n", name, instruct, Arrays.toString(paramListTypes), Arrays.toString(method.getParameterTypes())));
        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "func";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        LiAssertUtil.assertTrue(name.matches(LiConstant.ATTRIBUTE_NAME_RULE), String.format("the func name [%s] is not match %s", name, LiConstant.ATTRIBUTE_NAME_RULE));
        this.name = name;
    }

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
        Method innerFunc = InstructContainer.getInnerMethodByAlias(this.instruct);
        LiAssertUtil.assertTrue(innerFunc != null, String.format("the inner func [%s] is unsupported", instruct));
        String type = Lira.of(TypeAlias.ALIAS.entrySet())
                .filter(entry -> entry.getValue() == innerFunc.getReturnType())
                .map(Map.Entry::getKey)
                .first()
                .get();
        this.setType(type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        LiAssertUtil.assertTrue(TypeAlias.ALIAS.containsKey(type), String.format("the func type [%s] is unsupported", type));
        this.type = type;
    }

    public ParamList getParamList() {
        return paramList;
    }

    public void setParamList(ParamList paramList) {
        this.paramList = paramList;
    }
}
