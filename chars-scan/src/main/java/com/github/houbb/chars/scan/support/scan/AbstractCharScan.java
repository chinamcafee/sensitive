package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.api.ICharsScan;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象
 *
 * 1. thread-safe
 * 私有变量，必须 new
 *  new AbstractCharScan();
 *
 * 2. PhoneScanXX 一个
 *
 * 用之前：
 * ThreadLocal 线程级别
 *
 * itemList.clear();
 * buffer.clear();
 * preCondition false
 *
 * @author d
 * @since 1.0.0
 */
public abstract class AbstractCharScan implements ICharsScan {

    /**
     * 元素列表
     */
    private final List<CharsScanMatchItem> itemList = new ArrayList<CharsScanMatchItem>();

    /**
     * 缓存
     */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * 前一个字符是否满足条件
     * @since 0.3.0
     */
    private volatile boolean preCharMatchCondition = false;

    public boolean isPreCharMatchCondition() {
        return preCharMatchCondition;
    }

    public void setPreCharMatchCondition(boolean preCharMatchCondition) {
        this.preCharMatchCondition = preCharMatchCondition;
    }

    /**
     * 获取缓存，调整为方法，便于后续改为 threadLocal
     * @return 缓存
     * @since 0.4.0
     */
    protected StringBuilder getBuffer() {
        return this.buffer;
    }

    /**
     * 清空当前的 buffer
     * 使用场景：如果一个已经匹配，可以考虑清空其他的信息。
     */
    protected void clearBuffer() {
        // 这里其实可选，性能会好一点
        buffer.setLength(0);
    }

    @Override
    public List<CharsScanMatchItem> getMatchList() {
        return itemList;
    }

    /**
     * 添加匹配元素
     * @param item 元素
     * @since 0.4.0
     */
    protected void addMatchItem(final CharsScanMatchItem item) {
        this.itemList.add(item);
    }

    @Override
    public void clear() {
        this.clearBuffer();

        this.preCharMatchCondition = false;

        this.itemList.clear();
    }

}
