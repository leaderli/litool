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

    public static boolean isFinal(Member member) {
        return Modifier.isFinal(member.getModifiers());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isAbstract(Member member) {
        return Modifier.isAbstract(member.getModifiers());
    }

    /**
     * the Calculation formula:
     * <pre>
     * public  0b100
     * protected 0b10
     * default 0b1
     * private 0b0
     * </pre>
     * <p>
     * Higher priority means easier access
     *
     * @param member the member have modifier
     * @return the priority of {@link  Member}
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
