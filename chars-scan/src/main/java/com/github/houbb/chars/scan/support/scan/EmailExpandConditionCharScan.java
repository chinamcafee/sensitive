package com.github.houbb.chars.scan.support.scan;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.constant.CharsScanTypeEnum;

/**
 * 邮箱
 *
 * 核心思想：因为 @ 一般不多，所以这里首先寻找到 @，然后想两边 expand，直到不是 email 的时候截止。
 *
 * @author d
 * @since 1.23.0
 */
public class EmailExpandConditionCharScan extends AbstractExpandConditionCharScan {

    @Override
    public int getPriority() {
        return CharsScanTypeEnum.EMAIL.getPriority();
    }

    @Override
    public String getScanType() {
        return CharsScanTypeEnum.EMAIL.getScanType();
    }

    @Override
    protected CharsScanTypeEnum isExpandStartCharCondition(int i, char c, char[] chars, CharsScanContext context) {
        if(c == '@') {
            return CharsScanTypeEnum.EMAIL;
        }

        return null;
    }

    @Override
    protected boolean isLeftExpandMatchCondition(int leftIx, char leftChar, char[] chars, CharsScanContext context) {
        return isEmilChar(leftChar);
    }

    @Override
    protected boolean isRightExpandMatchCondition(int rightIx, char rightChar, char[] chars, CharsScanContext context) {
        return isEmilChar(rightChar);
    }

    //x@y.z
    @Override
    protected boolean isLeftBufferMatch(int middleIx, int startIx, char[] chars, CharsScanContext context) {
        int len = middleIx - startIx;
        if(len < 1 || len > 32) {
            return false;
        }

        return true;
    }


    @Override
    protected int loopHandleRight(int leftIx, int i, char[] chars, CharsScanContext context, CharsScanTypeEnum scanTypeEnum) {
        StringBuilder buffer = super.getBuffer();

        int rightIx = i+1;

        // 最后的 . 的位置
        int lastPointIx = -1;

        // 向右边
        for(rightIx = i+1; rightIx < chars.length; rightIx++) {
            char cc = chars[rightIx];

            // 是否为 .
            if('.' == cc) {
                lastPointIx = rightIx;
            }

            // 满足，添加
            if(isRightExpandMatchCondition(rightIx, cc, chars, context)) {
                buffer.append(cc);
            } else {
                break;
            }
        }

        // 判断 . 的位置
        // AT 符号后面的字符串必须包含至少一个字符，以及一个句点，并且句点后面必须有至少两个字符。
        if(lastPointIx < 0) {
            return -1;
        }

        // 判断是否为符合条件的值
        // 判断右边是否满足，右边需要-1
        int actualRightIx = rightIx - 1;
        if(!isRightBufferMatch(i, leftIx, actualRightIx, chars, context)) {
            return -1;
        }

        // 并且句点后面必须有至少两个字符。
        int differPoint = actualRightIx - lastPointIx;
        if(differPoint < 2) {
            return -1;
        }

        return actualRightIx;
    }

    //x@y.z
    @Override
    protected boolean isRightBufferMatch(int middleIx, int startIx, int endIx, char[] chars, CharsScanContext context) {
        int len = endIx - middleIx;
        if(len < 3 || len > 32) {
            return false;
        }

        return true;
    }

    private static boolean isDigitOrLetter(char c) {
        return Character.isDigit(c) || Character.isLowerCase(c) || Character.isUpperCase(c);
    }

    private static boolean isEmilChar(char c) {
        return isDigitOrLetter(c) || '_' == c || '-' == c || c == '.';
    }

}
