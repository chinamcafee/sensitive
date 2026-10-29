package com.github.houbb.chars.scan.support.replace.factory;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.heaven.util.util.MapUtil;

import java.util.Map;

/**
 * 抽象实现，预留
 *
 * @since 1.14.0
 */
public abstract class AbstractCharsReplaceFactory implements ICharsReplaceFactory {

    /**
     * 获取替换策略
     * 1. 策略需要统一。
     * 保障 scan 与 replace 策略一致。
     *
     * @param scanType       类别
     * @param replaceMap     所有的替换策略
     * @param defaultReplace 默认策略
     * @return 结果
     * @since 0.9.0
     */
    protected ICharsReplace getCharsReplace(String scanType, Map<String, ICharsReplace> replaceMap, ICharsReplace defaultReplace) {
        if (MapUtil.isEmpty(replaceMap)) {
            return defaultReplace;
        }

        ICharsReplace replace = replaceMap.get(scanType);
        if (replace != null) {
            return replace;
        }

        return defaultReplace;
    }


}
