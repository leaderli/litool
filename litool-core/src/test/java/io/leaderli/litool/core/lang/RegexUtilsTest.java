package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegexUtilsTest {
    @Test
    public void testRemoveAll() {
        assertNull(RegexUtils.removeAll(null, (Pattern) null));
        assertEquals("any", RegexUtils.removeAll("any", (Pattern) null));
        assertEquals("any", RegexUtils.removeAll("any", Pattern.compile("")));
        assertEquals("", RegexUtils.removeAll("any", Pattern.compile(".*")));
        assertEquals("", RegexUtils.removeAll("any", Pattern.compile(".+")));
        assertEquals("", RegexUtils.removeAll("abc", Pattern.compile(".?")));
        assertEquals("A\nB", RegexUtils.removeAll("A<__>\n<__>B", Pattern.compile("<.*>")));
        assertEquals("AB", RegexUtils.removeAll("A<__>\n<__>B", Pattern.compile("(?s)<.*>")));
        assertEquals("AB", RegexUtils.removeAll("A<__>\n<__>B", Pattern.compile("<.*>", Pattern.DOTALL)));
        assertEquals("ABC123", RegexUtils.removeAll("ABCabc123abc", Pattern.compile("[a-z]")));
    }


    @Test
    public void testReplaceAll() {
        assertNull(RegexUtils.replaceAll(null, (Pattern) null, null));
        assertEquals("any", RegexUtils.replaceAll("any", (Pattern) null, null));
        assertEquals("any", RegexUtils.replaceAll("any", (Pattern) null, "something"));
        assertEquals("zzz", RegexUtils.replaceAll("", Pattern.compile(""), "zzz"));
        assertEquals("zzz", RegexUtils.replaceAll("", Pattern.compile(".*"), "zzz"));
        assertEquals("", RegexUtils.replaceAll("", Pattern.compile(".+"), "zzz"));
        assertEquals("ZZaZZbZZcZZ", RegexUtils.replaceAll("abc", Pattern.compile(""), "ZZ"));
        assertEquals("z\nz", RegexUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>"), "z"));
        assertEquals("z", RegexUtils.replaceAll("<__>\n<__>", Pattern.compile("<.*>", Pattern.DOTALL), "z"));
        assertEquals("z", RegexUtils.replaceAll("<__>\n<__>", Pattern.compile("(?s)<.*>"), "z"));
        assertEquals("ABC___123", RegexUtils.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_"));
        assertEquals("ABC_123", RegexUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_"));
        assertEquals("ABC123", RegexUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), ""));
        assertEquals("Lorem_ipsum_dolor_sit", RegexUtils.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2"));
    }
}
