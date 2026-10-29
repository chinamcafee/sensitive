package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.ICharsReplace;
import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.exception.CharsScanException;
import com.github.houbb.chars.scan.support.replace.factory.CharsReplaceFactory;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.*;

/**
 * @author d
 * @since 1.0.0
 */
public class CharsReplaces {


    /**
     * 需要保证替换策略无状态
     */
    private static final Map<String, ICharsReplace> DEFAULT_MAP;

    static {
        //TODO: 这里可以根据实际需要拓展
        DEFAULT_MAP  = chainsMap(
                SingletonCharsReplaceFactory.getBankCard(),
                SingletonCharsReplaceFactory.getChineseName(),
                SingletonCharsReplaceFactory.getBirthday(),
                SingletonCharsReplaceFactory.getChineseName(),
                SingletonCharsReplaceFactory.getEmail(),
                SingletonCharsReplaceFactory.getGps(),
                SingletonCharsReplaceFactory.getIdNo(),
                SingletonCharsReplaceFactory.getPhone(),
                SingletonCharsReplaceFactory.getIpv4(),
                SingletonCharsReplaceFactory.getAddress(),
                SingletonCharsReplaceFactory.getPassport(),
                SingletonCharsReplaceFactory.getMaskHalf(),
                SingletonCharsReplaceFactory.getMaskAll(),
                SingletonCharsReplaceFactory.getNone()
                );
    }

    /**
     * 默认策略
     * @return 默认策略
     * @since 1.14.0
     */
    public static ICharsReplaceFactory defaults() {
        return new CharsReplaceFactory();
    }

    /**
     * 链式
     * @param replaces 替换策略
     * @return 结果
     */
    private static List<ICharsReplace> chains(ICharsReplace ... replaces) {
        if(ArrayUtil.isEmpty(replaces)) {
            return Collections.emptyList();
        }

        List<ICharsReplace> list = new ArrayList<ICharsReplace>(Arrays.asList(replaces));
        return list;
    }

    /**
     * 链式 map
     * @param replaces 策略
     * @return 结果
     * @since 0.3.0
     */
    public static Map<String, ICharsReplace> chainsMap(ICharsReplace ... replaces) {
        Map<String, ICharsReplace> map = new HashMap<String, ICharsReplace>();
        if(ArrayUtil.isEmpty(replaces)) {
            return map;
        }

        for(ICharsReplace replace : replaces) {
            map.put(replace.getScanType(), replace);
        }

        return map;
    }

    /**
     * 链式 map
     * @param charsReplaceCollection 策略集合
     * @return 结果
     * @since 1.14.0
     */
    public static Map<String, ICharsReplace> chainsMap(Collection<ICharsReplace> charsReplaceCollection) {
        Map<String, ICharsReplace> map = new HashMap<String, ICharsReplace>();
        if(CollectionUtil.isEmpty(charsReplaceCollection)) {
            return map;
        }

        for(ICharsReplace replace : charsReplaceCollection) {
            map.put(replace.getScanType(), replace);
        }

        return map;
    }

    /**
     * 默认的替换策略集合
     * @return 结果
     * @since 1.14.0
     */
    public static Collection<ICharsReplace> defaultReplaceCollection() {
        return Collections.unmodifiableCollection(DEFAULT_MAP.values());
    }

    /**
     * 默认替换策略
     * @return 默认替换策略
     * @since 1.14.0
     */
    public static ICharsReplace defaultReplace() {
        return SingletonCharsReplaceFactory.getMaskHalf();
    }

    /**
     * 实现
     * @param scanTypeCode 替换策略
     * @return 实现
     * @since 1.15.0
     */
    private static ICharsReplace getCharsReplace(final String scanTypeCode) {
        ICharsReplace charsReplace = DEFAULT_MAP.get(scanTypeCode);
        if(charsReplace == null) {
            throw new CharsScanException("Not support replace for " + scanTypeCode);
        }

        return charsReplace;
    }

    /**
     * 默认的替换工厂
     * @param scanTypeList 列表
     * @param defaultScanType 默认
     * @return 结果
     * @since 1.15.0
     */
    public static ICharsReplaceFactory defaultsReplaceFactory(List<String> scanTypeList,
                                                              String defaultScanType) {
        List<ICharsReplace> replaceList = new ArrayList<ICharsReplace>(scanTypeList.size());

        for(String scanType : scanTypeList) {
            replaceList.add(getCharsReplace(scanType));
        }
        ICharsReplace defaultReplace = getCharsReplace(defaultScanType);
        return new CharsReplaceFactory(replaceList, defaultReplace);
    }

}
