package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 地址
 *
 * @author d
 * @since 0.8.0
 */
public class AddressConditionCharScan extends AbstractConditionCharScan {

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

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        // 第一个从中文开始，以满足在严格模式中 MM上海市 这种非标准的写法
        if(super.getBuffer().length() == 0) {
            return CharUtil.isChinese(c);
        }

        // 上海市徐汇区888号A座-2楼
        return CharUtil.isChinese(c) || CharUtil.isDigitOrLetter(c) || '-' == c;
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        int bufferLen = super.getBuffer().length();
        // 必须是6位以上，避免和中文名称混淆
        // 及时截断，避免太长
        if(bufferLen < 6 || bufferLen > 100) {
            return false;
        }

        if(containsKeyword()) {
            return true;
        }

        return false;
    }

    /**
     * 是否包含地区关键词
     * @return 关键词
     */
    private boolean containsKeyword() {
        // 当前的 buffer
        StringBuilder builder = super.getBuffer();
        int bufferLen = builder.length();

        int maxLen = Math.min(50, bufferLen);

        for(int j = 0; j < maxLen; j++) {
            char bc = builder.charAt(j);
            if(PLACE_CHAR_SET.contains(bc)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.ADDRESS.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.ADDRESS.getPriority();
    }

}
