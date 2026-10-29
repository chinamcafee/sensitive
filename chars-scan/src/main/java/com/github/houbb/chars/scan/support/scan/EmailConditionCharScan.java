package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.List;

/**
 * 邮箱
 *
 * @author d
 * @since 1.0.0
 */
public class EmailConditionCharScan extends AbstractConditionCharScan {

    @Override
    protected boolean isCharMatchCondition(int i, char c, char[] chars) {
        return CharUtil.isEmilChar(c);
    }

    /**
     * AT 符号前面的字符串必须包含至少一个字符，最后，AT 符号后面的字符串必须包含至少一个字符，以及一个句点，并且句点后面必须有至少两个字符。
     *
     * @param i 下标
     * @param c 字符
     * @param chars 原始数组
     * @return 是否
     */
    @Override
    protected boolean isStringMatchCondition(int i, char c, char[] chars, final CharsScanContext context) {
        String string = super.getBuffer().toString();

        //TODO: 直接用正则匹配也可以
        // 可以考虑直接使用邮箱判断
        List<String> subArray = StringUtil.splitToList(string, '@');

        // 要求有且只有一个@
        if(subArray.size() != 2) {
            return false;
        }

        // 或者自己的判断

        //@前必须包含一个字符
        String beforeAt = subArray.get(0);
        if(beforeAt.length() < 1) {
            return false;
        }

        //@后面必须是一个域名
        //@符号后面的字符串必须包含至少一个字符，以及一个句点，并且句点后面必须有至少两个字符
        // 也就是要求后面必须4位及以上
        String afterAt = subArray.get(1);
        if(afterAt.length() < 4) {
            return false;
        }
        // 必须包含一个句点
        // TODO... 包含一个什么符号，拆分为单个列表
        List<String> domainList = StringUtil.splitToList(afterAt, '.');
        if(domainList.size() < 2) {
            return false;
        }
        // 至少包含一个字符
        String domainFirst = domainList.get(0);
        if(domainFirst.length() < 1) {
            return false;
        }
        //句点后面必须有至少两个字符
        String domainLast = domainList.get(domainList.size()-1);
        if(domainLast.length() < 2) {
            return false;
        }

        return true;
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.EMAIL.getScanType();
    }

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.EMAIL.getPriority();
    }

}
