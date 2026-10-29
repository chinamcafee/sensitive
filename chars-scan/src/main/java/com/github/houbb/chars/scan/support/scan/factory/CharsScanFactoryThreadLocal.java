package com.github.houbb.chars.scan.support.scan.factory;

import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 新建对象实例
 *
 * @since 1.13.0
 * @see SoftReference 软引用
 */
public class CharsScanFactoryThreadLocal {

    /**
     * key: scanTypeList.toString 保证不同的类别配置调用，对应的实现不同。从而避免存在问题。
     *
     */
    private static final ThreadLocal<ConcurrentHashMap<String,List<ICharsScan>>> THREAD_LOCAL = new ThreadLocal<ConcurrentHashMap<String,List<ICharsScan>>>();

    static {
        // 初始化
        THREAD_LOCAL.set(new ConcurrentHashMap<String, List<ICharsScan>>());
    }

    /**
     * 初始化
     * 1. 如何并发控制？
     * 2. 如何尽可能的提升性能
     * @param charScanFactory 工厂
     * @return result
     */
    public static List<ICharsScan> getAndCache(ICharsScanFactory charScanFactory) {
        List<String> scanTypeCodeList = charScanFactory.scanTypeList();
        ConcurrentHashMap<String, List<ICharsScan>> scanConcurrentHashMap = THREAD_LOCAL.get();

        //cache
        String key = scanTypeCodeList.toString();
        List<ICharsScan> scanList = scanConcurrentHashMap.get(key);
        if(scanList == null) {
            scanList = charScanFactory.allCharScanList();

            // set
            scanConcurrentHashMap.putIfAbsent(key, scanList);
        }

        return scanList;
    }

    /**
     * 清空所有信息
     *
     * 1. 恢复初始化状态，保证对象的重用。
     * @param charScanFactory 扫描工厂
     */
    public static void clearAll(ICharsScanFactory charScanFactory) {
        Collection<ICharsScan> scanCollection = getAndCache(charScanFactory);
        if(CollectionUtil.isNotEmpty(scanCollection)) {
            for(ICharsScan charsScan : scanCollection) {
                charsScan.clear();
            }
        }
    }

}
