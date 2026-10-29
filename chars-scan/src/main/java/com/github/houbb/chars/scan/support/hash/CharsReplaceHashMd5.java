package com.github.houbb.chars.scan.support.hash;

import com.github.houbb.chars.scan.api.CharsScanContext;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.heaven.util.codec.Base64;
import com.github.houbb.heaven.util.util.ArrayPrimitiveUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @since 1.12.0
 */
public class CharsReplaceHashMd5 implements ICharsReplaceHash {

    @Override
    public void hash(byte[] bytes, CharsScanContext context, StringBuilder builder) {
        byte[] outBytes = md5(bytes);

        if(ArrayPrimitiveUtil.isNotEmpty(outBytes)) {
            int size = outBytes.length;

            builder.append('|');

            for(int i = 0; i < size; ++i) {
                byte b = outBytes[i];
                builder.append(byteToHex(b));
            }
        }
    }

    private static String byteToHex(byte b) {
        int n = b;
        if (b < 0) {
            n = b + 256;
        }

        int d1 = n / 16;
        int d2 = n % 16;
        String charOne = String.valueOf(Base64.HEX_ARRAY[d1]);
        String charTwo = String.valueOf(Base64.HEX_ARRAY[d2]);
        return charOne + charTwo;
    }

    private static byte[] md5(byte[] bytes) {
        if (ArrayPrimitiveUtil.isEmpty(bytes)) {
            return null;
        } else {
            MessageDigest messageDigest = null;

            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var3) {
                throw new RuntimeException(var3);
            }

            return messageDigest.digest(bytes);
        }
    }

}
