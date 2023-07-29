package io.leaderli.litool.ast;


import java.util.ArrayList;

public class TestBean {

    public static void main(String[] args) {
        SourceCodeUtil.getSources();
        io.leaderli.litool.ast.SourceCodeUtil.getSources();

    }

    public void m1() {
        java.util.List<Integer> list = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();
    }
}
