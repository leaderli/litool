package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.Opcodes.ASM9;

public class BeanMethodUtil {


    /**
     * 一些类的所有方法被视为简单方法，例如基础包装类，StringBuilder。
     */
    private final static Set<String> SIMPLE_OWNER_SET = new HashSet<>();

    static {
        SIMPLE_OWNER_SET.add(StringBuilder.class.getName().replace(".", "/"));
        PrimitiveEnum.PRIMITIVE_WRAPPER_MAP.values().forEach(c -> SIMPLE_OWNER_SET.add(c.getName().replace(".", "/")));
    }


    public static Method[] scanSimpleMethod(final Class<?> clazz, boolean allowInit) {
        return scanSimpleMethod(clazz, "", allowInit);
    }

    public static Method[] scanSimpleMethod(final Class<?> clazz, String packageName, boolean allowInit) {

        Map<String, Method> foundMethods = new HashMap<>();
        Set<Class<?>> methodDeclaredClasses = new HashSet<>();

        Class<?> temp = clazz;
        while (temp != null && temp != Object.class && temp.getPackage().getName().startsWith(packageName)) {
            methodDeclaredClasses.add(temp);
            for (Method method : temp.getDeclaredMethods()) {
                if (method.isSynthetic() && !method.isBridge()) {
                    continue;
                }
                foundMethods.putIfAbsent(MethodUtil.nameDesc(method), method);
            }
            temp = temp.getSuperclass();
        }
        Map<String, LiTuple<Method, MethodNode>> ownerNameDescMethodAndNodeMap = new HashMap<>();
        for (Class<?> methodDeclaredClass : methodDeclaredClasses) {
            visitMethod(methodDeclaredClass, foundMethods, ownerNameDescMethodAndNodeMap);
        }
        Map<String, LiTuple<Boolean, Method>> simpleMethods = new HashMap<>();
        ownerNameDescMethodAndNodeMap.forEach((ownerNameDesc, methodAndNode) -> {
            Method method = methodAndNode._1;
            boolean simple = isSimpleMethod(ownerNameDesc, ownerNameDescMethodAndNodeMap, allowInit, simpleMethods);
            simpleMethods.put(ownerNameDesc, LiTuple.of(simple, method));
        });
        return Lira.of(simpleMethods.values())
                .filter(m -> m._1)
                .map(m -> m._2)
                .toArray(Method.class);
    }

    /**
     * 是否为简单方法，方法体内内的字节码指令，是否不包含复杂方法调用。部分方法调用会去进一步分析，是否属于简单方法，如果为简单方法则也该指令也视为简单方法
     *
     * @param allowInit 是否允许构造器方法
     * @see org.objectweb.asm.Opcodes#INVOKEVIRTUAL 182
     * @see org.objectweb.asm.Opcodes#INVOKESPECIAL 183
     * @see org.objectweb.asm.Opcodes#INVOKESTATIC 184
     * @see org.objectweb.asm.Opcodes#INVOKEVIRTUAL 185
     * @see org.objectweb.asm.Opcodes#INVOKEDYNAMIC 186
     */
    private static boolean isSimpleMethod(String ownerNameDesc, Map<String, LiTuple<Method, MethodNode>> ownerNameDesc_MethodAndNode, boolean allowInit, Map<String, LiTuple<Boolean, Method>> simpleMethods) {

        LiTuple<Method, MethodNode> methodMethodNodeLiTuple = ownerNameDesc_MethodAndNode.get(ownerNameDesc);
        // 仅分析查找类涉及的方法
        if (methodMethodNodeLiTuple == null) {
            return false;
        }
        if (simpleMethods.containsKey(ownerNameDesc)) {
            return simpleMethods.get(ownerNameDesc)._1;
        }
        MethodNode methodNode = methodMethodNodeLiTuple._2;
        for (AbstractInsnNode ins : IterableItr.of(methodNode.instructions.iterator())) {
            if (ins.getOpcode() > -1) {
                if (ins instanceof MethodInsnNode) {
                    MethodInsnNode mi = (MethodInsnNode) ins;
                    if (allowInit) {
                        if (mi.name.equals("<init>")) {
                            continue;
                        }
                    }
                    // 某些类的所有方法，视为简单函数，例如StringBuilder的append方法，基础类的装箱、拆箱方法
                    if (SIMPLE_OWNER_SET.contains(mi.owner)) {
                        continue;
                    }
                    String invokeMethod = mi.owner + "." + mi.name + mi.desc;

                    // 进一步分析调用的指令
                    if (!isSimpleMethod(invokeMethod, ownerNameDesc_MethodAndNode, allowInit, simpleMethods)) {
                        return false;
                    }
                    return false;

                } else if (ins instanceof InsnNode) {
                    if (ins.getOpcode() == Opcodes.ATHROW) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static void visitMethod(Class<?> methodDeclaredClass, Map<String, Method> methodMap, Map<String, LiTuple<Method, MethodNode>> methodVisitors) {
        try {
            ClassReader classReader = new ClassReader(methodDeclaredClass.getName());
            ClassVisitor classVisitor = new ClassVisitor(ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    String id = name + desc;
                    Method method = methodMap.get(id);
                    if (method != null && method.getDeclaringClass() == methodDeclaredClass) {
                        MethodNode methodNode = new MethodNode(ASM9, access, name, desc, signature, exceptions);
                        methodVisitors.put(method.getDeclaringClass().getName().replace(".", "/") + "." + id, LiTuple.of(method, methodNode));
                        return methodNode;
                    }
                    return super.visitMethod(access, name, desc, signature, exceptions);
                }

            };
            classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
