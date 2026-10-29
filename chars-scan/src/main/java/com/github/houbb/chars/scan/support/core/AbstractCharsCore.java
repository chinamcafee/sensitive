package com.github.houbb.chars.scan.support.core;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.List;

/**
 * @author d
 * @since 1.0.0
 */
public abstract class AbstractCharsCore implements ICharsCore {

    /**
     * 扫描
     * @param originalString 原始字符串
     * @param context 上下文
     * @param oldChars 原始
     * @return 结果
     */
    public abstract List<CharsScanMatchItem> scan(final String originalString, final CharsScanContext context,
                                                  final char[] oldChars);

    /**
     * 替换
     * @param charsScanMatchItemList 扫描列表
     * @param originalString 原始字符串
     * @param context 上下文
     * @param oldChars 原始
     * @return 结果
     */
    public abstract String replace(final List<CharsScanMatchItem> charsScanMatchItemList, final String originalString, final CharsScanContext context,
                                   final char[] oldChars);

    @Override
    public String scanAndReplace(String originalString, CharsScanContext context) {
        try {
            final ICharsScanFactory charScanFactory = context.getCharScanFactory();
            List<String> charsScanTypeList = charScanFactory.scanTypeList();
            if(StringUtil.isEmpty(originalString)
                    || CollectionUtil.isEmpty(charsScanTypeList)) {
                return originalString;
            }

            // 扫描
            final char[] oldChars = originalString.toCharArray();
            List<CharsScanMatchItem> charsScanMatchItemList = this.scan(originalString, context, oldChars);
            if(CollectionUtil.isEmpty(charsScanMatchItemList)) {
                return originalString;
            }

            // 替换
            String result = this.replace(charsScanMatchItemList, originalString, context, oldChars);

            // 其他

            return result;
        } finally {
            // 最后清空当前线程
        }
    }

}
