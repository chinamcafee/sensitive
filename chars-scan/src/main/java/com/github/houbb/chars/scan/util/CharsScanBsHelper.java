package com.github.houbb.chars.scan.util;

import com.github.houbb.chars.scan.api.ICharsCore;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;

import java.util.Set;

/**
 * 单独设计的原因：可以让 context 复用。
 *
 * @author d
 * @since 1.0.0
 */
public class CharsScanBsHelper {

    /**
     * 扫描并且替换
     * @param text 文本
     * @param charScanFactory 扫描策略
     * @param charsReplaceFactory 替换策略
     * @param charsCore 核心实现
     * @param charsPrefix 前缀策略
     * @param charsReplaceHash 替换时是否需要添加哈希后缀
     * @return 结果
     * @since 1.12.0
     */
    public static String scanAndReplace(String text,
                                        ICharsScanFactory charScanFactory,
                                        ICharsReplaceFactory charsReplaceFactory,
                                        ICharsCore charsCore,
                                        Set<Character> charsPrefix,
                                        ICharsReplaceHash charsReplaceHash) {
        CharsScanBs charsScanBs = CharsScanBs.newInstance()
                .charsScanFactory(charScanFactory)
                .charsReplaceFactory(charsReplaceFactory)
                .charsCore(charsCore)
                .prefixCharSet(charsPrefix)
                .charsReplaceHash(charsReplaceHash)
                .init();

        return charsScanBs.scanAndReplace(text);
    }

    /**
     * 扫描并且替换
     * @param text 文本
     * @param charScanFactory 扫描列表
     * @param charsReplaceFactory 替换策略
     * @param charsCore 核心实现
     * @param charsPrefix 前缀策略
     * @return 结果
     * @since 1.6.0
     */
    public static String scanAndReplace(String text,
                                        ICharsScanFactory charScanFactory,
                                        ICharsReplaceFactory charsReplaceFactory,
                                        ICharsCore charsCore,
                                        Set<Character> charsPrefix) {
        return scanAndReplace(text, charScanFactory, charsReplaceFactory, charsCore, charsPrefix, CharsReplaceHashes.defaults());
    }


    /**
     * 扫描并且替换
     * @param text 文本
     * @param charScanFactory 扫描列表
     * @param charsReplaceFactory 替换策略
     * @param charsCore 核心实现
     * @return 结果
     * @since 1.6.0
     */
    public static String scanAndReplace(String text,
                                        ICharsScanFactory charScanFactory,
                                        ICharsReplaceFactory charsReplaceFactory,
                                        ICharsCore charsCore) {
        return scanAndReplace(text, charScanFactory, charsReplaceFactory, charsCore, InnerCharUtil.DEFAULT_PREFIX_SET);
    }


    /**
     * 扫描并且获取替换结果
     * @param text 文本
     * @param charScanFactory 扫描策略
     * @param charsReplaceFactory 替换策略
     * @return 结果
     */
    public static String scanAndReplace(String text,
                                        ICharsScanFactory charScanFactory,
                                        ICharsReplaceFactory charsReplaceFactory) {
        return scanAndReplace(text, charScanFactory, charsReplaceFactory, CharsCores.defaults());
    }

    /**
     * 扫描并且获取替换结果
     * @param text 文本
     * @param charScanFactory 扫描策略
     * @return 结果
     */
    public static String scanAndReplace(String text,
                                        ICharsScanFactory charScanFactory) {
        return scanAndReplace(text, charScanFactory, CharsReplaces.defaults());
    }

    /**
     * 扫描并且获取替换结果
     * @param text 文本
     * @return 结果
     */
    public static String scanAndReplace(String text) {
        return scanAndReplace(text, CharsScans.defaults());
    }

}
