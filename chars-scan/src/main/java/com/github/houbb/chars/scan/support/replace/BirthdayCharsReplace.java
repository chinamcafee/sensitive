package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;


/**
 * 生日替换策略
 *
 * TODO: 底层全掩盖，其实没啥意义。
 *
 * @author d
 * @since 0.8.0
 */
public class BirthdayCharsReplace extends AbstractRangeCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.BIRTHDAY.getScanType();
    }

    @Override
    protected int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getStartIndex();
    }

    @Override
    protected int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getEndIndex();
    }

}
