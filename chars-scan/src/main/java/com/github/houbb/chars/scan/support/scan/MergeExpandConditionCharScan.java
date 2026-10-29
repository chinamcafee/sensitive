package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.chars.scan.util.InnerChineseNameUtil;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.lang.CharUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 邮箱
 *
 * 核心思想：因为 @ 一般不多，所以这里首先寻找到 @，然后想两边 expand，直到不是 email 的时候截止。
 *
 * @author d
 * @since 1.24.0
 */
public class MergeExpandConditionCharScan extends AbstractExpandConditionCharScan {

    /**
     * 支持的扫描类别，支持多个之间任意组合
     *
     * @since 1.29.0
     */
    private final Collection<String> scanTypeCollect;

    /**
     * @since 1.29.0
     */
    public MergeExpandConditionCharScan() {
        this(Arrays.asList(CharsScanTypeEnum.EMAIL.getScanType(),
                CharsScanTypeEnum.CHINESE_NAME.getScanType(),
                CharsScanTypeEnum.ADDRESS.getScanType()
        ));
    }

    public MergeExpandConditionCharScan(Collection<String> scanTypeEnums) {
        ArgUtil.notNull(scanTypeEnums, "scanTypeEnums");

        this.scanTypeCollect = scanTypeEnums;
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.MERGE_EXPAND.getPriority();
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.MERGE_EXPAND.getScanType();
    }

    @Override
    protected CharsScanTypeEnum isExpandStartCharCondition(int i, char c, char[] chars, CharsScanContext context) {
        if(scanTypeCollect.contains(CharsScanTypeEnum.EMAIL.getScanType()) && c == '@') {
            return CharsScanTypeEnum.EMAIL;
        }

        if(scanTypeCollect.contains(CharsScanTypeEnum.ADDRESS.getScanType()) && InnerCharUtil.isAddressKeyword(c)) {
            return CharsScanTypeEnum.ADDRESS;
        }

        if(scanTypeCollect.contains(CharsScanTypeEnum.CHINESE_NAME.getScanType()) &&  InnerChineseNameUtil.isFamilyName(c)) {
            return CharsScanTypeEnum.CHINESE_NAME;
        }


        return null;
    }

    @Override
    protected int loopHandleLeft(int i, char[] chars, CharsScanContext context, CharsScanTypeEnum scanTypeEnum) {
        final AbstractExpandConditionCharScan emailExpandConditionCharScan = new EmailExpandConditionCharScan();
        final AbstractExpandConditionCharScan addressExpandConditionCharScan = new AddressExpandConditionCharScan();
        final AbstractExpandConditionCharScan chineseNameExpandConditionCharScan = new ChineseNameExpandConditionCharScan();

        if(CharsScanTypeEnum.EMAIL.equals(scanTypeEnum)) {
            return emailExpandConditionCharScan.loopHandleLeft(i, chars, context, scanTypeEnum);
        } else if(CharsScanTypeEnum.ADDRESS.equals(scanTypeEnum)) {
            return addressExpandConditionCharScan.loopHandleLeft(i, chars, context, scanTypeEnum);
        } else if(CharsScanTypeEnum.CHINESE_NAME.equals(scanTypeEnum)) {
            return chineseNameExpandConditionCharScan.loopHandleLeft(i, chars, context, scanTypeEnum);
        }

        return -1;
    }

    @Override
    protected int loopHandleRight(int leftIx, int i, char[] chars, CharsScanContext context, CharsScanTypeEnum scanTypeEnum) {
        final AbstractExpandConditionCharScan emailExpandConditionCharScan = new EmailExpandConditionCharScan();
        final AbstractExpandConditionCharScan addressExpandConditionCharScan = new AddressExpandConditionCharScan();
        final AbstractExpandConditionCharScan chineseNameExpandConditionCharScan = new ChineseNameExpandConditionCharScan();

        if(CharsScanTypeEnum.EMAIL.equals(scanTypeEnum)) {
            return emailExpandConditionCharScan.loopHandleRight(leftIx, i, chars, context, scanTypeEnum);
        } else if(CharsScanTypeEnum.ADDRESS.equals(scanTypeEnum)) {
            return addressExpandConditionCharScan.loopHandleRight(leftIx, i, chars, context, scanTypeEnum);
        } else if(CharsScanTypeEnum.CHINESE_NAME.equals(scanTypeEnum)) {
            return chineseNameExpandConditionCharScan.loopHandleRight(leftIx, i, chars, context, scanTypeEnum);
        }

        return -1;
    }

}
