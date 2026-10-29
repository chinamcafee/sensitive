package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.support.scan.factory.SimpleCharsScanFactory;
import com.github.houbb.heaven.util.util.ArrayUtil;

import java.util.Arrays;
import java.util.List;

public class CharsScans {

    /**
     * 默认的扫描策略
     *
     * TODO: 因为扫描拥有私有变量，是有状态的。
     *
     * // 或者把代码固定写死在 loop 中，但是这样非常不利于拓展和后续的维护，不建议。
     *
     * 所以为了避免并发问题，每一次都重新创建对象。
     * 当然，也可以采用 threadLocal 的方式，此处为了简单，暂时使用创建对象列表的方式。
     * 因为对象创建的方式更加灵活。
     *
     * @return 结果
     */
    public static ICharsScanFactory defaults() {
        return new SimpleCharsScanFactory();
    }

    /**
     * 默认策略列表
     * @param scanTypeList 扫描列表
     * @return 结果
     * @since 1.15.0
     */
    public static ICharsScanFactory defaults(List<String> scanTypeList) {
        return new SimpleCharsScanFactory(scanTypeList);
    }

    /**
     * 基本四要素
     * @return 列表
     * @since 1.5.0
     */
    public static ICharsScanFactory basic4() {
        return new SimpleCharsScanFactory();
    }

    /**
     * 链式初始化
     * @param charsScans 扫描类
     * @return 结果
     * @since 0.8.0
     */
    public static ICharsScanFactory chains(final String ... charsScans) {
        if(ArrayUtil.isEmpty(charsScans)) {
            return new SimpleCharsScanFactory(Arrays.asList(charsScans));
        }

        return new SimpleCharsScanFactory(Arrays.asList(charsScans));
    }

}
