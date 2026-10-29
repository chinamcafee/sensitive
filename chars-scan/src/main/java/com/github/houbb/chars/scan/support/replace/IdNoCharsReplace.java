package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;


/**
 * 身份证替换策略
 *
 * @author d
 * @since 1.0.0
 */
public class IdNoCharsReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.ID_NO.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getStartIndex()+1;
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getEndIndex() -1;
    }

}
