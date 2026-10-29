package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.lang.CharUtil;

/**
 * 护照
 *
 * 按照ICAO要求，全世界护照的号码均为9位拉丁字母或阿拉伯数字。
 * 中国普通护照 (电子护照)E开头+8位数字 或 E+字母+7位数字 (E12345678/ED1234567)
 * (97-2版) G开头+8位数字 (G12345678)
 * 中国香港护照(2019年5月起) H开头+8位数字(32页版本)(H12345678) HJ+7位数字 (48页版本)(HJ1234567)
 * (电子护照，2019年5月前)K开头+8位数字(32页版本)(K12345678) KJ+7位数字 (48页版本)(KJ1234567)
 * 中国澳门护照 (2019年12月起) MB+7位数字(MB1234567)
 * (2019年12月前) MA+7位数字(MA1234567)
 * 中华民国护照 9位数字，无字母 (123456789)
 * 新加坡护照 第一位字母+7位数字+最后一位字母 (P1234567S)
 * 韩国护照 PM+7位数字 (PM1234567)
 * 日本护照 M/T+一位字母+7位数字 (MA1234567/TA1234567)
 * 美国护照 9位数字，无字母 (123456789)
 * 英国护照 9位数字，无字母(123456789)
 * 加拿大护照 两位字母+7位数字 (CA1234567)
 * 澳大利亚护照 字母+8位数字 (E12345678)
 *
 * @author d
 * @since 1.12.0
 */
public class PassportConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c) || CharUtil.isEnglish(c);
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        //必须为9位
        int bufferLen = super.getBuffer().length();
        if(bufferLen != 9) {
            return false;
        }

        //第一个必须是字母
        int startIx = i - bufferLen;
        if(!CharUtil.isEnglish(chars[startIx])) {
            return false;
        }

        //2. 2-最后必须是数字
        for(int j = startIx+2; j < i; j++) {
            if(CharUtil.isNotNumber(chars[j])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PASSPORT.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.PASSPORT.getPriority();
    }

}
