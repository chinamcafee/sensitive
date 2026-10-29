package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerBufferUtil;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 手机
 *
 * TODO: xxx-xxx
 *
 * @author d
 * @since 1.0.0
 */
public class PhoneConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        StringBuilder buffer = super.getBuffer();
        int bufferLen = buffer.length();

        if(bufferLen != 11) {
            return false;
        }

        return InnerBufferUtil.isPhone(buffer);
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PHONE.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.PHONE.getPriority();
    }

}
