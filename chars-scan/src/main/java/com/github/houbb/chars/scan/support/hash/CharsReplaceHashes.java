package com.github.houbb.chars.scan.support.hash;

import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.constant.CharsScanReplaceHashTypeEnum;
import com.github.houbb.chars.scan.exception.CharsScanException;

/**
 * @since 1.12.0
 */
public final class CharsReplaceHashes {

    private static final ICharsReplaceHash MD5 = new CharsReplaceHashMd5();

    private static final ICharsReplaceHash NONE = new CharsReplaceHashNone();

    public static ICharsReplaceHash md5() {
        return MD5;
    }

    public static ICharsReplaceHash none() {
        return NONE;
    }

    /**
     * 默认策略
     * @return 策略
     * @since 1.14.0
     */
    public static ICharsReplaceHash defaults() {
        return MD5;
    }

    /**
     * @param idCode 编码
     * @return 实现
     * @since 1.15.0
     */
    public static ICharsReplaceHash newInstance(String idCode) {
        String lowerCode = idCode.toLowerCase();

        if(CharsScanReplaceHashTypeEnum.MD5.getId().equals(lowerCode)) {
            return md5();
        }

        if(CharsScanReplaceHashTypeEnum.NONE.getId().equals(lowerCode)) {
            return none();
        }

        throw new CharsScanException(idCode + " match hash not found!");
    }

}
