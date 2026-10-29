package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.util.ArrayUtil;


/**
 * 身份证替换策略
 *
 * @author d
 * @since 1.0.0
 */
public class EmailCharsReplace extends AbstractCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.EMAIL.getScanType();
    }

    @Override
    public void doReplace(StringBuilder stringBuilder, char[] chars, CharsScanMatchItem charsScanMatchItem, CharsScanContext context) {
        int lastAtIx = ArrayUtil.lastIndexOf(chars, '@', charsScanMatchItem.getStartIndex(), charsScanMatchItem.getEndIndex());

        int prefixLen = lastAtIx - charsScanMatchItem.getStartIndex();
        // 保留前4位，这里的 AT 判断有2次，不够优雅。
        int maskStartIx  = charsScanMatchItem.getStartIndex()+Math.min(4, prefixLen);
        //AT 之前
        int maskEndIx = lastAtIx - 1;

        // 构建
        super.replaceByIndex(stringBuilder, chars, charsScanMatchItem, context, maskStartIx, maskEndIx);
    }

}
