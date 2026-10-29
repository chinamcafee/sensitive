package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.chars.scan.util.InnerBankLuhnHelper;
import com.github.houbb.chars.scan.util.InnerBufferUtil;
import com.github.houbb.chars.scan.util.InnerCharUtil;
import com.github.houbb.chars.scan.util.InnerIdNoHelper;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 合并的操作类。
 *
 * 思想：地名+人名强制要求前 N 个是中文。不过地名要复杂一些。
 *
 * @since 1.29.0 开始废弃，这个方法合并的不够彻底。 {@link MergeExpandConditionCharScan} 用这个
 * @author d
 * @since 1.26.0
 */
@Deprecated
public class MergeNameAddressConditionCharScan extends AbstractConditionCharScan {

    /**
     * 名称最大长度
     */
    private static final int MAX_NAME_LIMIT = 3;

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        //名称前N个必须是中文
        int len = getBuffer().length();
        if(len < MAX_NAME_LIMIT) {
            return CharUtil.isChinese(c);
        }

        // 地址的后续判断，允许数字
        return InnerCharUtil.isAddressChar(c);
    }

    @Override
    protected void addItemWhenStringMatch(int i, char c, char[] chars, CharsScanContext context) {
        // 长度判断
        StringBuilder buffer = super.getBuffer();
        int bufLen = buffer.length();

        if(bufLen < 2) {
            return;
        }

        // 1. name
        if(bufLen <= MAX_NAME_LIMIT) {
            if(InnerBufferUtil.isChineseName(buffer)) {
                super.addMatchItemForMerge(CharsScanTypeEnum.CHINESE_NAME, i, chars, context);
            }
            return;
        } else if(bufLen >= 6 && bufLen < 100) {
            //2. address
            if(InnerBufferUtil.isAddress(buffer)) {
                super.addMatchItemForMerge(CharsScanTypeEnum.ADDRESS, i, chars, context);
            }
            // 是否需要处理包含中文名，但是不包含地址的场景？
            // 暂时先不处理
        }

    }



    @Override
    public String getScanType() {
        return CharsScanTypeEnum.MERGE_NAME_ADDRESS.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.MERGE_NAME_ADDRESS.getPriority();
    }

}
