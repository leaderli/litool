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
        Method[] methods = BeanMethodUtil.scanSimpleMethod(Person.class, false);
        Person person = new Person();
        for (Method method : methods) {
            System.out.println(method);
        }
        Assertions.assertEquals(9, methods.length);
        for (Method method : methods) {
            Object[] args = ArrayUtils.map(method.getParameterTypes(), Object.class, c -> PrimitiveEnum.get(c).zero_value);
            Assertions.assertDoesNotThrow(() -> ReflectUtil.invokeMethod(method, person, args).get());
        }

        methods = BeanMethodUtil.scanSimpleMethod(Person.class, true);
        Assertions.assertEquals(10, methods.length);
        for (Method method : methods) {
            Object[] args = ArrayUtils.map(method.getParameterTypes(), Object.class, c -> PrimitiveEnum.get(c).zero_value);
            Assertions.assertDoesNotThrow(() -> ReflectUtil.invokeMethod(method, person, args).get());
        }

        Assertions.assertEquals(1, BeanMethodUtil.scanSimpleMethod(Util.class, false).length);
    }

}

abstract class Base<T> {
    public void setMap(Map<String, String> map) {

    }

    public abstract void setMap2(Map<String, String> map);

    public abstract T setT(T t);

    public abstract T setT2(T t);


}

class Util {
    private Util() {
        throw new UnsupportedOperationException();
    }

    public static int init(int age) {
        return age++;
    }
}

class Person extends Base<String> {

    public Person(int age) {
        this.age = age;
        if (this.age == 0) {
            throw new IllegalArgumentException();
        }
    }

    private int age;

    public Person() {
        this.age = 1;
    }

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

    public static Person newAge4(int age) {
        return new Person(age);
    }
    public void setMap(Map<String, String> map) {
        map.put("1", "1");
    }

    public void setMap2(Map<String, String> map) {
        map.put("1", "1");
    }

    @Override
    public String setT(String s) {
        return s.length() + "";
    }

    @Override
    public String setT2(String s) {
        return "";
    }

    public String setStr(String s) {
        return s + "." + s;
    }

    public int setInt(Integer integer) {
        return integer + 1;
    }

    public Integer setInt(int integer) {
        return integer + 1;
    }

    public void init() {
        this.age = new Person().age;
    }

    public void error(int a) {
        if (a == 0) {
            throw new IllegalStateException();
        }
    }
}
