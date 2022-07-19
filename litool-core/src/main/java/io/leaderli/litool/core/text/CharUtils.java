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
package io.leaderli.litool.core.text;

import io.leaderli.litool.core.exception.LiAssertUtil;

/**
 * <p>Operations on char primitives and Character objects.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behaviour in more detail.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 2.1
 */
public class CharUtils implements CharPool {

    private static final String[] CHAR_STRING_ARRAY = new String[128];
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static {
        for (char c = 0; c < CHAR_STRING_ARRAY.length; c++) {
            CHAR_STRING_ARRAY[c] = String.valueOf(c);
        }
    }

    /**
     * <p>{@code LiCharUtils} instances should NOT be constructed in standard programming.
     * Instead, the class should be used as {@code LiCharUtils.toString('c');}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public CharUtils() {
        super();
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Converts the character to a Character.</p>
     *
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same Character object each time.</p>
     *
     * <pre>
     *   LiCharUtils.toCharacterObject(' ')  = ' '
     *   LiCharUtils.toCharacterObject('A')  = 'A'
     * </pre>
     *
     * @param ch the character to convert
     * @return a Character of the specified character
     * @deprecated Java 5 introduced {@link Character#valueOf(char)} which caches chars 0 through 127.
     */
    @Deprecated
    public static Character toCharacterObject(final char ch) {
        return ch;
    }

