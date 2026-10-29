package com.github.houbb.chars.scan.api;

/**
 * 替换策略
 *
 * @author d
 * @since 1.0.0
 */
public interface ICharsReplace extends ICharsScanType {

    /**
     * 执行替换
     * @param stringBuilder 构建的 buffer，避免 string 的多次创建
     * @param chars 原始的数组，使用数组的性能会优于 charAt，因为 charAt 会进行数组越界等判断。
     * @param charsScanMatchItem 扫描的
     * @param context 上下文
     * @since 0.3.0
     */
    void replace(final StringBuilder stringBuilder,
                 final char[] chars,
                 final CharsScanMatchItem charsScanMatchItem,
                 final CharsScanContext context);

}
