package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerChineseNameUtil;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 中文人名
 *
 * TODO: 可以引入百家姓，甚至 NLP-人名。
 *
 * @author d
 * @since 1.26.0
 */
public class ChineseNameExpandConditionCharScan extends AbstractExpandConditionCharScan {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.CHINESE_NAME.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.CHINESE_NAME.getPriority();
    }

    @Override
    protected CharsScanTypeEnum isExpandStartCharCondition(int i, char c, char[] chars, CharsScanContext context) {
        // 姓氏
        if(InnerChineseNameUtil.isFamilyName(c)) {
            return CharsScanTypeEnum.CHINESE_NAME;
        }
        return null;
    }

    @Override
    protected boolean isLeftExpandMatchCondition(int leftIx, char leftChar, char[] chars, CharsScanContext context) {
        // 左边不做拓展
        return CharUtil.isChinese(leftChar);
    }

    @Override
    protected boolean isRightExpandMatchCondition(int rightIx, char rightChar, char[] chars, CharsScanContext context) {
        return CharUtil.isChinese(rightChar);
    }

    @Override
    protected boolean isLeftBufferMatch(int middleIx, int startIx, char[] chars, CharsScanContext context) {
        // 左边不允许有元素
        return startIx == middleIx;
    }

    @Override
    protected boolean isRightBufferMatch(int middleIx, int startIx, int endIx, char[] chars, CharsScanContext context) {
        int bufferLen = endIx - startIx;
        // 长度为
        if(bufferLen < 1 || bufferLen > 2) {
            return false;
        }

        return true;
    }

}
