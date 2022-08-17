package io.leaderli.litool.core.type;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author leaderli
 * @since 2022/8/9
 */
public class ModifierUtil {

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }

    public static boolean isProtected(Member member) {
        return Modifier.isProtected(member.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }
    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    /**
     * 计算公式如下
     * <p>
     * public  0b100
     * protected 0b10
     * default 0b1
     * private 0b0
     *
     * @param member 返回可访问的权限的优先级
     * @return 优先级
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


}
