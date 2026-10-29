package com.github.houbb.chars.scan.util;

import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.util.util.DateUtil;
import com.github.houbb.property.bs.PropertyBs;

/**
 * 单独设计的原因：可以让 context 复用。
 *
 * @author d
 * @since 1.0.0
 */
public class CharsScanPropertyHelper {

    /**
     * 配置引导类
     */
    private static final PropertyBs PROPERTY_BS;

    static {
        PROPERTY_BS = buildPropertyBs("chars-scan-config.properties");
    }

    @CommonEager
    private static PropertyBs buildPropertyBs(String path) {
        try {
            return PropertyBs.getInstance(path);
        } catch (Exception e) {
            String logFormat = "WARN: [chars-scan] [%s] [%s] config file not found, use default config.";
            String log = String.format(logFormat, DateUtil.getCurrentDateTimeStr(), path);
            System.out.println(log);
            return PropertyBs.getInstance("chars-scan-config-default.properties");
        }
    }

    /**
     * //    @PluginAttribute(value = "prefix", defaultString = "：‘“，| ,:\"'=") String prefix,
     * //    @PluginAttribute(value = "scanList", defaultString = "1,2,3,4") String scanList,
     * //    @PluginAttribute(value = "replaceList", defaultString = "1,2,3,4") String replaceList,
     * //    @PluginAttribute(value = "defaultReplace", defaultString = "12") String defaultReplace,
     * //    @PluginAttribute(value = "replaceHash", defaultString = "md5") String replaceHash,
     * //    @PluginAttribute(value = "charset", defaultString = "UTF-8") String charset,
     * //    @PluginAttribute(value = "pattern", defaultString = "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n") String pattern,
     * //    @PluginAttribute(value = "whiteList", defaultString = ""
     *
     * @return 结果
     */
    private static CharsScanBs buildCharsScanBs() {
        String prefix = PROPERTY_BS.getOrDefault("chars.scan.prefix", "：‘“，| ,:\\\"'=");
        String scanList = PROPERTY_BS.getOrDefault("chars.scan.scanList", "1,2,3,4,9");
        String replaceList = PROPERTY_BS.getOrDefault("chars.scan.replaceList", "1,2,3,4,9");
        String defaultReplace = PROPERTY_BS.getOrDefault("chars.scan.defaultReplace", "12");
        String replaceHash = PROPERTY_BS.getOrDefault("chars.scan.replaceHash", "md5");
        String whiteList = PROPERTY_BS.getOrDefault("chars.scan.whiteList", "");

        return InnerCharsScanPropertyBuilder.buildCharsScanBs(prefix, scanList, replaceList, defaultReplace, replaceHash, whiteList);
    }

    /**
     * 扫描并且替换
     * @param text 文本
     * @return 结果
     * @since 1.22.0
     */
    public static String scanAndReplace(String text) {
        CharsScanBs charsScanBs = buildCharsScanBs();
        return charsScanBs.scanAndReplace(text);
    }

}
