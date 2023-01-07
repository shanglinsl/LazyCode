package lazycode;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.interfaces.PBEKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LzString {

    /**
     * 系统行分割符
     *
     * @return
     */
    public static String systemLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static boolean stripIsEmpty(String str, String stripChars) {
        return str != null && StringUtils.strip(str, stripChars).isEmpty();
    }

    public static boolean stripIsEmpty(String str) {
        // " \r\n  " => true
        return stripIsEmpty(str, null);
    }


    public static String fillCenter(String str, int size, String fillStr) {
        // "abc" 5 "*" => "*abc*"
        return StringUtils.center(str, size, fillStr);
    }

    public static String fillCenter(String str, int size) {
        return fillCenter(str, size, " ");
    }

    public static String fillLeft(String str, int size, String fillStr) {
        // "abc" 5 "*"  => "**abc"
        return StringUtils.leftPad(str, size, fillStr);
    }

    public static String fillLeft(String str, int size) {
        return fillLeft(str, size, " ");
    }

    public static String fillRight(String str, int size, String fillStr) {
        // "abc" 5 "*"  => "abc**"
        return StringUtils.rightPad(str, size, fillStr);
    }

    public static String fillRight(String str, int size) {
        return fillRight(str, size, " ");
    }

    public static String capitalize(String str) {
        // cat => Cat
        return StringUtils.capitalize(str);
    }

    public static String unCapitalize(String str) {
        // Cat => cat
        return StringUtils.uncapitalize(str);
    }

    public static boolean contains(String str, String searchStr) {
        // "abc"  "ab" => true
        return StringUtils.contains(str, searchStr);
    }

    public static int countMatches(String str, String subStr) {
        return StringUtils.countMatches(str, subStr);
    }

    public static boolean startWithAny(String str, String[] strArray) {
        return StringUtils.startsWithAny(str, strArray);
    }

    public static boolean endWithAny(String str, String[] strArray) {
        return StringUtils.endsWithAny(str, strArray);
    }

    public static String commonPrefix(String... strings) {
        // "abcd", "abc", "ab" => "ab"
        return StringUtils.getCommonPrefix(strings);
    }

    public static int firstIndexOfN(String str, String searchStr, int order) {
        return StringUtils.ordinalIndexOf(str, searchStr, order);
    }

    public static int lastIndexOfN(String str, String searchStr, int order) {
        return StringUtils.lastOrdinalIndexOf(str, searchStr, order);
    }

    public static boolean isAllUpperCase(String str) {
        return StringUtils.isAllUpperCase(str);
    }

    public static boolean isAllLowerCase(String str) {
        return StringUtils.isAllLowerCase(str);
    }

    public static String replaceEach(String str, String[]... strings) {
        // "aabcbd", new String[][]{{"a", "*"}, {"b", "$"}}  => **$c$d
        for (String[] array : strings) {
            str = str.replace(array[0], array[1]);
        }
        return str;
    }

    public static String replaceEachReg(String str, String[]... strings) {
        for (String[] array : strings) {
            str = str.replaceAll(array[0], array[1]);
        }
        return str;
    }

    public static boolean isNumber(String str) {
        return isNumberInt(str) || isNumberDouble(str);
    }

    public static boolean isNumberInt(String str) {
        return StringUtils.isNumeric(str);
    }

    public static boolean isNumberDouble(String str) {
        return str.matches("\\d+\\.\\d+");
    }

    public static String repeat(String str, int times) {
        return StringUtils.repeat(str, times);
    }

    public static String reverse(String str) {
        return StringUtils.reverse(str);
    }

    public static String reverse(String str, int start, int end) {
        // "0123456", 1, 4 => "0321456"
        String prefix = str.substring(0, start);
        String suffix = str.substring(end, str.length());
        return prefix + reverse(str.substring(start, end)) + suffix;
    }

    public static String join(Object... objects) {
        return StringUtils.join(objects);
    }

    public static String joinWith(String separator, Object... objects) {
        return StringUtils.joinWith(separator, objects);
    }

}
