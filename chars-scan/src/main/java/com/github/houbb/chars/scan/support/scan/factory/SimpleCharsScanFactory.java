package com.github.houbb.chars.scan.support.scan.factory;

import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.api.ICharsScan;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.exception.CharsScanException;
import com.github.houbb.chars.scan.support.scan.*;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: 实现策略
 *
 * @author d
 * @since 1.13.0
 */
public class SimpleCharsScanFactory extends AbstractCharsScanFactory implements ICharsScanFactory {

    private final List<String> scanTypeList;

    public SimpleCharsScanFactory(List<String> scanTypeList) {
        this.scanTypeList = scanTypeList;
    }

    public SimpleCharsScanFactory() {
        this(Arrays.asList(
                //1,2,3
                CharsScanTypeEnum.MERGE_NUMS.getScanType(),
                //4,5,9
                CharsScanTypeEnum.MERGE_EXPAND.getScanType(),

                CharsScanTypeEnum.IPV4.getScanType(),
                CharsScanTypeEnum.PASSPORT.getScanType(),
                CharsScanTypeEnum.GPS.getScanType()
        ));
    }

    /**
     * 构建合并的数字
     *
     * 后续可能会调整，所以使用封装的方法，而不是把细节暴露给用户。
     *
     * @since 1.29.0
     * @param numScanTypes 具体的类别
     * @return 结果
     */
    public static String buildMergeNums(Collection<String> numScanTypes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CharsScanTypeEnum.MERGE_NUMS.getScanType())
                        .append(":");

        String scanTypes = StringUtil.join(numScanTypes, "&");
        stringBuilder.append(scanTypes);
        return stringBuilder.toString();
    }

    /**
     * 构建合并的数字
     *
     * 后续可能会调整，所以使用封装的方法，而不是把细节暴露给用户。
     *
     * @since 1.29.0
     * @param scanTypesCollect 具体的类别
     * @return 结果
     */
    public static String buildMergeExpands(Collection<String> scanTypesCollect) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(CharsScanTypeEnum.MERGE_EXPAND.getScanType())
                .append(":");

        String scanTypes = StringUtil.join(scanTypesCollect, "&");
        stringBuilder.append(scanTypes);
        return stringBuilder.toString();
    }

    /**
     * 获取合并的参数
     * @param scanTypes 扫描类别
     * @return 结果
     * @since 1.29.0
     */
    public static List<String> getMergeParams(String[] scanTypes) {
        if(scanTypes.length <= 1) {
            return Collections.emptyList();
        }

        String paramsRaw = scanTypes[1];
        return StringUtil.splitToList(paramsRaw, '&');
    }

    @Override
    public List<String> scanTypeList() {
        return scanTypeList;
    }

    @Override
    public ICharsScan getCharScan(String scanTypeRaw) {
        // 拆分
        String[] scanTypes = scanTypeRaw.split(":");
        String scanType = scanTypes[0];
        List<String> mergeParams = getMergeParams(scanTypes);

        // 这里使用反射也可以，但是反射的性能会比直接调用差一些。
        // switch(String) 需要 jdk 版本较高，暂时不适用
        //1,2,3 三合一
        if(CharsScanTypeEnum.MERGE_NUMS.getScanType().equals(scanType)) {
            if(CollectionUtil.isNotEmpty(mergeParams)) {
                return new MergeNumsConditionCharScan(mergeParams);
            }
            return new MergeNumsConditionCharScan();
        }
        //expand 合并
        if(CharsScanTypeEnum.MERGE_EXPAND.getScanType().equals(scanType)) {
            if(CollectionUtil.isNotEmpty(mergeParams)) {
                return new MergeExpandConditionCharScan(mergeParams);
            }
            return new MergeExpandConditionCharScan();
        }
        //name+address 合并
        if(CharsScanTypeEnum.MERGE_NAME_ADDRESS.getScanType().equals(scanType)) {
            return new MergeNameAddressConditionCharScan();
        }

        //expand 策略
        if(CharsScanTypeEnum.EMAIL.getScanType().equals(scanType)) {
            return new EmailExpandConditionCharScan();
        }
        if(CharsScanTypeEnum.ADDRESS.getScanType().equals(scanType)) {
            return new AddressExpandConditionCharScan();
        }
        if(CharsScanTypeEnum.CHINESE_NAME.getScanType().equals(scanType)) {
            return new ChineseNameExpandConditionCharScan();
        }

        if(CharsScanTypeEnum.ID_NO.getScanType().equals(scanType)) {
            return new IdNoConditionCharScan();
        }
        if(CharsScanTypeEnum.PHONE.getScanType().equals(scanType)) {
            return new PhoneConditionCharScan();
        }
        if(CharsScanTypeEnum.BANK_CARD.getScanType().equals(scanType)) {
            return new BankCardConditionCharScan();
        }

        if(CharsScanTypeEnum.GPS.getScanType().equals(scanType)) {
            return new GPSConditionCharScan();
        }
        if(CharsScanTypeEnum.IPV4.getScanType().equals(scanType)) {
            return new IPV4ConditionCharScan();
        }

        if(CharsScanTypeEnum.PASSPORT.getScanType().equals(scanType)) {
            return new PassportConditionCharScan();
        }

        if(CharsScanTypeEnum.BIRTHDAY.getScanType().equals(scanType)) {
            return new BirthdayConditionCharScan();
        }

        throw new CharsScanException("Not support type for " + scanType);
    }

}
