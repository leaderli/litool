/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.text.StrPool;

import java.util.regex.Pattern;

/**
 * <p>Helpers to process Strings using regular expressions.</p>
 *
 * @see Pattern
 * @since 3.8
 */
public class RegExUtils {

/**
 * <p>Removes each substring of the text String that matches the given regular expression pattern.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code pattern.matcher(text).replaceAll(StringUtils.EMPTY)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.removeAll(null, *)      = null
 * LiStringUtils.removeAll("any", (Pattern) null)  = "any"
 * LiStringUtils.removeAll("any", Pattern.compile(""))    = "any"
 * LiStringUtils.removeAll("any", Pattern.compile(".*"))  = ""
 * LiStringUtils.removeAll("any", Pattern.compile(".+"))  = ""
 * LiStringUtils.removeAll("abc", Pattern.compile(".?"))  = ""
 * LiStringUtils.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\nB"
 * LiStringUtils.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
 * LiStringUtils.removeAll("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL))  = "AB"
 * LiStringUtils.removeAll("ABCabc123abc", Pattern.compile("[a-z]"))     = "ABC123"
 * </pre>
 *
 * @param text  text to remove from, may be null
 * @param regex the regular expression to which this string is to be matched
 * @return the text with any removes processed,
 * {@code null} if null String input
 * @see #replaceAll(String, Pattern, String)
 * @see java.util.regex.Matcher#replaceAll(String)
 * @see Pattern
 */
public static String removeAll(final String text, final Pattern regex) {
    return replaceAll(text, regex, StrPool.EMPTY);
}

/**
 * <p>Replaces each substring of the text String that matches the given regular expression pattern with the given
 * replacement.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code pattern.matcher(text).replaceAll(replacement)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.replaceAll(null, *, *)       = null
 * LiStringUtils.replaceAll("any", (Pattern) null, *)   = "any"
 * LiStringUtils.replaceAll("any", *, null)   = "any"
 * LiStringUtils.replaceAll("", Pattern.compile(""), "zzz")    = "zzz"
 * LiStringUtils.replaceAll("", Pattern.compile(".*"), "zzz")  = "zzz"
 * LiStringUtils.replaceAll("", Pattern.compile(".+"), "zzz")  = ""
 * LiStringUtils.replaceAll("abc", Pattern.compile(""), "ZZ")  = "ZZaZZbZZcZZ"
 * LiStringUtils.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")                 = "z\nz"
 * LiStringUtils.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;", Pattern.DOTALL), "z") = "z"
 * LiStringUtils.replaceAll("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")             = "z"
 * LiStringUtils.replaceAll("ABCabc123", Pattern.compile("[a-z]"), "_")       = "ABC___123"
 * LiStringUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123"
 * LiStringUtils.replaceAll("ABCabc123", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123"
 * LiStringUtils.replaceAll("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum_dolor_sit"
 * </pre>
 *
 * @param text        text to search and replace in, may be null
 * @param regex       the regular expression pattern to which this string is to be matched
 * @param replacement the string to be substituted for each match
 * @return the text with any replacements processed,
 * {@code null} if null String input
 * @see java.util.regex.Matcher#replaceAll(String)
 * @see Pattern
 */
public static String replaceAll(final String text, final Pattern regex, final String replacement) {
    if (text == null || regex == null || replacement == null) {
        return text;
    }
    return regex.matcher(text).replaceAll(replacement);
}

/**
 * <p>Removes each substring of the text String that matches the given regular expression.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code text.replaceAll(regex, LiStringUtils.EMPTY)}</li>
 *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(StringUtils.EMPTY)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <p>Unlike in the {@link #removePattern(String, String)} method, the {@link Pattern#DOTALL} option
 * is NOT automatically added.
 * To use the DOTALL option prepend {@code "(?s)"} to the regex.
 * DOTALL is also known as single-line mode in Perl.</p>
 *
 * <pre>
 * LiStringUtils.removeAll(null, *)      = null
 * LiStringUtils.removeAll("any", (String) null)  = "any"
 * LiStringUtils.removeAll("any", "")    = "any"
 * LiStringUtils.removeAll("any", ".*")  = ""
 * LiStringUtils.removeAll("any", ".+")  = ""
 * LiStringUtils.removeAll("abc", ".?")  = ""
 * LiStringUtils.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\nB"
 * LiStringUtils.removeAll("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
 * LiStringUtils.removeAll("ABCabc123abc", "[a-z]")     = "ABC123"
 * </pre>
 *
 * @param text  text to remove from, may be null
 * @param regex the regular expression to which this string is to be matched
 * @return the text with any removes processed,
 * {@code null} if null String input
 * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
 * @see #replaceAll(String, String, String)
 * @see #removePattern(String, String)
 * @see String#replaceAll(String, String)
 * @see Pattern
 * @see Pattern#DOTALL
 */
public static String removeAll(final String text, final String regex) {
    return replaceAll(text, regex, StrPool.EMPTY);
}

/**
 * <p>Replaces each substring of the text String that matches the given regular expression
 * with the given replacement.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code text.replaceAll(regex, replacement)}</li>
 *  <li>{@code Pattern.compile(regex).matcher(text).replaceAll(replacement)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <p>Unlike in the {@link #replacePattern(String, String, String)} method, the {@link Pattern#DOTALL} option
 * is NOT automatically added.
 * To use the DOTALL option prepend {@code "(?s)"} to the regex.
 * DOTALL is also known as single-line mode in Perl.</p>
 *
 * <pre>
 * LiStringUtils.replaceAll(null, *, *)       = null
 * LiStringUtils.replaceAll("any", (String) null, *)   = "any"
 * LiStringUtils.replaceAll("any", *, null)   = "any"
 * LiStringUtils.replaceAll("", "", "zzz")    = "zzz"
 * LiStringUtils.replaceAll("", ".*", "zzz")  = "zzz"
 * LiStringUtils.replaceAll("", ".+", "zzz")  = ""
 * LiStringUtils.replaceAll("abc", "", "ZZ")  = "ZZaZZbZZcZZ"
 * LiStringUtils.replaceAll("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\nz"
 * LiStringUtils.replaceAll("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
 * LiStringUtils.replaceAll("ABCabc123", "[a-z]", "_")       = "ABC___123"
 * LiStringUtils.replaceAll("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
 * LiStringUtils.replaceAll("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
 * LiStringUtils.replaceAll("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
 * </pre>
 *
 * @param text        text to search and replace in, may be null
 * @param regex       the regular expression to which this string is to be matched
 * @param replacement the string to be substituted for each match
 * @return the text with any replacements processed,
 * {@code null} if null String input
 * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
 * @see #replacePattern(String, String, String)
 * @see String#replaceAll(String, String)
 * @see Pattern
 * @see Pattern#DOTALL
 */
public static String replaceAll(final String text, final String regex, final String replacement) {
    if (text == null || regex == null || replacement == null) {
        return text;
    }
    return text.replaceAll(regex, replacement);
}

/**
 * <p>Removes the first substring of the text string that matches the given regular expression pattern.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code pattern.matcher(text).replaceFirst(StringUtils.EMPTY)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.removeFirst(null, *)      = null
 * LiStringUtils.removeFirst("any", (Pattern) null)  = "any"
 * LiStringUtils.removeFirst("any", Pattern.compile(""))    = "any"
 * LiStringUtils.removeFirst("any", Pattern.compile(".*"))  = ""
 * LiStringUtils.removeFirst("any", Pattern.compile(".+"))  = ""
 * LiStringUtils.removeFirst("abc", Pattern.compile(".?"))  = "bc"
 * LiStringUtils.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("&lt;.*&gt;"))      = "A\n&lt;__&gt;B"
 * LiStringUtils.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", Pattern.compile("(?s)&lt;.*&gt;"))  = "AB"
 * LiStringUtils.removeFirst("ABCabc123", Pattern.compile("[a-z]"))          = "ABCbc123"
 * LiStringUtils.removeFirst("ABCabc123abc", Pattern.compile("[a-z]+"))      = "ABC123abc"
 * </pre>
 *
 * @param text  text to remove from, may be null
 * @param regex the regular expression pattern to which this string is to be matched
 * @return the text with the first replacement processed,
 * {@code null} if null String input
 * @see #replaceFirst(String, Pattern, String)
 * @see java.util.regex.Matcher#replaceFirst(String)
 * @see Pattern
 */
public static String removeFirst(final String text, final Pattern regex) {
    return replaceFirst(text, regex, StrPool.EMPTY);
}

/**
 * <p>Replaces the first substring of the text string that matches the given regular expression pattern
 * with the given replacement.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code pattern.matcher(text).replaceFirst(replacement)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.replaceFirst(null, *, *)       = null
 * LiStringUtils.replaceFirst("any", (Pattern) null, *)   = "any"
 * LiStringUtils.replaceFirst("any", *, null)   = "any"
 * LiStringUtils.replaceFirst("", Pattern.compile(""), "zzz")    = "zzz"
 * LiStringUtils.replaceFirst("", Pattern.compile(".*"), "zzz")  = "zzz"
 * LiStringUtils.replaceFirst("", Pattern.compile(".+"), "zzz")  = ""
 * LiStringUtils.replaceFirst("abc", Pattern.compile(""), "ZZ")  = "ZZabc"
 * LiStringUtils.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("&lt;.*&gt;"), "z")      = "z\n&lt;__&gt;"
 * LiStringUtils.replaceFirst("&lt;__&gt;\n&lt;__&gt;", Pattern.compile("(?s)&lt;.*&gt;"), "z")  = "z"
 * LiStringUtils.replaceFirst("ABCabc123", Pattern.compile("[a-z]"), "_")          = "ABC_bc123"
 * LiStringUtils.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "_")  = "ABC_123abc"
 * LiStringUtils.replaceFirst("ABCabc123abc", Pattern.compile("[^A-Z0-9]+"), "")   = "ABC123abc"
 * LiStringUtils.replaceFirst("Lorem ipsum  dolor   sit", Pattern.compile("( +)([a-z]+)"), "_$2")  = "Lorem_ipsum  dolor   sit"
 * </pre>
 *
 * @param text        text to search and replace in, may be null
 * @param regex       the regular expression pattern to which this string is to be matched
 * @param replacement the string to be substituted for the first match
 * @return the text with the first replacement processed,
 * {@code null} if null String input
 * @see java.util.regex.Matcher#replaceFirst(String)
 * @see Pattern
 */
public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
    if (text == null || regex == null || replacement == null) {
        return text;
    }
    return regex.matcher(text).replaceFirst(replacement);
}

