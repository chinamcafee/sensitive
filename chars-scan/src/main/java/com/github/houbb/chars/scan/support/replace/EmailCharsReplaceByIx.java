package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.CharsScanMatchItem;
import com.github.houbb.chars.scan.constant.CharsScanConst;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;


/**
 * 身份证替换策略
 *
 * 实际测试下来，性能好像没有优化太多。暂时保留
 *
 * @author d
 * @since 1.24.1
 */
public class EmailCharsReplaceByIx extends AbstractCharReplace {

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.EMAIL.getScanType();
    }

    @Override
    public void doReplace(StringBuilder stringBuilder, char[] chars, CharsScanMatchItem charsScanMatchItem, CharsScanContext context) {
        // 处理的结果
        // [原始部分~掩码开始)
        // [掩码开始~掩码结束]
        // (掩码结束~原始结束]
        int plainLenBeforeAt = 0;

        int startIx = charsScanMatchItem.getStartIndex();
        int endIx = charsScanMatchItem.getEndIndex();
        boolean beforeAtFlag = true;
        for(int i = startIx; i <= endIx; i++) {
            char c = chars[i];
            if(c == '@') {
                beforeAtFlag = false;
            }

            if(beforeAtFlag) {
                plainLenBeforeAt++;
                if(plainLenBeforeAt <= 4) {
                    stringBuilder.append(c);
                } else {
                    stringBuilder.append(CharsScanConst.REPLACE_CHAR);
                }
            } else {
                //@及之后，添加原始信息
                stringBuilder.append(c);
            }
        }
    }

}
