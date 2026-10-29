package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.List;

/**
 * 邮箱
 *
 * 核心思想：因为 @ 一般不多，所以这里首先寻找到 @，然后想两边 expand，直到不是 email 的时候截止。
 *
 * @author d
 * @since 1.24.0
 */
public abstract class AbstractExpandConditionCharScan extends AbstractConditionCharScan {

    @Override
    @Deprecated
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return false;
    }

    /**
     * 拓展的开始中心位置
     * @param i 位置
     * @param c 字符
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     */
    protected CharsScanTypeEnum isExpandStartCondition(int i, char c, char[] chars, CharsScanContext context) {
        // 位置，要求必须在上一个的后面。避免多次 expand
        List<CharsScanMatchItem> oldList = super.getMatchList();
        if(CollectionUtil.isNotEmpty(oldList)) {
            // 要求必须大于最后一个下标，避免重复处理
            int lastIx = oldList.get(oldList.size()-1).getEndIndex();
            if(i <= lastIx) {
                return null;
            }
        }

        return isExpandStartCharCondition(i, c, chars, context);
    }

    /**
     * 拓展的开始中心位置
     * @param i 索引
     * @param c 字符
     * @param chars 数组
     * @param context  上下文
     * @return 结果
     */
    protected abstract CharsScanTypeEnum isExpandStartCharCondition(int i, char c, char[] chars, CharsScanContext context);

    /**
     * 向左边拓展是否匹配
     * @param leftIx 左边索引
     * @param leftChar 字符
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     */
    protected boolean isLeftExpandMatchCondition(int leftIx, char leftChar, char[] chars, CharsScanContext context) {
        return false;
    }


    /**
     * 向左边 buffer 是否满足
     *
     * 最短：1
     *
     * 最长：32
     * @param middleIx 中间位置
     * @param startIx 开始位置
     * @param chars 字符数组
     * @param context 上下文
     * @return 结果
     */
    protected boolean isLeftBufferMatch(int middleIx, int startIx, char[] chars, CharsScanContext context) {
        return false;
    }

    /**
     * 向右边拓展是否匹配
     * @param rightIx 右边索引你
     * @param rightChar 右边字符
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     */
    protected boolean isRightExpandMatchCondition(int rightIx, char rightChar, char[] chars, CharsScanContext context) {
        return false;
    }

    /**
     * 向右边 buffer 是否满足
     *
     * 最短：1
     *
     * 最长：32
     *
     * a@xy.c
     * @param middleIx 中间位置
     * @param startIx 开始位置
     * @param endIx 结束位置
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     */
    protected boolean isRightBufferMatch(int middleIx, int startIx, int endIx, char[] chars, CharsScanContext context) {
        return false;
    }


    protected int loopHandleLeft(int i, char[] chars, CharsScanContext context, CharsScanTypeEnum scanTypeEnum) {
        StringBuilder buffer = super.getBuffer();

        int leftIx = i-1;
        for(leftIx = i-1; leftIx >= 0; leftIx--) {
            char cc = chars[leftIx];
            // 满足，添加
            if(isLeftExpandMatchCondition(leftIx, cc, chars, context)) {
                buffer.append(cc);
            } else {
                break;
            }
        }

        // 是否匹配前缀
        boolean isMatchPrefix = super.isPrefixMatch(i, chars[i], chars, context);
        if(!isMatchPrefix) {
            return -1;
        }

        // 判断左边是否满足
        // 因为会向左多一次
        int actualLeftIx = leftIx + 1;
        if(!isLeftBufferMatch(i, actualLeftIx, chars, context)) {
            return -1;
        } else {
            // 满足，则清空 left buffer?
            clearBuffer();
        }

        return actualLeftIx;
    }

    protected int loopHandleRight(int leftIx, int i, char[] chars, CharsScanContext context, CharsScanTypeEnum scanTypeEnum) {
        StringBuilder buffer = super.getBuffer();

        int rightIx = i+1;
        // 向右边
        for(rightIx = i+1; rightIx < chars.length; rightIx++) {
            char cc = chars[rightIx];
            // 满足，添加
            if(isRightExpandMatchCondition(rightIx, cc, chars, context)) {
                buffer.append(cc);
            } else {
                break;
            }
        }
        // 判断是否为符合条件的值
        // 判断左边是否满足
        int actualRightIx = rightIx - 1;
        if(!isRightBufferMatch(i, leftIx, actualRightIx, chars, context)) {
            return -1;
        }

        return actualRightIx;
    }

    @Override
    public void scan(int i, char c, char[] chars, CharsScanContext context) {
        // 判断当前是否为关键词
        CharsScanTypeEnum scanTypeEnum = isExpandStartCondition(i, c, chars, context);

        // 向两边拓展
        if(scanTypeEnum != null) {
            try {
                clearBuffer();

                // 处理左边
                int leftIx = loopHandleLeft(i, chars, context, scanTypeEnum);
                if(leftIx < 0) {
                    return;
                }

                // 处理右边
                int rightIx = loopHandleRight(leftIx, i, chars, context, scanTypeEnum);
                if(rightIx < 0) {
                    return;
                }

                // 两边都满足，则加入到结果中
                addExpandScanMatchItem(i, chars, context, leftIx, rightIx, scanTypeEnum);
            } finally {
                // 清空 buffer
                clearBuffer();
            }
        }
    }

    protected void addExpandScanMatchItem(int middleIx,
                                          char[] chars,
                                          CharsScanContext context,
                                          int leftIx,
                                          int rightIx,
                                          CharsScanTypeEnum scanTypeEnum) {
        // 两边都满足，则加入到结果中
        CharsScanMatchItem charsScanMatchItem = new CharsScanMatchItem();
        charsScanMatchItem.setStartIndex(leftIx);
        charsScanMatchItem.setEndIndex(rightIx);
        charsScanMatchItem.setScanType(scanTypeEnum.getScanType());
        charsScanMatchItem.setPriority(scanTypeEnum.getPriority());
        super.getMatchList().add(charsScanMatchItem);

    }

}
