package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerBankLuhnHelper;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 银行卡
 *
 * @author d
 * @since 1.0.0
 */
public class BankCardConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        int bufferLen = super.getBuffer().length();

        // 主流卡是 15-19
        if(bufferLen < 15 || bufferLen > 19) {
            return false;
        }

        //TODO: 添加银行卡的计算规则
        return InnerBankLuhnHelper.isValidBankNo(super.getBuffer());
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.BANK_CARD.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.BANK_CARD.getPriority();
    }

}
