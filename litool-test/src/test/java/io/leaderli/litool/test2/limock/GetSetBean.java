package io.leaderli.litool.test2.limock;

/**
 * 用于测试
 */
public class GetSetBean {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age + 1;
    }
}
