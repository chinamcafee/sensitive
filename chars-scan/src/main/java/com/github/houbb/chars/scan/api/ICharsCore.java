package com.github.houbb.chars.scan.api;

/**
 * 定义为接口，是为了方便拓展
 *
 * 工具类对用户，接口+实现对研发
 * 
 * @author d
 * @since 1.0.0
 */
public interface ICharsCore {

    /**
     * 扫描并且获取替换结果
     * @param originalString 原始的文本
     * @param context 上下文
     * @return 结果
     */
    String scanAndReplace(String originalString, CharsScanContext context);

}
