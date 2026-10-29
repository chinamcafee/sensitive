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
 * yyyyMMdd
 * @author d
 * @since 1.0.0
 */
public class IPV4ConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c) || '.' == c;
    }

    //0-255
    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        int bufferLen = super.getBuffer().length();
        //0.0.0.0
        //255.255.255.255
        if(bufferLen < 7
            || bufferLen > 15) {
            return false;
        }

        // 尽可能减少对象的创建
        String string = super.getBuffer().toString();
        List<String> stringList = StringUtil.splitToList(string, '.');
        if(stringList.size() != 4) {
            return false;
        }

        for(String numStr : stringList) {
            int integer = InnerCharUtil.parseInt(numStr);

            if(integer < 0 || integer > 256) {
                return false;
            }
        }

        // 额外处理
        return true;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.IPV4.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.IPV4.getPriority();
    }

}
