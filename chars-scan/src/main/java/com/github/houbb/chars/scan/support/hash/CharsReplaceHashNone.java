package com.github.houbb.chars.scan.support.hash;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;

/**
 * @since 1.12.0
 */
public class CharsReplaceHashNone implements ICharsReplaceHash {

    @Override
    public void hash(byte[] bytes, CharsScanContext context, StringBuilder builder) {
    }

}
