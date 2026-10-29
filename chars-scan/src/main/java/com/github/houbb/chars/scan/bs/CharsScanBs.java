package com.github.houbb.chars.scan.bs;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.chars.scan.constant.CharsScanConfigConst;
import com.github.houbb.chars.scan.constant.CharsScanConst;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.util.Set;

/**
 * 引导类
 *
 * @since 1.0.0
 */
public class CharsScanBs {

    /**
     * 核心实现
     */
    private ICharsCore charsCore = CharsCores.defaults();

    /**
     * 哈希策略
     * @since 1.12.0
     */
    private ICharsReplaceHash charsReplaceHash = CharsReplaceHashes.defaults();

    /**
     * 策略扫描工厂
     * @since 1.13.0
     */
    private ICharsScanFactory charScanFactory = CharsScans.defaults();

    /**
     * 替换策略工厂
     * @since 1.14.0
     */
    private ICharsReplaceFactory charsReplaceFactory = CharsReplaces.defaults();

    /**
     * 扫描的开始位置
     * @since 1.17.0
     */
    private int scanStartIndex = CharsScanConfigConst.DEFAULT_START_INDEX;

    /**
     * 前缀字符集合
     * @since 1.17.0
     */
    private Set<Character> prefixCharSet = InnerCharUtil.DEFAULT_PREFIX_SET;

    /**
     * 白名单前缀树
     * @since 1.21.0
     */
    private ITrieTree whiteListTrie = TrieTrees.node();

    /**
     * @since 1.30.0
     */
    private Set<Character> escapePrefixCharSet = InnerCharUtil.DEFAULT_ESCAPE_PREFIX_SET;

    /**
     * 上下文
     *
     * 1. 为了便于后续参数的拓展，而不是通过修改参数。
     */
    private CharsScanContext context;


    public static CharsScanBs newInstance() {
        return new CharsScanBs();
    }

    public CharsScanBs whiteListTrie(ITrieTree whiteListTrie) {
        ArgUtil.notNull(whiteListTrie, "whiteListTrie");

        this.whiteListTrie = whiteListTrie;
        return this;
    }

    public CharsScanBs charsCore(ICharsCore charsCore) {
        ArgUtil.notNull(charsCore, "charsCore");

        this.charsCore = charsCore;
        return this;
    }

    public CharsScanBs charsReplaceHash(ICharsReplaceHash charsReplaceHash) {
        ArgUtil.notNull(charsReplaceHash, "charsReplaceHash");

        this.charsReplaceHash = charsReplaceHash;
        return this;
    }

    public CharsScanBs charsScanFactory(ICharsScanFactory charScanFactory) {
        ArgUtil.notNull(charScanFactory, "charScanFactory");

        this.charScanFactory = charScanFactory;
        return this;
    }

    public CharsScanBs charsReplaceFactory(ICharsReplaceFactory charsReplaceFactory) {
        ArgUtil.notNull(charsReplaceFactory, "charsReplaceFactory");

        this.charsReplaceFactory = charsReplaceFactory;
        return this;
    }

    public CharsScanBs prefixCharSet(Set<Character> prefixCharSet) {
        ArgUtil.notNull(prefixCharSet, "prefixCharSet");

        this.prefixCharSet = prefixCharSet;
        return this;
    }

    public CharsScanBs escapePrefixCharSet(Set<Character> escapePrefixCharSet) {
        ArgUtil.notNull(escapePrefixCharSet, "escapePrefixCharSet");

        this.escapePrefixCharSet = escapePrefixCharSet;
        return this;
    }

    public CharsScanBs scanStartIndex(int scanStartIndex) {
        this.scanStartIndex = scanStartIndex;
        return this;
    }

    /**
     * 初始化
     * @return this
     */
    public CharsScanBs init() {
        context = new CharsScanContext();
        context.setCharScanFactory(charScanFactory);
        context.setCharsReplaceFactory(charsReplaceFactory);
        context.setCharsReplaceHash(charsReplaceHash);
        context.setScanStartIndex(scanStartIndex);
        context.setPrefixCharSet(prefixCharSet);
        context.setWhiteListTrie(whiteListTrie);
        context.setEscapePrefixCharSet(escapePrefixCharSet);

        return this;
    }

    /**
     * 扫描并且替换
     * @param text 文本
     * @return 结果
     */
    public String scanAndReplace(String text) {
        return this.charsCore.scanAndReplace(text, context);
    }

}
