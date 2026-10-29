package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;


/**
 * 中国人名替换策略
 *
 * @author d
 * @since 1.0.0
 */
public class ChineseNameCharsReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.CHINESE_NAME.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getStartIndex() + itemLen/2;
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getEndIndex();
    }

}
