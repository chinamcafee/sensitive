package com.github.houbb.chars.scan.api;

/**
 * 扫描中匹配的元素
 * @author d
 * @since 1.0.0
 */
public class CharsScanMatchItem {

    /**
     * 开始下标
     */
    private int startIndex;

    /**
     * 结束下标
     */
    private int endIndex;

    /**
     * 元素类别
     *
     * 这里使用枚举，而不是 code。
     * 1. 通过 code 获取信息，还要通过遍历映射
     * 2. 枚举值 jvm 处理过，都是单例。
     */
    private String scanType;

    /**
     * 扫描优先级
     * @since 1.8.0
     */
    private int priority;

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getScanType() {
        return scanType;
    }

    public int getPriority() {
        return priority;
    }

    public void setScanType(String scanType) {
        this.scanType = scanType;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @since 1.9.0
     * @return 结果
     */
    public int getLength() {
        return endIndex - startIndex + 1;
    }

    @Override
    public String toString() {
        return "CharsScanMatchItem{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                ", scanType='" + scanType + '\'' +
                ", priority=" + priority +
                '}';
    }

}
