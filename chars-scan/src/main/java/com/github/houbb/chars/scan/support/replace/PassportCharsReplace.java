package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;

/**
 * 护照
 * @since 1.12.0
 */
public class PassportCharsReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.PASSPORT.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getStartIndex() + 2;
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getEndIndex()-2;
    }
}
