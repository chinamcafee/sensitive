package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.heaven.util.lang.StringUtil;

import java.util.List;

/**
 * GPS
 *
 * TODO: GPS 的实现需要调整下，不然会与普通的金额混乱。
 *
 * 这里有些公司的金额会乘以多少，避免出现 .
 * yyyyMMdd
 * @author d
 * @since 1.0.0
 */
public class GPSConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c) || '.' == c;
    }

    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        //0-180.6位数字
        int bufferLen = super.getBuffer().length();
        if(bufferLen < 8 || bufferLen > 10) {
            return false;
        }

        String string = super.getBuffer().toString();

        List<String> stringList = StringUtil.splitToList(string, '.');
        if(stringList.size() != 2) {
            return false;
        }

        // 额外处理
        //前面0-180， 后面
        int firstNum = InnerCharUtil.parseInt(stringList.get(0));
        if(firstNum < 0 || firstNum > 180) {
            return false;
        }

        return true;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.GPS.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.GPS.getPriority();
    }

}