/**
 * <p>Removes the first substring of the text string that matches the given regular expression.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code text.replaceFirst(regex, LiStringUtils.EMPTY)}</li>
 *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(StringUtils.EMPTY)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
 * To use the DOTALL option prepend {@code "(?s)"} to the regex.
 * DOTALL is also known as single-line mode in Perl.</p>
 *
 * <pre>
 * LiStringUtils.removeFirst(null, *)      = null
 * LiStringUtils.removeFirst("any", (String) null)  = "any"
 * LiStringUtils.removeFirst("any", "")    = "any"
 * LiStringUtils.removeFirst("any", ".*")  = ""
 * LiStringUtils.removeFirst("any", ".+")  = ""
 * LiStringUtils.removeFirst("abc", ".?")  = "bc"
 * LiStringUtils.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")      = "A\n&lt;__&gt;B"
 * LiStringUtils.removeFirst("A&lt;__&gt;\n&lt;__&gt;B", "(?s)&lt;.*&gt;")  = "AB"
 * LiStringUtils.removeFirst("ABCabc123", "[a-z]")          = "ABCbc123"
 * LiStringUtils.removeFirst("ABCabc123abc", "[a-z]+")      = "ABC123abc"
 * </pre>
 *
 * @param text  text to remove from, may be null
 * @param regex the regular expression to which this string is to be matched
 * @return the text with the first replacement processed,
 * {@code null} if null String input
 * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
 * @see #replaceFirst(String, String, String)
 * @see String#replaceFirst(String, String)
 * @see Pattern
 * @see Pattern#DOTALL
 */
