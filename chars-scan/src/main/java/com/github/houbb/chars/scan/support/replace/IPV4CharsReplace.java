package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;


/**
 * 替换策略
 *
 * @author d
 * @since 1.0.0
 */
public class IPV4CharsReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.IPV4.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getEndIndex() - 5;
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        // 固定掩盖后面6位
        return charsScanMatchItem.getEndIndex();
    }

}
