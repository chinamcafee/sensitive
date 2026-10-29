package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.util.InnerCharUtil;

/**
 * 不做任何处理的替换策略
 *
 * @author d
 * @since 1.0.0
 */
public class NoneCharsReplace extends AbstractCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.ANY_NONE.getScanType();
    }

    @Override
    public void doReplace(StringBuilder stringBuilder, char[] chars,
                          CharsScanMatchItem charsScanMatchItem,
                          final CharsScanContext context) {
        InnerCharUtil.appendChars(stringBuilder, chars, charsScanMatchItem.getStartIndex(), charsScanMatchItem.getEndIndex());
    }

}
