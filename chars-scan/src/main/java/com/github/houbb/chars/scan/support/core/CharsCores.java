package com.github.houbb.chars.scan.support.core;

import com.github.houbb.chars.scan.api.ICharsCore;

/**
 * 核心类工具类
 * @since 1.6.0
 */
public final class CharsCores {

    private CharsCores(){}

    /**
     * 顺序执行核心策略
     * @return 实现
     */
    public static ICharsCore threadLocal() {
        return new CharsCoreThreadLocal();
    }

    /**
     * 并发执行
     * @param threadSize 线程大小
     * @return 结果
     * @since 1.6.0
     */
    public static ICharsCore concurrency(int threadSize) {
        return new CharsCoreConcurrency(threadSize);
    }

    /**
     * 并发执行
     * @return 结果
     * @since 1.6.0
     */
    public static ICharsCore concurrency() {
        return new CharsCoreConcurrency();
    }

    /**
     * 普通策略
     *
     * @return 旧策略
     * @since 1.13.0
     */
    public static ICharsCore common() {
        return new CharsCoreCommon();
    }

    /**
     * 默认策略
     *
     * @return 策略
     * @since 1.14.0
     */
    public static ICharsCore defaults() {
        return new CharsCoreCommon();
    }

}
