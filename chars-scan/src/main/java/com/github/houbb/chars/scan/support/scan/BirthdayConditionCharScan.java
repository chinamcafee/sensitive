package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 生日
 *
 * 个人感觉生日没必要，因为时间在日志中太常见，太重要，容易混淆。
 *
 * yyyyMMdd
 * @author d
 * @since 1.0.0
 */
public class BirthdayConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        StringBuilder buffer = super.getBuffer();
        int bufferLen = buffer.length();
        if(bufferLen != 8) {
            return false;
        }

        //19900101
        return InnerCharUtil.isValidDate(buffer, 0);
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.BIRTHDAY.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.BIRTHDAY.getPriority();
    }

}
