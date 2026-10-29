package com.github.houbb.chars.scan.constant;

import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.api.ICharsScanPriority;
import com.github.houbb.chars.scan.api.ICharsScanType;
import com.github.houbb.chars.scan.support.replace.PhoneCharsReplace;
import com.github.houbb.chars.scan.support.replace.IdNoCharsReplace;
import com.github.houbb.chars.scan.support.scan.IdNoConditionCharScan;
import com.github.houbb.chars.scan.support.scan.PhoneConditionCharScan;

/**
 *
 * 对应的 code 用数字代替，比英文长字母稍微降低内存占用。
 *
 * @author d
 * @since 1.0.0
 */
public enum CharsScanTypeEnum implements ICharsScanType, ICharsScanPriority {
    /**
     * 手机号
     * @see PhoneConditionCharScan 扫描
     * @see PhoneCharsReplace 替换
     */
    PHONE("1", "手机号", 1),
    /**
     * 身份证
     * @see IdNoConditionCharScan 扫描
     * @see IdNoCharsReplace 替换
     */
    ID_NO("2", "身份证", 2),
    /**
     *
     */
    BANK_CARD("3", "银行卡", 3),
    EMAIL("4", "邮箱", 4),
    CHINESE_NAME("5", "中国人名", 5),
    BIRTHDAY("6", "出生日期", 6),
    GPS("7", "GPS", 7),
    IPV4("8", "IPV4", 8),
    ADDRESS("9", "地址", 9),
    PASSPORT("10", "护照", 10),
    /**
     * 用于匹配所有场景
     */
    ANY_NONE("11", "匹配任意不掩盖", 11),
    ANY_HALF("12", "匹配任意半掩盖", 12),
    ANY_ALL("13", "匹配任意全掩盖", 13),
    MERGE_NUMS("m1", "合并数字类类的操作1,2,3", 14),
    MERGE_NAME_ADDRESS("m2", "合并人名称和地址 5,9", 15),
    MERGE_EXPAND("m3", "拓展类合并到一起 4,5,9", 16),
    ;

    private final String scanType;
    private final String desc;

    /**
     * 优先级
     *
     * 1. 越小的越优先执行
     * 2. 主要用于 {@link CharsScanMatchItem#getStartIndex()} 开始下标相同的场景，排序使用。
     *
     * 其实还有另一种方式指定优先级，根据 scanList 的下标作为优先级，这种的好处是优先级和扫描策略独立，更加灵活。
     * 不过考虑到1次设置order的消耗+scan的优先级一般固定，此处暂时使用接口方式
     * @since 0.7.0
     */
    private final int priority;

    CharsScanTypeEnum(String code, String desc, int priority) {
        this.scanType = code;
        this.desc = desc;
        this.priority = priority;
    }

    @Override
    public String getScanType() {
        return scanType;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "CharsScanTypeEnum{" +
                "scanType='" + scanType + '\'' +
                ", desc='" + desc + '\'' +
                ", priority=" + priority +
                '}';
    }

}