public static String removeFirst(final String text, final String regex) {
    return replaceFirst(text, regex, StrPool.EMPTY);
}

/**
 * <p>Replaces the first substring of the text string that matches the given regular expression
 * with the given replacement.</p>
 * <p>
 * This method is a {@code null} safe equivalent to:
 * <ul>
 *  <li>{@code text.replaceFirst(regex, replacement)}</li>
 *  <li>{@code Pattern.compile(regex).matcher(text).replaceFirst(replacement)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <p>The {@link Pattern#DOTALL} option is NOT automatically added.
 * To use the DOTALL option prepend {@code "(?s)"} to the regex.
 * DOTALL is also known as single-line mode in Perl.</p>
 *
 * <pre>
 * LiStringUtils.replaceFirst(null, *, *)       = null
 * LiStringUtils.replaceFirst("any", (String) null, *)   = "any"
 * LiStringUtils.replaceFirst("any", *, null)   = "any"
 * LiStringUtils.replaceFirst("", "", "zzz")    = "zzz"
 * LiStringUtils.replaceFirst("", ".*", "zzz")  = "zzz"
 * LiStringUtils.replaceFirst("", ".+", "zzz")  = ""
 * LiStringUtils.replaceFirst("abc", "", "ZZ")  = "ZZabc"
 * LiStringUtils.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")      = "z\n&lt;__&gt;"
 * LiStringUtils.replaceFirst("&lt;__&gt;\n&lt;__&gt;", "(?s)&lt;.*&gt;", "z")  = "z"
 * LiStringUtils.replaceFirst("ABCabc123", "[a-z]", "_")          = "ABC_bc123"
 * LiStringUtils.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "_")  = "ABC_123abc"
 * LiStringUtils.replaceFirst("ABCabc123abc", "[^A-Z0-9]+", "")   = "ABC123abc"
 * LiStringUtils.replaceFirst("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum  dolor   sit"
 * </pre>
 *
 * @param text        text to search and replace in, may be null
 * @param regex       the regular expression to which this string is to be matched
 * @param replacement the string to be substituted for the first match
 * @return the text with the first replacement processed,
 * {@code null} if null String input
 * @throws java.util.regex.PatternSyntaxException if the regular expression's syntax is invalid
 * @see String#replaceFirst(String, String)
 * @see Pattern
 * @see Pattern#DOTALL
 */
