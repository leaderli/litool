package io.leaderli.litool.runner;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.instruct.FuncScope;
import io.leaderli.litool.runner.instruct.Instruct;

import java.lang.reflect.Method;

/**
 * @author leaderli
 * @since 2022/8/16 3:39 PM
 * <p>
 * 用来给实际调用指令传递返回类型的适配器
 */
public class InstructAdapter implements Instruct {

    public final Instruct instruct;
    public final Class<?> type;

    public InstructAdapter(Instruct instruct, String type) {
        this.instruct = instruct;
        this.type = TypeAlias.getType(type);
    }

    @Override
    public Object apply(Class<?> type, Object[] objects) {
        return instruct.apply(this.type, objects);
    }

    @Override
    public String name() {
        return instruct.name();
    }

    @Override
    public Lira<Method> getInstructMethod() {
        return instruct.getInstructMethod();
    }

    @Override
    public FuncScope getScope() {
        return instruct.getScope();
    }

    @Override
    public String toString() {
        return "InstructAdapter{" +
                "instruct=" + instruct +
                ", type=" + type +
                '}';
    }

    public Instruct newType(String type) {
        return new InstructAdapter(this.instruct, type);
    }
}
