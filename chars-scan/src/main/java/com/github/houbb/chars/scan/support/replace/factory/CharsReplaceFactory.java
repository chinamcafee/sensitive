package com.github.houbb.chars.scan.support.replace.factory;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;

import java.util.Collection;
import java.util.Map;

/**
 * 默认工厂策略
 *
 * @since 1.14.0
 */
public class CharsReplaceFactory extends AbstractCharsReplaceFactory {

    /**
     * 替换策略
     */
    private final Map<String, ICharsReplace> charsReplaceMap;

    /**
     * 默认策略
     */
    private final ICharsReplace defaultReplace;

    public CharsReplaceFactory(Collection<ICharsReplace> charsReplaceCollection, ICharsReplace defaultReplace) {
        this.charsReplaceMap = CharsReplaces.chainsMap(charsReplaceCollection);
        this.defaultReplace = defaultReplace;
    }

    public CharsReplaceFactory(Collection<ICharsReplace> charsReplaceList) {
        this(charsReplaceList, CharsReplaces.defaultReplace());
    }

    public CharsReplaceFactory() {
        this(CharsReplaces.defaultReplaceCollection());
    }

    @Override
    public ICharsReplace getReplace(String scanType) {
        return getCharsReplace(scanType, charsReplaceMap, defaultReplace);
    }

}
