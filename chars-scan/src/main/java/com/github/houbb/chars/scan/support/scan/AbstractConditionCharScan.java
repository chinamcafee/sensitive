package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.trie.api.ITrieTree;

import java.util.Set;

/**
 * 抽象
 * @author d
 * @since 1.0.0
 */
public abstract class AbstractConditionCharScan extends AbstractCharScan {

    /**
     * 字符是否满足条件
     *
     * 这里只做简单的判断，提升 append 的性能。
     *
     * @param i 位置
     * @param c 字符
     * @param chars 原始数组
     * @return 结果
     */
    protected abstract boolean isCharMatchCondition(int i, char c, char[] chars);

    /**
     * 当前字符串是否满足条件
     *
     * 可以在这里做复杂的判断。
     *
     * @param i 下标
     * @param c 字符
     * @param chars 原始数组
     * @param context 上下文
     * @return 结果
     */
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        return true;
    }

    @Override
    public void scan(int i, char c, char[] chars, final CharsScanContext context) {
        // 分成几个场景
        boolean currentMatchCondition = isCharMatchCondition(i, c, chars);

        // 前一个不满足，现在满足。开始初始化
        if(!isPreCharMatchCondition()
            && currentMatchCondition) {
            clearBuffer();

            // 可以抽离一个接口模拟 buffer，但是没必要
            getBuffer().append(c);
        } else if(!isPreCharMatchCondition() && !currentMatchCondition) {
            // 前一个不满足，现在也不满足。
            // 应该啥也不做
        } else if(isPreCharMatchCondition() && currentMatchCondition) {
            // 前一个是满足，现在也满足。继续累加
            getBuffer().append(c);
        } else if(isPreCharMatchCondition() && !currentMatchCondition) {
            // 上一个满足，现在不满足。
            // 则进行清空，并且整理结果
            clearBufferAndAddItem(i, c, chars, context);
        }

        // 更新匹配条件
        setPreCharMatchCondition(currentMatchCondition);

        // 如果是最后一个元素，避免最后才匹配的情况
        if(i == chars.length - 1
            && currentMatchCondition) {
            // 这里需要+1
            clearBufferAndAddItem(i+1, c, chars, context);
        }
    }

    /**
     * 清空缓存，并且构建匹配的结果
     * @param i 当前
     * @param c 当前
     * @param chars 原始
     * @param context 上下文
     */
    protected void clearBufferAndAddItem(int i, char c, char[] chars,
                                         final CharsScanContext context) {
        try {
            // 前缀条件处理
            final boolean prefixFlag = isPrefixMatch(i, c, chars, context);
            if(!prefixFlag) {
                return;
            }

            // 白名单校验
            final ITrieTree trieTree = context.whiteListTrie();
            if(trieTree.search(this.getBuffer())) {
                return;
            }

            // 根据 chars 判断，尽量这里减少一次 string
            addItemWhenStringMatch(i, c, chars, context);
        } finally {
            // 清空缓存，这里 return 也会被执行。
            clearBuffer();
        }
    }

    /**
     * 当字符串匹配的时候，添加字符串信息
     * @param i 下标
     * @param c 字符
     * @param chars 字符数组
     * @param context 上下文
     */
    protected void addItemWhenStringMatch(int i, char c, char[] chars, final CharsScanContext context) {
        boolean isStringMatchCondition = isStringMatchCondition(i, c, chars, context);
        if(isStringMatchCondition) {
            CharsScanMatchItem charsScanMatchItem = new CharsScanMatchItem();

            // TODO:注意下这里是否正确
            // 判断 buffer 的长度，用于统一的长度截断。可以拓展
            final int bufferLen = getBuffer().length();
            final String scanType = getScanType();
            charsScanMatchItem.setStartIndex(i - bufferLen);
            charsScanMatchItem.setEndIndex(i-1);
            charsScanMatchItem.setScanType(scanType);
            charsScanMatchItem.setPriority(getPriority());

            super.addMatchItem(charsScanMatchItem);
        }
    }

    /**
     * 是否匹配
     * @param i 索引
     * @param c 字符
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     * @since 1.17.0
     */
    protected boolean isPrefixMatch(int i, char c, char[] chars,
                                    final CharsScanContext context) {
        final Set<Character> characterSet = context.prefixCharSet();

        // 空时，匹配所有
        if(CollectionUtil.isEmpty(characterSet)) {
            return true;
        }

        int prefixIx = getPrefixMatchPrefixIx(i, chars, context);
        if(prefixIx < 0) {
            return false;
        }

        char preChar = chars[prefixIx];

        // 是否为任一中文匹配
        if(isAnyChineseMatch(preChar, context)) {
            return true;
        }

        // 特殊转义符号
        if(isAnyEscapePrefixCharMatch(preChar, context)) {
            return true;
        }

        // 普通的前缀符号
        return isPrefixCharContains(preChar, context);
    }

    /**
     * 是否匹配任何一个转移符号
     * @param preChar 符号
     * @param context 上下文
     * @return 结果
     */
    protected boolean isAnyEscapePrefixCharMatch(char preChar,
                                                 final CharsScanContext context) {
        Set<Character> escapeChars = context.getEscapePrefixCharSet();

        if(escapeChars.contains(preChar)) {
            return true;
        }

        return false;
    }

    /**
     * 获取前缀的位置
     * @since 1.24.0
     * @param i 索引
     * @param chars 数组
     * @param context 上下文
     * @return 结果
     */
    protected int getPrefixMatchPrefixIx(int i, char[] chars,
                                        final CharsScanContext context) {
        StringBuilder buffer = getBuffer();
        int bufferLen = buffer.length();
        int prefixIx = i - bufferLen - 1;
        return prefixIx;
    }

    /**
     * 任一中文匹配
     * @param preChar 前缀
     * @param context 上下文
     * @return 结果
     */
    protected boolean isAnyChineseMatch(char preChar,
                                        final CharsScanContext context) {
        String scanType = getScanType();

        if (CharsScanTypeEnum.PHONE.getScanType().equals(scanType)
                || CharsScanTypeEnum.EMAIL.getScanType().equals(scanType)
                || CharsScanTypeEnum.BANK_CARD.getScanType().equals(scanType)
                || CharsScanTypeEnum.ID_NO.getScanType().equals(scanType)
                || CharsScanTypeEnum.MERGE_NUMS.getScanType().equals(scanType)
        ) {
            return CharUtil.isChinese(preChar);
        }

        return false;
    }

    /**
     * 字符是否包含
     * @param context 上下文
     * @param preChar 前缀字符
     * @return 结果
     */
    protected boolean isPrefixCharContains(char preChar,
                                           final CharsScanContext context) {
        final Set<Character> characterSet = context.prefixCharSet();

        return characterSet.contains(preChar);
    }


    /**
     * 为合并类添加元素
     * @param scanTypeEnum 扫描的真正类别
     * @param i 下标
     * @param chars 字符
     * @param context 上下文
     * @since 1.26.0
     */
    protected void addMatchItemForMerge(CharsScanTypeEnum scanTypeEnum, int i, char[] chars, CharsScanContext context) {
        boolean isSupport = isSupportMergeScanType(scanTypeEnum, i, chars, context);
        if(!isSupport) {
            return;
        }

        CharsScanMatchItem charsScanMatchItem = new CharsScanMatchItem();

        final int bufferLen = getBuffer().length();
        charsScanMatchItem.setStartIndex(i - bufferLen);
        charsScanMatchItem.setEndIndex(i-1);
        charsScanMatchItem.setScanType(scanTypeEnum.getScanType());
        charsScanMatchItem.setPriority(scanTypeEnum.getPriority());

        super.addMatchItem(charsScanMatchItem);
    }

    /**
     * 是否为支持的合并类别
     *
     * @param scanTypeEnum 扫描类别
     * @param i 下标
     * @param chars 字符串
     * @param context 上下文
     * @return 是否支持
     * @since 1.29.0
     */
    protected boolean isSupportMergeScanType(CharsScanTypeEnum scanTypeEnum, int i, char[] chars, CharsScanContext context) {
        return true;
    }

}
