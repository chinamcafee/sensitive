package com.github.houbb.chars.scan.api;

/**
 * 替换哈希策略
 *
 * @author d
 * @since 1.12.0
 */
public interface ICharsReplaceHash {

    /**
     * 执行哈希策略
     * @param bytes 数组
     * @param context 上下文
     * @param builder 构建器
     */
    void hash(byte[] bytes,
                final CharsScanContext context,
                final StringBuilder builder);

}
