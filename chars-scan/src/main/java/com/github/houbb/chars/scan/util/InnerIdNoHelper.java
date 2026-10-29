package com.github.houbb.chars.scan.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 地区编码
 * @since 1.19.0
 */
public class InnerIdNoHelper {

    /**
     * 省份编码
     */
    private static final String PROVINCE_CODES = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65";

    /**
     * 节点数
     */
    private static final Map<Character, Set<Character>> AREA_MAP = new HashMap<Character, Set<Character>>();

    /**
     * 校验码权重
     */
    private static final int[] CHECK_WEIGHT = new int[]{
            7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2
    };

    /**
     * 权重码映射
     */
    private static final char[] CHECK_MAPPING = new char[]{
            '1','0','X','9','8','7','6','5','4','3','2'
    };

    static {
        // 初始化
        String[] codes = PROVINCE_CODES.split(",");
        for(String code : codes) {
            char firstChar = code.charAt(0);
            char secondChar = code.charAt(1);

            Set<Character> characters = AREA_MAP.get(firstChar);
            if(characters == null) {
                characters = new HashSet<Character>();
            }
            characters.add(secondChar);

            AREA_MAP.put(firstChar, characters);
        }
    }

    /**
     * 是否为合法身份证
     * @param buffer 缓存
     * @return 结果
     */
    public static boolean isValidIdNo(StringBuilder buffer) {
        int len = buffer.length();
        if(len != 18) {
            return false;
        }

        if(!isValidAreaCode(buffer)) {
            return false;
        }

        // 第7位开始是日期
        if(!InnerCharUtil.isValidDate(buffer, 6)) {
            return false;
        }

        // check
        char lastChar = buffer.charAt(17);
        if(lastChar == 'x') {
            lastChar = 'X';
        }
        char check = getCheckDigit(buffer);
        return check == lastChar;
    }

    /**
     * 是否为合法地区
     * @param stringBuilder buffer
     * @return 结果
     */
    public static boolean isValidAreaCode(StringBuilder stringBuilder) {
        Set<Character> characters = AREA_MAP.get(stringBuilder.charAt(0));
        if (characters == null) {
            return false;
        }

        return characters.contains(stringBuilder.charAt(1));
    }


    /**
     * 获取校验码
     * 1. 将前面的身份证号码17位数分别乘以不同的系数。
     * 2. 将这17位数字和系数相乘的结果相加；用加出来和除以11，看余数是多少
     * 3. 返回映射数字
     *
     * @param stringBuilder 身份证（前17位即可）
     * @return 结果
     */
    public static char getCheckDigit(StringBuilder stringBuilder) {
        int sum = 0;
        for(int i = 0; i < 17; i++) {
            int weight = CHECK_WEIGHT[i] * InnerCharUtil.getCharInt(stringBuilder.charAt(i));
            sum += weight;
        }

        int value = sum % 11;
        return CHECK_MAPPING[value];
    }

}
