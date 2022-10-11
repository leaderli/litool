package io.leaderli.litool.action;

import io.leaderli.litool.core.text.StrSubstitution;

public class Demo {

    public static void main(String[] args) {
        String format = "{$java.home}123";

//        System.out.println(System.getProperties());
        System.out.println(StrSubstitution.format(format, v -> {
            if (v.startsWith("$")) {
                return System.getProperty(v.substring(1));
            }
            return v;
        }));
    }

}
