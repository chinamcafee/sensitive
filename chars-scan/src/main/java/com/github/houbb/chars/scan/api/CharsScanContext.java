package com.github.houbb.chars.scan.api;

import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.util.Map;
import java.util.Set;

/**
 * 上下文
 *
 * 每一次请求，只有一个，保证不变。
 * @since 0.9.0
 */
public class CharsScanContext {

    /**
     * 是否需要添加哈希
     *
     * 不会变化的属性，不存在并发问题。
     *
     * 1. 如果只是一个参数传入，会导致所有的 replace 策略都要添加或者不添加，不够灵活；
     * 2. 如果每一个 replace 的属性都设置一遍，那么和直接创建区别不大。
     * 3. 也可以继承重载实现，但是没必要。
     *
     * @since 1.12.0
     */
    private ICharsReplaceHash charsReplaceHash;

    /**
     * 扫描工厂
     *
     * 说明：主要是为了用户自定义，同时尝试放在 {@link ThreadLocal}
     * @since 1.13.0
     */
    private ICharsScanFactory charScanFactory;

    /**
     * 替换工厂
     * @since 1.14.0
     */
    private ICharsReplaceFactory charsReplaceFactory;

    /**
     * 扫描的开始位置
     * @since 1.17.0
     */
    private int scanStartIndex;

    /**
     * 前缀字符集合
     * @since 1.17.0
     */
    private Set<Character> prefixCharSet;

    /**
     * 白名单前缀树
     * @since 1.21.0
     */
    private ITrieTree whiteListTrie = TrieTrees.node();

    /**
     * 拓展类的扫描类
     * @since 1.28.0
     */
    private CharsScanTypeEnum expandScanType;

    /**
     * 转义符号
     *
     * @since 1.30.0
     */
    private Set<Character> escapePrefixCharSet;

    public ICharsReplaceHash getCharsReplaceHash() {
        return charsReplaceHash;
    }

    public CharsScanContext setCharsReplaceHash(ICharsReplaceHash charsReplaceHash) {
        this.charsReplaceHash = charsReplaceHash;
        return this;
    }

    public ICharsScanFactory getCharScanFactory() {
        return charScanFactory;
    }

    public CharsScanContext setCharScanFactory(ICharsScanFactory charScanFactory) {
        this.charScanFactory = charScanFactory;
        return this;
    }

    public ICharsReplaceFactory getCharsReplaceFactory() {
        return charsReplaceFactory;
    }

    public CharsScanContext setCharsReplaceFactory(ICharsReplaceFactory charsReplaceFactory) {
        this.charsReplaceFactory = charsReplaceFactory;
        return this;
    }

    public int scanStartIndex() {
        return scanStartIndex;
    }

    public CharsScanContext setScanStartIndex(int scanStartIndex) {
        this.scanStartIndex = scanStartIndex;
        return this;
    }

    public Set<Character> prefixCharSet() {
        return prefixCharSet;
    }

    public CharsScanContext setPrefixCharSet(Set<Character> prefixCharSet) {
        this.prefixCharSet = prefixCharSet;
        return this;
    }

    public ITrieTree whiteListTrie() {
        return whiteListTrie;
    }

    public CharsScanContext setWhiteListTrie(ITrieTree whiteListTrie) {
        this.whiteListTrie = whiteListTrie;
        return this;
    }

    public CharsScanTypeEnum getExpandScanType() {
        return expandScanType;
    }

    public void setExpandScanType(CharsScanTypeEnum expandScanType) {
        this.expandScanType = expandScanType;
    }

    public Set<Character> getEscapePrefixCharSet() {
        return escapePrefixCharSet;
    }

    public void setEscapePrefixCharSet(Set<Character> escapePrefixCharSet) {
        this.escapePrefixCharSet = escapePrefixCharSet;
    }

    @Override
    public String toString() {
        return "CharsScanContext{" +
                "charsReplaceHash=" + charsReplaceHash +
                ", charScanFactory=" + charScanFactory +
                ", charsReplaceFactory=" + charsReplaceFactory +
                ", scanStartIndex=" + scanStartIndex +
                ", prefixCharSet=" + prefixCharSet +
                ", whiteListTrie=" + whiteListTrie +
                ", expandScanType=" + expandScanType +
                ", escapePrefixCharSet=" + escapePrefixCharSet +
                '}';
    }

}
