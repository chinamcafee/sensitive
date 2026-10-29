package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * expand 地址
 *
 * 核心思想：两边 expand，减少一次 keyword 的遍历判断
 *
 * @author d
 * @since 1.24.0
 */
public class AddressExpandConditionCharScan extends AbstractExpandConditionCharScan {

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
    public int getPriority() {
        return CharsScanTypeEnum.ADDRESS.getPriority();
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.ADDRESS.getScanType();
    }

    @Override
    protected CharsScanTypeEnum isExpandStartCharCondition(int i, char c, char[] chars, CharsScanContext context) {
        if(PLACE_CHAR_SET.contains(c)) {
            return CharsScanTypeEnum.ADDRESS;
        }

        return null;
    }

    @Override
    protected boolean isLeftExpandMatchCondition(int leftIx, char leftChar, char[] chars, CharsScanContext context) {
        // 第一个从中文开始，以满足在严格模式中 MM上海市 这种非标准的写法
        if(leftIx == 0) {
            return CharUtil.isChinese(leftChar);
        }

        // 上海市徐汇区888号A座-2楼
        return CharUtil.isChinese(leftChar) || CharUtil.isDigitOrLetter(leftChar) || '-' == leftChar;
    }

    @Override
    protected boolean isRightExpandMatchCondition(int rightIx, char rightChar, char[] chars, CharsScanContext context) {
        return CharUtil.isChinese(rightChar) || CharUtil.isDigitOrLetter(rightChar) || '-' == rightChar;
    }

    @Override
    protected boolean isLeftBufferMatch(int middleIx, int startIx, char[] chars, CharsScanContext context) {
        int len = middleIx - startIx;
        // 必须是6位以上，避免和中文名称混淆
        // 及时截断，避免太长
        if(len < 1 || len > 100) {
            return false;
        }

        return true;
    }

    @Override
    protected boolean isRightBufferMatch(int middleIx, int startIx, int endIx, char[] chars, CharsScanContext context) {
        int len = endIx - startIx;
        if(len < 6 || len > 100) {
            return false;
        }

        return true;
    }


}
