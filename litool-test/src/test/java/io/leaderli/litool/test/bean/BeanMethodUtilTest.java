package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.type.PrimitiveEnum;
import io.leaderli.litool.core.type.ReflectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

class BeanMethodUtilTest {


    @Test
    void test() {
        Method[] methods = BeanMethodUtil.scanSimpleMethod(Person.class);
        Person person = new Person();
        Assertions.assertEquals(5, methods.length);
        for (Method method : methods) {
            Object[] args = ArrayUtils.map(method.getParameterTypes(), Object.class, c -> PrimitiveEnum.get(c).zero_value);
            Assertions.assertDoesNotThrow(() -> ReflectUtil.invokeMethod(method, person, args).get());
        }
    }

}

class Base {
    public void setMap(Map<String, String> map) {

    }
}

class Person extends Base {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAge2(int age) {
        setAge(age);
    }

    public Person setAge3(int age) {
        this.age = age;
        return this;
    }

    public static int newAge(int age) {
        return age + 1;
    }

    public static Person newAge(Person person) {
        return person;
    }

    public static Person newAge2(Person person) {
        return newAge(person);
    }

    public static void newAge3(Person person) {
        person.setAge(1);
    }

    public void setMap(Map<String, String> map) {
        map.put("1", "1");
    }

    public void error(int a) {
        if (a == 0) {
            throw new IllegalStateException();
        }
    }
}
