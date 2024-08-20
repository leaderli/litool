package io.leaderli.litool.test.bean;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.util.stream.Collectors;


public class AsmUtil {

    public static String getInsn(MethodNode node) {
        Textifier text = new Textifier();
        node.accept(new TraceMethodVisitor(text));
        System.out.println(node.name + ":" + text.getText());
        return text.getText().stream().map(Object::toString).collect(Collectors.joining("\r\n"));
    }
}
