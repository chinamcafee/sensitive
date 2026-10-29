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
 * @since 1.0.0
 */
public class ChineseNameConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isChinese(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        // 长度
        StringBuilder buffer = super.getBuffer();
        int bufferLen = buffer.length();
        // 中国人名 [2,3]，不考虑新疆等地区
        // TODO: 后续考虑改为 2-3
        if(bufferLen < 2 || bufferLen > 3) {
            return false;
        }

        //TODO: 姓氏判断，降低误判率
        char firstChar = buffer.charAt(0);
        // 姓氏判断
        if(!InnerChineseNameUtil.isFamilyName(firstChar)) {
            return false;
        }

        return true;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.CHINESE_NAME.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.CHINESE_NAME.getPriority();
    }

}