public static String replaceFirst(final String text, final String regex, final String replacement) {
    if (text == null || regex == null || replacement == null) {
        return text;
    }
    return text.replaceFirst(regex, replacement);
}

/**
 * <p>Removes each substring of the source String that matches the given regular expression using the DOTALL option.</p>
 * <p>
 * This call is a {@code null} safe equivalent to:
 * <ul>
 * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, LiStringUtils.EMPTY)}</li>
 * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(StringUtils.EMPTY)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.removePattern(null, *)       = null
 * LiStringUtils.removePattern("any", (String) null)   = "any"
 * LiStringUtils.removePattern("A&lt;__&gt;\n&lt;__&gt;B", "&lt;.*&gt;")  = "AB"
 * LiStringUtils.removePattern("ABCabc123", "[a-z]")    = "ABC123"
 * </pre>
 *
 * @param text  the source string
 * @param regex the regular expression to which this string is to be matched
 * @return The resulting {@code String}
 * @see #replacePattern(String, String, String)
 * @see String#replaceAll(String, String)
 * @see Pattern#DOTALL
 */
public static String removePattern(final String text, final String regex) {
    return replacePattern(text, regex, StrPool.EMPTY);
}

/**
 * <p>Replaces each substring of the source String that matches the given regular expression with the given
 * replacement using the {@link Pattern#DOTALL} option. DOTALL is also known as single-line mode in Perl.</p>
 * <p>
 * This call is a {@code null} safe equivalent to:
 * <ul>
 * <li>{@code text.replaceAll(&quot;(?s)&quot; + regex, replacement)}</li>
 * <li>{@code Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement)}</li>
 * </ul>
 *
 * <p>A {@code null} reference passed to this method is a no-op.</p>
 *
 * <pre>
 * LiStringUtils.replacePattern(null, *, *)       = null
 * LiStringUtils.replacePattern("any", (String) null, *)   = "any"
 * LiStringUtils.replacePattern("any", *, null)   = "any"
 * LiStringUtils.replacePattern("", "", "zzz")    = "zzz"
 * LiStringUtils.replacePattern("", ".*", "zzz")  = "zzz"
 * LiStringUtils.replacePattern("", ".+", "zzz")  = ""
 * LiStringUtils.replacePattern("&lt;__&gt;\n&lt;__&gt;", "&lt;.*&gt;", "z")       = "z"
 * LiStringUtils.replacePattern("ABCabc123", "[a-z]", "_")       = "ABC___123"
 * LiStringUtils.replacePattern("ABCabc123", "[^A-Z0-9]+", "_")  = "ABC_123"
 * LiStringUtils.replacePattern("ABCabc123", "[^A-Z0-9]+", "")   = "ABC123"
 * LiStringUtils.replacePattern("Lorem ipsum  dolor   sit", "( +)([a-z]+)", "_$2")  = "Lorem_ipsum_dolor_sit"
 * </pre>
 *
 * @param text        the source string
 * @param regex       the regular expression to which this string is to be matched
 * @param replacement the string to be substituted for each match
 * @return The resulting {@code String}
 * @see #replaceAll(String, String, String)
 * @see String#replaceAll(String, String)
 * @see Pattern#DOTALL
 */
public static String replacePattern(final String text, final String regex, final String replacement) {
    if (text == null || regex == null || replacement == null) {
        return text;
    }
    return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
}

}
