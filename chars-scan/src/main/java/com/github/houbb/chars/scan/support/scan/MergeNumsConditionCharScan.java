package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerBankLuhnHelper;
import com.github.houbb.chars.scan.util.InnerBufferUtil;
import com.github.houbb.chars.scan.util.InnerIdNoHelper;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.*;

/**
 * 合并的操作类。
 *
 * 思想：数字的处理事类似的。主要考虑长度问题，所以尝试合并为一次是否可以更快？
 *
 * @author d
 * @since 1.25.0
 */
public class MergeNumsConditionCharScan extends AbstractConditionCharScan {

    /**
     * 支持的扫描类别，支持多个之间任意组合
     *
     * @since 1.29.0
     */
    private final Collection<String> scanTypeCollect;

    /**
     * @since 1.29.0
     */
    public MergeNumsConditionCharScan() {
        this(Arrays.asList(CharsScanTypeEnum.PHONE.getScanType(),
                CharsScanTypeEnum.BANK_CARD.getScanType(),
                CharsScanTypeEnum.ID_NO.getScanType()
        ));
    }

    public MergeNumsConditionCharScan(Collection<String> scanTypeEnums) {
        ArgUtil.notNull(scanTypeEnums, "scanTypeEnums");

        this.scanTypeCollect = scanTypeEnums;
    }

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isNumber(c);
    }

    @Override
    protected void addItemWhenStringMatch(int i, char c, char[] chars, CharsScanContext context) {
        // 长度判断
        StringBuilder buffer = super.getBuffer();
        int bufLen = buffer.length();

        //1. 11位手机号
        if(bufLen == 11) {
            boolean isPhone = scanTypeCollect.contains(CharsScanTypeEnum.PHONE.getScanType()) && InnerBufferUtil.isPhone(buffer);

            if(isPhone) {
                addMatchItemForMerge(CharsScanTypeEnum.PHONE, i, chars, context);
            }
        } else if(bufLen == 15
            || bufLen == 16
            || bufLen == 19) {
            //2. 15 16 19 的银行卡
            boolean isBankNo = scanTypeCollect.contains(CharsScanTypeEnum.BANK_CARD.getScanType()) && InnerBankLuhnHelper.isValidBankNo(buffer);
            if(isBankNo) {
                super.addMatchItemForMerge(CharsScanTypeEnum.BANK_CARD, i, chars, context);
            }
        } else if(bufLen == 18) {
            // 判断是否为身份证
            boolean isIdNoFlag = scanTypeCollect.contains(CharsScanTypeEnum.ID_NO.getScanType()) && InnerIdNoHelper.isValidIdNo(buffer);
            if(isIdNoFlag) {
                super.addMatchItemForMerge(CharsScanTypeEnum.ID_NO, i, chars, context);
            }
        } else if(bufLen == 17) {
            //3.1 额外考虑一下身份证
            int nextCharIx = i+1;
            if(nextCharIx < chars.length) {
                char nextChar = chars[nextCharIx];
                if(nextChar == 'x' || nextChar == 'X') {
                    buffer.append(nextChar);

                    // 判断是否为身份证
                    boolean isIdNoFlag = scanTypeCollect.contains(CharsScanTypeEnum.ID_NO.getScanType()) && InnerIdNoHelper.isValidIdNo(buffer);
                    if(isIdNoFlag) {
                        super.addMatchItemForMerge(CharsScanTypeEnum.ID_NO, i, chars, context);
                        return;
                    }
                }
            }

            //3.2 是否为银行
            //2. 15 16 19 的银行卡
            boolean isBankNo = scanTypeCollect.contains(CharsScanTypeEnum.BANK_CARD.getScanType()) && InnerBankLuhnHelper.isValidBankNo(buffer);
            if(isBankNo) {
                super.addMatchItemForMerge(CharsScanTypeEnum.BANK_CARD, i, chars, context);
            }
        }
    }

    @Override
    protected boolean isSupportMergeScanType(CharsScanTypeEnum scanTypeEnum, int i, char[] chars, CharsScanContext context) {
        return scanTypeCollect.contains(scanTypeEnum.getScanType());
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.MERGE_NUMS.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.MERGE_NUMS.getPriority();
    }

}
