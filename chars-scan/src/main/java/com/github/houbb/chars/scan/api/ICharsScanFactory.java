package com.github.houbb.chars.scan.api;

import java.util.List;

/**
 * @author d
 * @since 1.13.0
 */
public interface ICharsScanFactory {

    /**
     * 支持的扫描列表
     * @return 列表
     */
    List<String> scanTypeList();

    /**
     * 获取扫描策略
     * @param scanType 扫描类别
     * @return 结果
     */
    ICharsScan getCharScan(String scanType);

    /**
     * 获取所有的扫描列表
     * @return 结果
     * @since 1.13.0
     */
    List<ICharsScan> allCharScanList();

}
