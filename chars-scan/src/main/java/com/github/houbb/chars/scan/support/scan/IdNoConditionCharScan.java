package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerIdNoHelper;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 身份证
 *
 *
 * @author d
 * @since 1.0.0
 */
public class IdNoConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        // 最后一位可以返回 x
        if(super.getBuffer().length() == 17) {
            return CharUtil.isNumber(c) || 'X' == c;
        }

        return CharUtil.isNumber(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        return InnerIdNoHelper.isValidIdNo(super.getBuffer());
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.ID_NO.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.ID_NO.getPriority();
    }

}
