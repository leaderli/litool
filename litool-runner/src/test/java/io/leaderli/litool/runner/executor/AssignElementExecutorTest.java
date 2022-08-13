package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassScanner;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.RequestElement;
import io.leaderli.litool.runner.xml.router.SequenceElement;
import io.leaderli.litool.runner.xml.router.task.AssignElement;
import io.leaderli.litool.runner.xml.router.task.TaskElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AssignElementExecutorTest {

    @Test
    void test() throws InvocationTargetException, InstantiationException, IllegalAccessException {

//        System.out.println(ClassScanner.getSubTypesOf(Context.class, ElementExecutor.class));
//        Class<?> x = ReflectUtil.getGenericSuperclassType(AssignElement.class, TaskElement.class, 1).get();
//        Constructor<?> constructor = ReflectUtil.getConstructors(x)
//                .filter(c -> c.getParameterTypes().length == 1)
//                .filter(c -> SaxBean.class.isAssignableFrom(c.getParameterTypes()[0])).first().get();
//
//        System.out.println(constructor);
//        constructor.newInstance(new AssignElement());
//        System.out.println(x);
////        System.out.println(ReflectUtil.getInterfacesType(SequenceElement.class));
//        System.out.println(ReflectUtil.getGenericInterfacesType(SequenceElement.class, ElementExecutor.class,1));
//
        System.out.println(new RequestElement().executor());

    }
    @Test
    void visit() {
        SaxEventInterceptor<AssignElement> dfs = new SaxEventInterceptor<>(AssignElement.class);
        AssignElement element = dfs.parse("router/task/assign.xml");

        Map<String, String> request = new HashMap<>();
        Context context = new Context(request);
        context.visit(new AssignElementExecutor(element));

        Assertions.assertTrue(StringUtils.equals(context.getResponse("skill"), "123"));
    }

    @Test
    void name_error() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement element = dfs.parse("router/task/assign_error_name.xml");

        List<String> list = new ArrayList<>();
        element.end_check(list);
        Assertions.assertTrue(StringUtils.equals(list.get(0), "response variable [abc] not exists"));
    }
}
