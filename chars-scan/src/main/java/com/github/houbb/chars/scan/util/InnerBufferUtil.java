package com.github.houbb.chars.scan.util;

/**
 *
 * PS: 这里不做判断，进入之前外部判断长度。减少一次判断
 *
 * @since 1.25.0
 */
public class InnerBufferUtil {



    /**
     * 是否为地址
     * @param buffer buffer
     * @return 结果
     */
    public static boolean isAddress(StringBuilder buffer) {
        return containsKeyword(buffer);
    }

    /**
     * 是否包含地区关键词
     * @return 关键词
     */
    private static boolean containsKeyword(StringBuilder builder) {
        int bufferLen = builder.length();

        int maxLen = Math.min(50, bufferLen);

        for(int j = 0; j < maxLen; j++) {
            char bc = builder.charAt(j);
            if(InnerCharUtil.isAddressKeyword(bc)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 是否为中文名称
     * @param buffer 缓存
     * @return 结果
     * @since 1.26.0
     */
    public static boolean isChineseName(StringBuilder buffer) {
        //TODO: 姓氏判断，降低误判率
        char firstChar = buffer.charAt(0);
        // 姓氏判断
        if(!InnerChineseNameUtil.isFamilyName(firstChar)) {
            return false;
        }

        return true;
    }


    /**
     * 是否为手机
     * @param stringBuilder buffer
     * @return 结果
     */
    public static boolean isPhone(StringBuilder stringBuilder) {
        return '1' == stringBuilder.charAt(0);
    }

}
