package com.github.houbb.chars.scan.constant;

/**
 * @since 1.15.0
 */
public enum CharsScanReplaceHashTypeEnum {
    MD5("md5"),
    NONE("none");

    private final String id;

    CharsScanReplaceHashTypeEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
