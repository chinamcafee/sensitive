package com.github.houbb.sensitive.core.support.deepcopy;

import com.github.houbb.sensitive.api.IDeepCopy;
import com.github.houbb.sensitive.core.support.InnerJsonUtil;

/**
 * 基于 Jackson 的深度拷贝实现；保留历史类名以兼容原有 API。
 * @author binbin.hou
 * @since 1.7.1
 */
public class Json2DeepCopy implements IDeepCopy {

    /** 是否启用循环引用检测。 */
    private final boolean cycleDetectionEnabled;

    private static class SingletonHolder {
        private static final Json2DeepCopy DEFAULT_INSTANCE = new Json2DeepCopy(false);
        private static final Json2DeepCopy CYCLE_DETECTION_INSTANCE = new Json2DeepCopy(true);
    }

    public Json2DeepCopy() {
        this(false);
    }

    public Json2DeepCopy(boolean cycleDetectionEnabled) {
        this.cycleDetectionEnabled = cycleDetectionEnabled;
    }

    public static Json2DeepCopy getInstance() {
        return SingletonHolder.DEFAULT_INSTANCE;
    }

    public static Json2DeepCopy getInstance(boolean cycleDetectionEnabled) {
        return cycleDetectionEnabled
                ? SingletonHolder.CYCLE_DETECTION_INSTANCE
                : SingletonHolder.DEFAULT_INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deepCopy(T object) {
        if (object == null) {
            return null;
        }
        String json = InnerJsonUtil.toJson(object);
        return (T) InnerJsonUtil.fromJson(json, object.getClass());
    }
}