    /**
     * <p>Converts the String to a Character using the first character, returning
     * null for empty Strings.</p>
     *
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same Character object each time.</p>
     *
     * <pre>
     *   LiCharUtils.toCharacterObject(null) = null
     *   LiCharUtils.toCharacterObject("")   = null
     *   LiCharUtils.toCharacterObject("A")  = 'A'
     *   LiCharUtils.toCharacterObject("BA") = 'B'
     * </pre>
     *
     * @param str the character to convert
     * @return the Character value of the first letter of the String
     */
    public static Character toCharacterObject(final String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str.charAt(0);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Converts the Character to a char throwing an exception for {@code null}.</p>
     *
     * <pre>
     *   LiCharUtils.toChar(' ')  = ' '
     *   LiCharUtils.toChar('A')  = 'A'
     *   LiCharUtils.toChar(null) throws IllegalArgumentException
     * </pre>
     *
     * @param ch the character to convert
     * @return the char value of the Character
     * @throws IllegalArgumentException if the Character is null
     */
    public static char toChar(final Character ch) {
        LiAssertUtil.assertTrue(ch != null, "The Character must not be null");
        return ch;
    }

    /**
     * <p>Converts the Character to a char handling {@code null}.</p>
     *
     * <pre>
     *   LiCharUtils.toChar(null, 'X') = 'X'
     *   LiCharUtils.toChar(' ', 'X')  = ' '
     *   LiCharUtils.toChar('A', 'X')  = 'A'
     * </pre>
     *
     * @param ch           the character to convert
     * @param defaultValue the value to use if the  Character is null
     * @return the char value of the Character or the default if null
     */
    public static char toChar(final Character ch, final char defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return ch;
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Converts the String to a char using the first character, throwing
     * an exception on empty Strings.</p>
     *
     * <pre>
     *   LiCharUtils.toChar("A")  = 'A'
     *   LiCharUtils.toChar("BA") = 'B'
     *   LiCharUtils.toChar(null) throws IllegalArgumentException
     *   LiCharUtils.toChar("")   throws IllegalArgumentException
     * </pre>
     *
     * @param str the character to convert
     * @return the char value of the first letter of the String
     * @throws IllegalArgumentException if the String is empty
     */
    public static char toChar(final String str) {
        LiAssertUtil.assertTrue(StringUtils.isNotEmpty(str), "The String must not be empty");
        return str.charAt(0);
    }

    /**
     * <p>Converts the String to a char using the first character, defaulting
     * the value on empty Strings.</p>
     *
     * <pre>
     *   LiCharUtils.toChar(null, 'X') = 'X'
     *   LiCharUtils.toChar("", 'X')   = 'X'
     *   LiCharUtils.toChar("A", 'X')  = 'A'
     *   LiCharUtils.toChar("BA", 'X') = 'B'
     * </pre>
     *
     * @param str          the character to convert
     * @param defaultValue the value to use if the  Character is null
     * @return the char value of the first letter of the String or the default if null
     */
    public static char toChar(final String str, final char defaultValue) {
        if (StringUtils.isEmpty(str)) {
            return defaultValue;
        }
        return str.charAt(0);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Converts the character to the Integer it represents, throwing an
     * exception if the character is not numeric.</p>
     *
     * <p>This method converts the char '1' to the int 1 and so on.</p>
     *
     * <pre>
     *   LiCharUtils.toIntValue('3')  = 3
     *   LiCharUtils.toIntValue(null) throws IllegalArgumentException
     *   LiCharUtils.toIntValue('A')  throws IllegalArgumentException
     * </pre>
     *
     * @param ch the character to convert, not null
     * @return the int value of the character
     * @throws IllegalArgumentException if the Character is not ASCII numeric or is null
     */
    public static int toIntValue(final Character ch) {
        LiAssertUtil.assertTrue(ch != null, "The character must not be null");
        return toIntValue(ch.charValue());
    }

    /**
     * <p>Converts the character to the Integer it represents, throwing an
     * exception if the character is not numeric.</p>
     *
     * <p>This method converts the char '1' to the int 1 and so on.</p>
     *
     * <pre>
     *   LiCharUtils.toIntValue('3')  = 3
     *   LiCharUtils.toIntValue('A')  throws IllegalArgumentException
     * </pre>
     *
     * @param ch the character to convert
     * @return the int value of the character
     * @throws IllegalArgumentException if the character is not ASCII numeric
     */
    public static int toIntValue(final char ch) {
        if (!isAsciiNumeric(ch)) {
            throw new IllegalArgumentException("The character " + ch + " is not in the range '0' - '9'");
        }
        return ch - 48;
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit numeric.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiNumeric('a')  = false
     *   LiCharUtils.isAsciiNumeric('A')  = false
     *   LiCharUtils.isAsciiNumeric('3')  = true
     *   LiCharUtils.isAsciiNumeric('-')  = false
     *   LiCharUtils.isAsciiNumeric('\n') = false
     *   LiCharUtils.isAsciiNumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 48 and 57 inclusive
     */
    public static boolean isAsciiNumeric(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * <p>Converts the character to the Integer it represents, throwing an
     * exception if the character is not numeric.</p>
     *
     * <p>This method converts the char '1' to the int 1 and so on.</p>
     *
     * <pre>
     *   LiCharUtils.toIntValue(null, -1) = -1
     *   LiCharUtils.toIntValue('3', -1)  = 3
     *   LiCharUtils.toIntValue('A', -1)  = -1
     * </pre>
     *
     * @param ch           the character to convert
     * @param defaultValue the default value to use if the character is not numeric
     * @return the int value of the character
     */
    public static int toIntValue(final Character ch, final int defaultValue) {
        if (ch == null) {
            return defaultValue;
        }
        return toIntValue(ch.charValue(), defaultValue);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Converts the character to the Integer it represents, throwing an
     * exception if the character is not numeric.</p>
     *
     * <p>This method converts the char '1' to the int 1 and so on.</p>
     *
     * <pre>
     *   LiCharUtils.toIntValue('3', -1)  = 3
     *   LiCharUtils.toIntValue('A', -1)  = -1
     * </pre>
     *
     * @param ch           the character to convert
     * @param defaultValue the default value to use if the character is not numeric
     * @return the int value of the character
     */
    public static int toIntValue(final char ch, final int defaultValue) {
        if (!isAsciiNumeric(ch)) {
            return defaultValue;
        }
        return ch - 48;
    }

    /**
     * <p>Converts the character to a String that contains the one character.</p>
     *
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.</p>
     *
     * <p>If {@code null} is passed in, {@code null} will be returned.</p>
     *
     * <pre>
     *   LiCharUtils.toString(null) = null
     *   LiCharUtils.toString(' ')  = " "
     *   LiCharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(final Character ch) {
        if (ch == null) {
            return null;
        }
        return toString(ch.charValue());
    }

    //--------------------------------------------------------------------------

    /**
     * <p>Converts the character to a String that contains the one character.</p>
     *
     * <p>For ASCII 7 bit characters, this uses a cache that will return the
     * same String object each time.</p>
     *
     * <pre>
     *   LiCharUtils.toString(' ')  = " "
     *   LiCharUtils.toString('A')  = "A"
     * </pre>
     *
     * @param ch the character to convert
     * @return a String containing the one specified character
     */
    public static String toString(final char ch) {
        if (ch < 128) {
            return CHAR_STRING_ARRAY[ch];
        }
        return String.valueOf(ch);
    }

    /**
     * <p>Converts the string to the Unicode format '\u0020'.</p>
     *
     * <p>This format is the Java source code format.</p>
     *
     * <p>If {@code null} is passed in, {@code null} will be returned.</p>
     *
     * <pre>
     *   LiCharUtils.unicodeEscaped(null) = null
     *   LiCharUtils.unicodeEscaped(' ')  = "\u0020"
     *   LiCharUtils.unicodeEscaped('A')  = "\u0041"
     * </pre>
     *
     * @param ch the character to convert, may be null
     * @return the escaped Unicode string, null if null input
     */
    public static String unicodeEscaped(final Character ch) {
        if (ch == null) {
            return null;
        }
        return unicodeEscaped(ch.charValue());
    }

    //--------------------------------------------------------------------------

    /**
     * <p>Converts the string to the Unicode format '\u0020'.</p>
     *
     * <p>This format is the Java source code format.</p>
     *
     * <pre>
     *   LiCharUtils.unicodeEscaped(' ') = "\u0020"
     *   LiCharUtils.unicodeEscaped('A') = "\u0041"
     * </pre>
     *
     * @param ch the character to convert
     * @return the escaped Unicode string
     */
    public static String unicodeEscaped(final char ch) {
        return "\\u" +
                HEX_DIGITS[(ch >> 12) & 15] +
                HEX_DIGITS[(ch >> 8) & 15] +
                HEX_DIGITS[(ch >> 4) & 15] +
                HEX_DIGITS[(ch) & 15];
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit.</p>
     *
     * <pre>
     *   LiCharUtils.isAscii('a')  = true
     *   LiCharUtils.isAscii('A')  = true
     *   LiCharUtils.isAscii('3')  = true
     *   LiCharUtils.isAscii('-')  = true
     *   LiCharUtils.isAscii('\n') = true
     *   LiCharUtils.isAscii('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if less than 128
     */
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit printable.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiPrintable('a')  = true
     *   LiCharUtils.isAsciiPrintable('A')  = true
     *   LiCharUtils.isAsciiPrintable('3')  = true
     *   LiCharUtils.isAsciiPrintable('-')  = true
     *   LiCharUtils.isAsciiPrintable('\n') = false
     *   LiCharUtils.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit control.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiControl('a')  = false
     *   LiCharUtils.isAsciiControl('A')  = false
     *   LiCharUtils.isAsciiControl('3')  = false
     *   LiCharUtils.isAsciiControl('-')  = false
     *   LiCharUtils.isAsciiControl('\n') = true
     *   LiCharUtils.isAsciiControl('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if less than 32 or equals 127
     */
    public static boolean isAsciiControl(final char ch) {
        return ch < 32 || ch == 127;
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit numeric.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiAlphanumeric('a')  = true
     *   LiCharUtils.isAsciiAlphanumeric('A')  = true
     *   LiCharUtils.isAsciiAlphanumeric('3')  = true
     *   LiCharUtils.isAsciiAlphanumeric('-')  = false
     *   LiCharUtils.isAsciiAlphanumeric('\n') = false
     *   LiCharUtils.isAsciiAlphanumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlphanumeric(final char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit alphabetic.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiAlpha('a')  = true
     *   LiCharUtils.isAsciiAlpha('A')  = true
     *   LiCharUtils.isAsciiAlpha('3')  = false
     *   LiCharUtils.isAsciiAlpha('-')  = false
     *   LiCharUtils.isAsciiAlpha('\n') = false
     *   LiCharUtils.isAsciiAlpha('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlpha(final char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit alphabetic upper case.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiAlphaUpper('a')  = false
     *   LiCharUtils.isAsciiAlphaUpper('A')  = true
     *   LiCharUtils.isAsciiAlphaUpper('3')  = false
     *   LiCharUtils.isAsciiAlphaUpper('-')  = false
     *   LiCharUtils.isAsciiAlphaUpper('\n') = false
     *   LiCharUtils.isAsciiAlphaUpper('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 65 and 90 inclusive
     */
    public static boolean isAsciiAlphaUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit alphabetic lower case.</p>
     *
     * <pre>
     *   LiCharUtils.isAsciiAlphaLower('a')  = true
     *   LiCharUtils.isAsciiAlphaLower('A')  = false
     *   LiCharUtils.isAsciiAlphaLower('3')  = false
     *   LiCharUtils.isAsciiAlphaLower('-')  = false
     *   LiCharUtils.isAsciiAlphaLower('\n') = false
     *   LiCharUtils.isAsciiAlphaLower('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 97 and 122 inclusive
     */
    public static boolean isAsciiAlphaLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * <p>Compares two {@code char} values numerically. This is the same functionality as provided in Java 7.</p>
     *
     * @param x the first {@code char} to compare
     * @param y the second {@code char} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 3.4
     */
    public static int compare(final char x, final char y) {
        return x - y;
    }

    /**
     * 获取给定字符的16进制数值
     *
     * @param b 字符
     * @return 16进制字符
     * @since 5.3.1
     */
    public static int digit16(int b) {
        return Character.digit(b, 16);
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param c 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     * @since 4.1.11
     */
    public static boolean isFileSeparator(char c) {
        return SLASH == c || BACKSLASH == c;
    }
}
