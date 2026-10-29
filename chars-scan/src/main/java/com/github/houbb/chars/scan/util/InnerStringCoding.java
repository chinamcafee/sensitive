package com.github.houbb.chars.scan.util;

import java.nio.charset.Charset;

/**
 * 这个方法 jdk 没有暴露出来，保证兼容性。
 *
 * @since 1.22.0
 */
public class InnerStringCoding {


    /**
     * chars 转 bytes
     * @param cs 编码
     * @param ca 数组
     * @param off 偏移量
     * @param len 长度
     * @return 结果
     */

    public static byte[] encode(Charset cs, char[] ca, int off, int len) {
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < len; i++) {
            stringBuilder.append(ca[off + i]);
        }

        String text = stringBuilder.toString();
        return text.getBytes(cs);
    }

}
