package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.util.ConsoleUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class BeanMethodUtilTest {


    @Test
    void test() {
        Method[] methods = BeanMethodUtil.scanSimpleMethod(Person.class);
        ConsoleUtil.line();
        for (Method method : methods) {
            System.out.println(method);
        }
    }

}

class Person {
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

    public static int newAge(int age) {
        return age + 1;
    }

    public static Person newAge(Person person) {
        return person;
    }

    public static Person newAge2(Person person) {
        return newAge(person);
    }
}
