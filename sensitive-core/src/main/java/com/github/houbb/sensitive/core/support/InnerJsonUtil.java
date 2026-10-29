package com.github.houbb.sensitive.core.support;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * 内部 JSON 工具类（Jackson 3 实现）。
 * @since 1.7.1
 */
public final class InnerJsonUtil {

    private static final ObjectMapper MAPPER = JsonMapper.builder().build();

    private InnerJsonUtil() {
    }

    /**
     * 对象转 JSON 字符串。
     * @param object 对象
     * @return JSON 字符串
     */
    public static String toJson(Object object) {
        return MAPPER.writeValueAsString(object);
    }

    /**
     * JSON 字符串转对象。
     * @param json JSON
     * @param type 目标类型
     * @return 对象
     * @param <T> 泛型
     */
    public static <T> T fromJson(String json, Class<T> type) {
        return MAPPER.readValue(json, type);
    }
}
