package io.leaderli.litool.core.type;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * A  tool for {@link  Modifier}, it used at {@link  Member}
 *
 * @author leaderli
 * @since 2022/8/9
 */
public class ModifierUtil {


    /**
     * 获取成员变量访问权限的优先级
     * <p>
     * 计算公式如下：
     * <pre>
     * public    0b100
     * protected 0b010
     * default   0b001
     * private   0b000
     * </pre>
     * <p>
     * 优先级越高，则表示访问权限越大
     *
     * @param member 成员变量
     * @return 成员变量访问权限的优先级
     */
    public static int priority(Member member) {
        int priority;
        if (ModifierUtil.isPublic(member)) {
            priority = 0b100;
        } else if (ModifierUtil.isProtected(member)) {
            priority = 0b10;
        } else if (ModifierUtil.isPrivate(member)) {
            priority = 0;
        } else {
            priority = 1;
        }
        return priority;
    }

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isAbstract(Member member) {
        return Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isAbstract(Class<?> cls) {
        return Modifier.isAbstract(cls.getModifiers());
    }

    public static boolean isStatic(Class<?> cls) {
        return Modifier.isStatic(cls.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }


}
