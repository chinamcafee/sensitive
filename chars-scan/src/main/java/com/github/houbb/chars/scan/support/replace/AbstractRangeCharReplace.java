package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;

/**
 * 抽象实现-范围替换策略
 *
 * @since 0.3.0
 */
public abstract class AbstractRangeCharReplace extends AbstractCharReplace {

    /**
     * 获取掩码的开始位置
     * @param chars 原始数组
     * @param itemLen 元素长度
     * @param charsScanMatchItem 匹配元素
     * @return 结果
     */
    protected abstract int getMaskStartIndex(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem);

    /**
     * 获取掩码的开始位置
     * @param chars 数组
     * @param itemLen 元素长度
     * @param charsScanMatchItem 匹配元素
     * @return 结果
     */
    protected abstract int getMaskStartEnd(char[] chars, int itemLen, CharsScanMatchItem charsScanMatchItem);

    /**
     * 执行替换
     * @param stringBuilder buffer
     * @param chars 字符数组
     * @param charsScanMatchItem 扫描元素
     * @param context 上下文
     * @since 0.3.0
     */
    public void doReplace(StringBuilder stringBuilder,
                          char[] chars,
                          CharsScanMatchItem charsScanMatchItem,
                          final CharsScanContext context) {
        int itemLen = charsScanMatchItem.getLength();

        //TODO: 这里要保证子类实现范围别乱写，此处不做额外校验。
        int maskStartIndex = getMaskStartIndex(chars, itemLen, charsScanMatchItem);
        int maskEndIndex = getMaskStartEnd(chars, itemLen, charsScanMatchItem);

        // 范围校验
        super.replaceByIndex(stringBuilder, chars, charsScanMatchItem, context, maskStartIndex, maskEndIndex);
    }

}
