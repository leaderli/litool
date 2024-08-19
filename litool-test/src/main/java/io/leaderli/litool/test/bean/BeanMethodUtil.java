package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.Opcodes.ASM6;

public class BeanMethodUtil {

    private static String methodID(int access, String name, String desc) {
        return Modifier.toString(access) + " " + name + ":" + desc;
    }


    public static Method[] scanSimpleMethod(final Class<?> clazz) {


        Map<String, Method> methodMap = ReflectUtil.getMethods(clazz).toMap(m -> methodID(m.getModifiers(), m.getName(), MethodUtil.getMethodDescriptor(m)), m -> m);
        Set<Class<?>> methodDeclaredClasses = new HashSet<>();
        methodMap.forEach((k, v) -> methodDeclaredClasses.add(v.getDeclaringClass()));
        Map<Method, MethodNode> methodVisitors = new HashMap<>();

        for (Class<?> methodDeclaredClass : methodDeclaredClasses) {
            visitMethod(methodDeclaredClass, methodMap, methodVisitors);
        }
        return Lira.of(methodVisitors.entrySet())
                .filter(e -> isSimpleMethod(e.getValue()))
                .map(Map.Entry::getKey)
                .toArray(Method.class);

    }

    /**
     * 是否为简单方法，方法体内内的字节码指令，是否不包含方法调用
     *
     * @see org.objectweb.asm.Opcodes#INVOKEVIRTUAL 182
     * @see org.objectweb.asm.Opcodes#INVOKESPECIAL 183
     * @see org.objectweb.asm.Opcodes#INVOKESTATIC 184
     * @see org.objectweb.asm.Opcodes#INVOKEVIRTUAL 185
     * @see org.objectweb.asm.Opcodes#INVOKEDYNAMIC 186
     */
    private static boolean isSimpleMethod(MethodNode methodNode) {
        for (AbstractInsnNode ins : IterableItr.of(methodNode.instructions.iterator())) {
            if (ins.getOpcode() > -1) {
                if (ins instanceof MethodInsnNode) {
                    return false;
                }
            }
        }
        return true;
    }


    private static void visitMethod(Class<?> methodDeclaredClass, Map<String, Method> methodMap, Map<Method, MethodNode> methodVisitors) {
        try {
            ClassReader classReader = new ClassReader(methodDeclaredClass.getName());
            ClassVisitor classVisitor = new ClassVisitor(ASM6) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    String id = methodID(access, name, desc);
                    Method method = methodMap.get(id);
                    if (method != null) {
                        MethodNode methodNode = new MethodNode(ASM6, access, name, desc, signature, exceptions);
                        methodVisitors.put(method, methodNode);
                        return methodNode;
                    }
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }
            };
            classReader.accept(classVisitor, 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
