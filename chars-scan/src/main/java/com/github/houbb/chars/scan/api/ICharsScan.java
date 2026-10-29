package com.github.houbb.chars.scan.api;

import java.util.List;

/**
 *
 * 统一的字符串 scan
 *
 * 编写时注意几点：
 * 1. 尽量减少对象的创建
 * 2. 尽量减少 O(N) 及以上的各种操作
 *
 * @author d
 * @since 1.0.0
 */
public interface ICharsScan extends ICharsScanPriority, ICharsScanType, ICharsLifecycle {

    /**
     * 扫描
     * @param i 下标
     * @param c 当前字符
     * @param chars 整体的字符串数组
     * @param context 上下文
     */
    void scan(int i, char c, char[] chars, final CharsScanContext context);

    // 清空其他的 buffer
    /**
     * 获取所有匹配的列表
     * @return 结果
     * @since 0.1.0
     */
    List<CharsScanMatchItem> getMatchList();

}

