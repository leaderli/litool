package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegExUtilsTest {
    @Test
    public void testRemoveAll() {
        assertNull(RegExUtils.removeAll(null, (Pattern) null));
        assertEquals("any", RegExUtils.removeAll("any", (Pattern) null));
        assertEquals("any", RegExUtils.removeAll("any", Pattern.compile("")));
        assertEquals("", RegExUtils.removeAll("any", Pattern.compile(".*")));
        assertEquals("", RegExUtils.removeAll("any", Pattern.compile(".+")));
        assertEquals("", RegExUtils.removeAll("abc", Pattern.compile(".?")));
        assertEquals("A\nB", RegExUtils.removeAll("A<__>\n<__>B", Pattern.compile("<.*>")));
        assertEquals("AB", RegExUtils.removeAll("A<__>\n<__>B", Pattern.compile("(?s)<.*>")));
        assertEquals("AB", RegExUtils.removeAll("A<__>\n<__>B", Pattern.compile("<.*>", Pattern.DOTALL)));
        assertEquals("ABC123", RegExUtils.removeAll("ABCabc123abc", Pattern.compile("[a-z]")));
    }


    @Test
    public void testReplaceAll() {
        assertNull(RegExUtils.replaceAll(null, (Pattern) null, null));
        assertEquals("any", RegExUtils.replaceAll("any", (Pattern) null, null));
        assertEquals("any", RegExUtils.replaceAll("any", (Pattern) null, "something"));
        assertEquals("zzz", RegExUtils.replaceAll("", Pattern.compile(""), "zzz"));
        assertEquals("zzz", RegExUtils.replaceAll("", Pattern.compile(".*"), "zzz"));
        assertEquals("", RegExUtils.replaceAll("", Pattern.compile(".+"), "zzz"));
        assertEquals("ZZaZZbZZcZZ", RegExUtils.replaceAll("abc", Pattern.compile(""), "ZZ"));
        assertEquals("z\nz", RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>"), "z"));
        assertEquals("z", RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>", Pattern.DOTALL), "z"));
        assertEquals("z", RegExUtils.replaceAll("<__>\n<__>", Pattern.compile("(?s)<.*>"), "z"));
        assertEquals("ABC___123", RegExUtils.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_"));
        assertEquals("ABC_123", RegExUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_"));
        assertEquals("ABC123", RegExUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), ""));
        assertEquals("Lorem_ipsum_dolor_sit", RegExUtils.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2"));
    }
}
