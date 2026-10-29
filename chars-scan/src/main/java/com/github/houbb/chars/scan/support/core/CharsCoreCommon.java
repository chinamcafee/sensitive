package com.github.houbb.chars.scan.support.core;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.chars.scan.support.scan.compare.CharsScanItemComparator;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.*;

/**
 * 核心实现-原始的实现
 *
 * 这里的 scanList 不是线程安全的，需要每一次的入参都要是新的。
 *
 * @author d
 * @since 1.13.0
 */
public class CharsCoreCommon extends AbstractCharsCore {

    @Override
    public List<CharsScanMatchItem> scan(final String originalText, final CharsScanContext context,
                                         final char[] chars) {
        try {
            // 基本属性
            final List<ICharsScan> charsScanList = this.getCharScanList(context);
            return this.doScan(chars, charsScanList, context);
        } finally {
            finallyCallBack(context);
        }
    }

    protected List<ICharsScan> getCharScanList(CharsScanContext context) {
        final ICharsScanFactory charScanFactory = context.getCharScanFactory();

        return charScanFactory.allCharScanList();
    }

    /**
     * finally 回調信息
     *
     * 可以用来执行清空等操作。
     *
     * @param context 上下文
     * @since 1.13.0
     */
    protected void finallyCallBack(CharsScanContext context) {
        //do nothing
    }

    /**
     * 执行扫描
     * @param chars 字符数组
     * @param charsScanList 扫描列表
     * @param context 上下文
     * @return 结果
     */
    protected List<CharsScanMatchItem> doScan(final char[] chars,
                                              final List<ICharsScan> charsScanList,
                                              final CharsScanContext context) {
        final int charsScanSize = charsScanList.size();
        final int scanStartIndex = context.scanStartIndex();

        for(int i = scanStartIndex; i < chars.length; i++) {
            char currentChar = chars[i];

            // 扫描
            for(int j = 0; j < charsScanSize; j++) {
                ICharsScan charsScan = charsScanList.get(j);
                charsScan.scan(i, currentChar, chars, context);
            }
        }

        // 结果
        //TODO: 这样处理的好处是，把扫描的结果单独处理出来，便于调试+维护。
        List<CharsScanMatchItem> resultList = new ArrayList<CharsScanMatchItem>();
        for(int j = 0; j < charsScanSize; j++) {
            // 需要判空
            ICharsScan charsScan = charsScanList.get(j);
            List<CharsScanMatchItem> itemList = charsScan.getMatchList();
            if(CollectionUtil.isNotEmpty(itemList)) {
                resultList.addAll(itemList);
            }
        }

        return resultList;
    }

    @Override
    public String replace(final List<CharsScanMatchItem> charsScanMatchItemList,
                          final String originalString,
                          final CharsScanContext context,
                          final char[] oldChars) {
        // 基本属性
        final ICharsReplaceFactory charsReplaceFactory = context.getCharsReplaceFactory();

        final StringBuilder stringBuilder = new StringBuilder();
        // 排序，理论上是从前往后的。
        // 但是因为策略不同，先做一次排序 Olog(N)
        if(charsScanMatchItemList.size() > 1) {
            Collections.sort(charsScanMatchItemList, new CharsScanItemComparator());
        }

        int itemListIx = 0;
        int startIx = 0;
        int endIx = 0;

        // substring 的算法复杂度？
        while (endIx < oldChars.length
                && itemListIx < charsScanMatchItemList.size()) {
            // 当前需要替换的元素
            CharsScanMatchItem charsScanMatchItem = charsScanMatchItemList.get(itemListIx);
            // 需要考虑可能这次的元素和上一次存在重叠的情况。
            // 直接忽略当前 item，继续下一次遍历。
            int currentItemEndIndex = charsScanMatchItem.getEndIndex();
            if(currentItemEndIndex <= endIx) {
                itemListIx++;
                continue;
            }

            // 获取下一个 endIndex (0,8]
            endIx = Math.max(endIx, getEndIndex(charsScanMatchItem));

            if(endIx > startIx) {
                // 不要截取，减少 String 的创建
                // 底层是 chars
                InnerCharUtil.appendChars(stringBuilder, oldChars, startIx, endIx-1);
            }

            boolean hasReplaced = hasReplaced(startIx, endIx, charsScanMatchItemList, itemListIx);
            if(hasReplaced) {
                // 不做处理
                InnerCharUtil.appendChars(stringBuilder, oldChars, charsScanMatchItem.getStartIndex(), charsScanMatchItem.getEndIndex());
            } else {
                // 替换
                ICharsReplace charsReplace = charsReplaceFactory.getReplace(charsScanMatchItem.getScanType());
                charsReplace.replace(stringBuilder, oldChars, charsScanMatchItem, context);
            }

            // 更新开始/结束位置
            startIx = Math.max(endIx, charsScanMatchItem.getEndIndex()) + 1;
            endIx = Math.max(startIx, endIx);
            itemListIx++;
        }

        // 剩余的部分
        if(startIx < oldChars.length) {
            InnerCharUtil.appendChars(stringBuilder, oldChars, startIx, oldChars.length-1);
        }

        return stringBuilder.toString();
    }

    /**
     * 判断当前的 ix 是否已经涉及到替换
     *
     * 思路：和上一个替换的命中 item 存在一定范围内的重合。
     * 这个概率很低，后续可以优化，预留
     *
     * 可以指定自己的默认策略，这里预留了方法，不在冗余暴露接口。
     *
     * @param charsScanMatchItemList 列表
     * @param itemIndex 下标
     * @return 结果
     */
    private static boolean hasReplaced(int startIx,
                                       int endIx,
                                       List<CharsScanMatchItem> charsScanMatchItemList,
                                       int itemIndex) {
        //TODO: 这里可以判断是否重合，比如和上一个判断。如果重合，则可以忽略处理。
        CharsScanMatchItem item = charsScanMatchItemList.get(itemIndex);
        // 说明上一次的处理，已经覆盖过这里了，需要往下依次迭代走。
        if(endIx > item.getEndIndex()) {
            return true;
        }

        return false;
    }

    /**
     * 可以截取的截止位置
     *
     * @param charsScanMatchItem 结果
     */
    private static int getEndIndex(CharsScanMatchItem charsScanMatchItem) {
        return charsScanMatchItem.getStartIndex();
    }

}
