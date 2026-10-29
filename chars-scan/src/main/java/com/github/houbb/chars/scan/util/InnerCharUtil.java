package com.github.houbb.chars.scan.util;

import com.github.houbb.chars.scan.constant.CharsScanConfigConst;
import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.heaven.util.lang.StringUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InnerCharUtil {

    /**
     * 用 set 把匹配变成 O(1)
     */
    private static final Set<Character> PLACE_CHAR_SET;

    static {
        String keywords = "国省市县区路镇乡村街道园院里厂弄";
        PLACE_CHAR_SET = new HashSet<Character>(keywords.length());
        for(char c : keywords.toCharArray()) {
            PLACE_CHAR_SET.add(c);
        }
    }

    /**
     * 是否为地址关键词
     * @param c 字符
     * @return 结果
     */
    public static boolean isAddressKeyword(char c) {
        return PLACE_CHAR_SET.contains(c);
    }

    /**
     * 默认的前缀集合
     * @since 1.17.0
     */
    public static final Set<Character> DEFAULT_PREFIX_SET = getCharSet(CharsScanConfigConst.DEFAULT_PREFIX);

    /**
     * 是否为合法的日期
     * @param buffer buffer
     * @param startIx 开始下标
     * @return 结果
     * @since 1.18.0
     */
    public static boolean isValidDate(StringBuilder buffer,
                                      int startIx) {
        //19900101
        int year = 1000 * InnerCharUtil.getCharInt(buffer.charAt(startIx))
                + 100 * InnerCharUtil.getCharInt(buffer.charAt(1+startIx))
                + 10 * InnerCharUtil.getCharInt(buffer.charAt(2+startIx))
                + InnerCharUtil.getCharInt(buffer.charAt(3+startIx));
        if(year < 1900 || year > 2099) {
            return false;
        }

        int month = 10 * InnerCharUtil.getCharInt(buffer.charAt(4+startIx)) + InnerCharUtil.getCharInt(buffer.charAt(5+startIx));
        if(month < 1 || month > 12) {
            return false;
        }

        int day = 10 * InnerCharUtil.getCharInt(buffer.charAt(6+startIx)) + InnerCharUtil.getCharInt(buffer.charAt(7+startIx));
        if(day < 1 || day > 31) {
            return false;
        }

        return true;
    }

    /**
     * 转换为整数
     * @param text 文本
     * @return 整数
     * @since 1.18.0
     */
    public static int parseInt(String text) {
        int len = text.length();

        int sum = 0;

        int weight = 1;
        char[] chars = text.toCharArray();
        for(int i = len-1; i >= 0; i--) {
            int val = getCharInt(chars[i]);

            sum += weight * val;

            weight *= 10;
        }
        return sum;
    }

    /**
     * 获取 int char 对应的真实值
     * @param c 字符
     * @return 结果
     * @since 1.18.0
     */
    public static int getCharInt(final char c) {
        return c - '0';
    }

    /**
     * 获取对应的集合
     * @param text 文本
     * @return 结果
     * @since 1.17.0
     */
    @CommonEager
    public static Set<Character> getCharSet(final String text) {
        if(StringUtil.isEmpty(text)) {
            return Collections.emptySet();
        }

        char[] chars = text.toCharArray();
        Set<Character> characterSet = new HashSet<Character>(chars.length);
        for(int i = 0; i < chars.length; i++) {
            characterSet.add(chars[i]);
        }

        return characterSet;
    }
    /**
     * 添加指定范围内的 char
     *
     * 注意：这里不做各种下标判空等校验，提升性能。在使用前校验号
     * @param stringBuilder 缓存
     * @param chars 原始数组
     * @param startIndex 开始位置
     * @param endIndex 完结位置
     * @since 0.3.0
     */
    public static void appendChars(StringBuilder stringBuilder,
                                   char[] chars,
                                   int startIndex,
                                   int endIndex) {
        int len = endIndex-startIndex + 1;
        stringBuilder.append(chars, startIndex, len);
    }

    /**
     * 是否为地址
     * @param c 字符
     * @return 结果
     * @since 1.26.0
     */
    public static boolean isAddressChar(char c) {
        // 上海市徐汇区888号A座-2楼
        return CharUtil.isChinese(c) || CharUtil.isDigitOrLetter(c) || '-' == c;
    }

    private static boolean isDigitOrLetter(char c) {
        return Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c);
    }

    public static boolean isEmilChar(char c) {
        return isDigitOrLetter(c) || '_' == c || '-' == c || c == '.';
    }

    /**
     * 默认的前缀集合
     * @since 1.30.0
     */
    public static final Set<Character> DEFAULT_ESCAPE_PREFIX_SET = getCharSet(CharsScanConfigConst.DEFAULT_ESCAPE_PREFIX);

    /**
     * 获取对应的转义符号
     * @param text 文本
     * @return 结果
     * @since 1.30.0
     */
    @CommonEager
    public static Set<Character> getEscapeCharSet(final String text) {
        if(StringUtil.isEmpty(text)) {
            return Collections.emptySet();
        }

        char[] chars = text.toCharArray();
        Set<Character> resultSet = new HashSet<Character>();

        for(char c: chars) {
            char escapeChar = getEscapedChar(c);
            resultSet.add(escapeChar);
        }

        return resultSet;
    }

    @CommonEager
    public static char getEscapedChar(char c) {
        switch (c) {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case 'r':
                return '\r';
            // 可以添加其他字符的处理
            default:
                // 如果没有匹配的特殊字符，直接返回原字符
                return c;
        }
    }

}
