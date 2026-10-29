package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.chars.scan.constant.CharsScanConst;
import com.github.houbb.chars.scan.util.InnerStringCoding;

import java.nio.charset.Charset;

/**
 * 抽象实现
 */
public abstract class AbstractCharReplace implements ICharsReplace {

    /**
     * 执行替换
     * @param stringBuilder buffer
     * @param chars 字符数组
     * @param charsScanMatchItem 扫描元素
     * @param context 上下文
     * @since 0.3.0
     */
    public abstract void doReplace(StringBuilder stringBuilder,
                                   char[] chars,
                                   CharsScanMatchItem charsScanMatchItem,
                                   final CharsScanContext context);

    @Override
    public void replace(StringBuilder stringBuilder,
                        char[] chars,
                        CharsScanMatchItem charsScanMatchItem,
                        final CharsScanContext context) {
        //1. 如果不用替换，则题解处理以前的。
        if(ignoreReplace(chars, charsScanMatchItem)) {
            //1.1 不用做什么，直接添加原始的部分
            InnerCharUtil.appendChars(stringBuilder, chars, charsScanMatchItem.getStartIndex(), charsScanMatchItem.getEndIndex());
            return;
        }

        //2. 正常的替换策略
        this.doReplace(stringBuilder, chars, charsScanMatchItem, context);

        //3. 添加对应的 hash 信息
        byte[] bytes = InnerStringCoding.encode(Charset.forName("UTF-8"),
                chars,
                charsScanMatchItem.getStartIndex(), charsScanMatchItem.getLength());
        context.getCharsReplaceHash().hash(bytes, context, stringBuilder);
    }

    /**
     * 如果包含了 * ，则直接忽略。
     *
     * 这里实际上是 O(n) 的遍历，contains 也是一样。
     * @param chars 数组
     * @param charsScanMatchItem 匹配标识
     * @return 结果
     */
    protected boolean ignoreReplace(char[] chars, CharsScanMatchItem charsScanMatchItem) {
        return false;
    }

    protected void replaceByIndex(StringBuilder stringBuilder,
                                  char[] chars,
                                  CharsScanMatchItem charsScanMatchItem,
                                  final CharsScanContext context,
                                  int maskStartIndex,
                                  int maskEndIndex) {
        // 开始位置
        int startIndex = charsScanMatchItem.getStartIndex();
        int endIndex = charsScanMatchItem.getEndIndex();

        // 范围校验
//        maskStartIndex = Math.max(maskStartIndex, startIndex);
//        maskEndIndex = Math.min(maskEndIndex, endIndex);

        // 处理的结果
        // [原始部分~掩码开始)
        // [掩码开始~掩码结束]
        // (掩码结束~原始结束]
        if(maskStartIndex > startIndex) {
            InnerCharUtil.appendChars(stringBuilder, chars, startIndex, maskStartIndex-1);
        }
        for(int i = maskStartIndex; i <= maskEndIndex; i++) {
            stringBuilder.append(CharsScanConst.REPLACE_CHAR);
        }
        if(maskEndIndex < endIndex) {
            InnerCharUtil.appendChars(stringBuilder, chars, maskEndIndex+1, endIndex);
        }
    }

}
