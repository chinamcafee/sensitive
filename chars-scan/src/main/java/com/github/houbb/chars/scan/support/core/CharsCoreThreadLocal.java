package com.github.houbb.chars.scan.support.core;

import com.github.houbb.chars.scan.api.*;
import com.github.houbb.chars.scan.support.scan.factory.CharsScanFactoryThreadLocal;

import java.util.*;

/**
 * 核心实现
 *
 * @author d
 * @since 1.0.0
 */
public class CharsCoreThreadLocal extends CharsCoreCommon {

    protected List<ICharsScan> getCharScanList(final CharsScanContext context) {
        final ICharsScanFactory charScanFactory = context.getCharScanFactory();
        return CharsScanFactoryThreadLocal.getAndCache(charScanFactory);
    }

    /**
     * finally 回調信息
     *
     * 可以用来执行清空等操作。
     *
     * @param context 上下文
     * @since 1.13.0
     */
    protected void finallyCallBack(final CharsScanContext context) {
        CharsScanFactoryThreadLocal.clearAll(context.getCharScanFactory());
    }

}
