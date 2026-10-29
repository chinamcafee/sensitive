package com.github.houbb.chars.scan.api;

/**
 * 字符替换工厂策略
 * @since 1.14.0
 */
public interface ICharsReplaceFactory {

    /**
     * 获取替换侧列
     * @param scanType 替换类别
     * @return 实现策略
     * @since 1.14.0
     */
    ICharsReplace getReplace(final String scanType);

}
