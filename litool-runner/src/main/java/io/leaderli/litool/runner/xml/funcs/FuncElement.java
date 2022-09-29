package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.dom.LiDomConstant;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.instruct.Instruct;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class FuncElement extends SaxBean {

    private String label;
    private String name;
    private Instruct instruct;
    private String type;

    private ParamList params = new ParamList();

    public FuncElement() {
        super("func");
    }

    public void addParam(ParamElement paramElement) {
        params.add(paramElement);
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
        LiAssertUtil.assertTrue(name.matches(LiDomConstant.ATTRIBUTE_NAME_RULE), String.format("the func name [%s] is" +
                " " +
                "not " +
                "match %s", name, LiDomConstant.ATTRIBUTE_NAME_RULE));
        this.name = name;
    }

    public Instruct getInstruct() {
        return instruct;
    }

    public void setInstruct(Instruct instruct) {
        this.instruct = instruct;
        Class<?> returnType = instruct.getInstructMethod().first().get().getReturnType();
        String _type = Lira.of(TypeAlias.getALIAS().entrySet())
                .filter(entry -> entry.getValue() == returnType)
                .map(Map.Entry::getKey)
                .first()
                .get();
        this.setType(_type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        LiAssertUtil.assertTrue(TypeAlias.getALIAS().containsKey(type), String.format("the func type [%s] is " +
                        "unsupported",
                type));
        this.type = type;
    }

    public ParamList getParams() {
        return params;
    }

    public void setParams(ParamList params) {
        this.params = params;
    }

    @Override
    public Lino<?> complexField(Class<?> parameterType, String value) {

        if (parameterType == Instruct.class) {

            return Lino.of(InstructContainer.getInnerMethodByAlias(value)).assertNotNone(String.format("instruct [%s]" +
                    " is " +
                    "unsupported", value));
        }
        return super.complexField(parameterType, value);
    }

    @Override
    public void end(EndEvent endEvent) {
        super.end(endEvent);

        final Class<?>[] paramListTypes = params.lira()
                .map(p -> TypeAlias.getType(p.getType())).cast(Class.class)
                .toArray(Class.class);
        boolean present = this.instruct.getInstructMethod()
                .filter(m -> m.getReturnType() == TypeAlias.getType(type))
                .filter(method -> {
                    Class<?>[] methodParameterTypes = method.getParameterTypes();

                    if (methodParameterTypes.length > 0) {
                        final Class<?> lastParameterType = methodParameterTypes[methodParameterTypes.length - 1];
                        // 当实际方法最后一位为可选参数时，将其平铺成与标签 param 类型数组数量相同的数组
                        if (lastParameterType.isArray()) {

                            Class<?>[] flat = new Class[paramListTypes.length - methodParameterTypes.length + 1];
                            for (int i = 0; i < flat.length; i++) {
                                flat[i] = lastParameterType.getComponentType();
                            }
                            methodParameterTypes = ArrayUtils.combination(ArrayUtils.subArray(methodParameterTypes, 0,
                                    -1), flat);
                        }
                    }
                    return Objects.deepEquals(paramListTypes, methodParameterTypes);
                }).present();


        LiAssertUtil.assertTrue(present, () -> String.format("the func [%s] parameterType %s is  not match clazz [%s]",
                name, Arrays.toString(paramListTypes), instruct.name()));

    }
}
