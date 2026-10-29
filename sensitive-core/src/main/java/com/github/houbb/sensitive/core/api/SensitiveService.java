package com.github.houbb.sensitive.core.api;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.support.cache.impl.ClassFieldListCache;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassTypeUtil;
import com.github.houbb.heaven.util.lang.reflect.ClassUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;
import com.github.houbb.sensitive.annotation.Sensitive;
import com.github.houbb.sensitive.annotation.metadata.SensitiveCondition;
import com.github.houbb.sensitive.annotation.metadata.SensitiveStrategy;
import com.github.houbb.sensitive.api.*;
import com.github.houbb.sensitive.api.impl.SensitiveStrategyBuiltIn;
import com.github.houbb.sensitive.core.api.context.SensitiveContext;
import com.github.houbb.sensitive.core.exception.SensitiveRuntimeException;
import com.github.houbb.sensitive.core.support.InnerJsonUtil;
import com.github.houbb.sensitive.core.util.entry.SensitiveEntryUtil;
import com.github.houbb.sensitive.core.util.strategy.SensitiveStrategyBuiltInUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * 脱敏服务实现类
 * <p>
 * [反射处理数组](https://blog.csdn.net/snakemoving/article/details/54287681)
 *
 * @author binbin.hou
 * @since 0.0.1
 * date 2018/12/29
 */
@ThreadSafe
public class SensitiveService<T> implements ISensitive<T> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T desCopy(T object, final ISensitiveConfig config) {
        return doDesCopy(object, config, false);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T doDesCopy(T object, final ISensitiveConfig config, final boolean maskMapEntries) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }

        //1. 初始化对象
        final Class clazz = object.getClass();
        final SensitiveContext context = SensitiveContext.newInstance();

        //2. 深度复制对象
        final IDeepCopy deepCopy = config.deepCopy();
        final T copyObject = deepCopy.deepCopy(object);

        //3. 处理
        context.setSensitiveConfig(config);
        Set<Object> visited = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());
        handleClassField(context, copyObject, clazz, visited, maskMapEntries);
        return copyObject;
    }
    @Override
    public String desJson(final T object, final ISensitiveConfig config) {
        if (ObjectUtil.isNull(object)) {
            return InnerJsonUtil.toJson(null);
        }
        return InnerJsonUtil.toJson(doDesCopy(object, config, true));
    }

    /**
     * 处理脱敏相关信息
     *
     * @param context    执行上下文
     * @param copyObject 拷贝的新对象
     * @param clazz      class 类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void handleClassField(final SensitiveContext context,
                                  final Object copyObject,
                                  final Class clazz,
                                  final Set<Object> visited,
                                  final boolean maskMapEntries) {
        if (ObjectUtil.isNull(copyObject)
                || ObjectUtil.isNull(clazz)
                || !visited.add(copyObject)) {
            return;
        }

        // JDK 9+ 强封装不允许反射 java.lang.String 等内部字段，基础类型/JDK 类型无需继续遍历。
        if (ClassTypeUtil.isJdk(clazz) || ClassTypeUtil.isBase(clazz) || clazz.isEnum()) {
            return;
        }

        // 每一个实体对应的字段，只对当前 clazz 生效。
        List<Field> fieldList = ClassFieldListCache.getInstance().get(clazz);
        context.setAllFieldList(fieldList);
        context.setCurrentObject(copyObject);

        try {
            for (Field field : fieldList) {
                // 设置当前处理的字段
                final Class fieldTypeClass = field.getType();
                context.setCurrentField(field);

                // 处理 @SensitiveEntry 注解
                if (SensitiveEntryUtil.hasSensitiveEntry(field)) {
                    if (ClassTypeUtil.isJavaBean(fieldTypeClass)) {
                        // 为普通 javabean 对象
                        final Object fieldNewObject = field.get(copyObject);
                        if (ObjectUtil.isNotNull(fieldNewObject)) {
                            handleClassField(context, fieldNewObject, fieldNewObject.getClass(), visited, maskMapEntries);
                        }
                    } else if (ClassTypeUtil.isArray(fieldTypeClass)) {
                        // 为数组类型
                        Object array = field.get(copyObject);
                        if (ObjectUtil.isNotNull(array)) {
                            final int arrayLength = Array.getLength(array);
                            for (int i = 0; i < arrayLength; i++) {
                                Object entry = Array.get(array, i);
                                if (ObjectUtil.isNull(entry)) {
                                    continue;
                                }

                                Class entryClass = entry.getClass();
                                if (needHandleEntryType(entryClass)) {
                                    handleClassField(context, entry, entryClass, visited, maskMapEntries);
                                } else {
                                    Object result = handleSensitiveEntry(context, entry, field);
                                    Array.set(array, i, result);
                                }
                            }
                        }
                    } else if (ClassTypeUtil.isCollection(fieldTypeClass)) {
                        // Collection 接口的子类
                        final Collection<Object> entryCollection = (Collection<Object>) field.get(copyObject);
                        if (ObjectUtil.isNotNull(entryCollection) && !entryCollection.isEmpty()) {
                            List<Object> newResultList = new ArrayList<>(entryCollection.size());
                            for (Object entry : entryCollection) {
                                if (ObjectUtil.isNull(entry)) {
                                    newResultList.add(null);
                                    continue;
                                }

                                Class entryClass = entry.getClass();
                                if (needHandleEntryType(entryClass)) {
                                    handleClassField(context, entry, entryClass, visited, maskMapEntries);
                                    newResultList.add(entry);
                                } else {
                                    newResultList.add(handleSensitiveEntry(context, entry, field));
                                }
                            }

                            // 保留原集合类型（例如 Set），避免以 ArrayList 覆盖后类型不兼容或语义丢失。
                            entryCollection.clear();
                            entryCollection.addAll(newResultList);
                        }
                    } else if (maskMapEntries && ClassTypeUtil.isMap(fieldTypeClass)) {
                        // JSON 输出时递归脱敏 Map 的 JavaBean 值；desCopy 保持原有不处理 Map 的语义。
                        java.util.Map<Object, Object> entryMap =
                                (java.util.Map<Object, Object>) field.get(copyObject);
                        if (ObjectUtil.isNotNull(entryMap)) {
                            for (Object value : entryMap.values()) {
                                if (ObjectUtil.isNotNull(value) && needHandleEntryType(value.getClass())) {
                                    handleClassField(context, value, value.getClass(), visited, maskMapEntries);
                                }
                            }
                        }
                    } else {
                        // 常见基本类型或其他类型，按单字段策略处理。
                        handleSensitive(context, copyObject, field);
                    }
                } else {
                    handleSensitive(context, copyObject, field);
                }
            }

        } catch (IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 处理需脱敏的单个对象
     * <p>
     * 1. 为了简化操作，所有的自定义注解使用多个，不生效。
     * 2. 生效顺序如下：
     * （1）Sensitive
     * （2）系统内置自定义注解
     * （3）用户自定义注解
     *
     * @param context 上下文
     * @param entry   明细
     * @param field   字段信息
     * @return 处理后的信息
     * @since 0.0.2
     */
    private Object handleSensitiveEntry(final SensitiveContext context,
                                        final Object entry,
                                        final Field field) {
        try {
            //处理 @Sensitive
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (ObjectUtil.isNotNull(sensitive)) {
                Class<? extends ICondition> conditionClass = sensitive.condition();
                ICondition condition = conditionClass.newInstance();
                if (condition.valid(context)) {
                    Class<? extends IStrategy> strategyClass = sensitive.strategy();
                    IStrategy strategy = strategyClass.newInstance();
                    return strategy.des(entry, context);
                }
            }

            // 获取所有的注解
            Annotation[] annotations = field.getAnnotations();
            if (ArrayUtil.isNotEmpty(annotations)) {
                ICondition condition = getCondition(annotations);
                if (ObjectUtil.isNull(condition)
                        || condition.valid(context)) {
                    IStrategy strategy = getStrategy(annotations);
                    if (ObjectUtil.isNotNull(strategy)) {
                        return strategy.des(entry, context);
                    }
                }
            }
            return entry;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }


    /**
     * 处理脱敏信息
     *
     * @param context    上下文
     * @param copyObject 复制的对象
     * @param field      当前字段
     * @since 0.0.2
     */
    private void handleSensitive(final SensitiveContext context,
                                 final Object copyObject,
                                 final Field field) {
        try {
            //处理 @Sensitive
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                Class<? extends ICondition> conditionClass = sensitive.condition();
                ICondition condition = conditionClass.newInstance();
                if (condition.valid(context)) {
                    Class<? extends IStrategy> strategyClass = sensitive.strategy();
                    IStrategy strategy = strategyClass.newInstance();
                    final Object originalFieldVal = field.get(copyObject);
                    final Object result = strategy.des(originalFieldVal, context);
                    field.set(copyObject, result);
                }
            }

            // 系统内置自定义注解的处理,获取所有的注解
            Annotation[] annotations = field.getAnnotations();
            if (ArrayUtil.isNotEmpty(annotations)) {
                ICondition condition = getCondition(annotations);
                if (ObjectUtil.isNull(condition)
                        || condition.valid(context)) {
                    IStrategy strategy = getStrategy(annotations);
                    if (ObjectUtil.isNotNull(strategy)) {
                        final Object originalFieldVal = field.get(copyObject);
                        final Object result = strategy.des(originalFieldVal, context);
                        field.set(copyObject, result);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SensitiveRuntimeException(e);
        }
    }

    /**
     * 获取策略
     *
     * @param annotations 字段对应注解
     * @return 策略
     */
    private IStrategy getStrategy(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveStrategy sensitiveStrategy = annotation.annotationType().getAnnotation(SensitiveStrategy.class);
            if (ObjectUtil.isNotNull(sensitiveStrategy)) {
                Class<? extends IStrategy> clazz = sensitiveStrategy.value();
                if (SensitiveStrategyBuiltIn.class.equals(clazz)) {
                    return SensitiveStrategyBuiltInUtil.require(annotation.annotationType());
                } else {
                    return ClassUtil.newInstance(clazz);
                }
            }
        }
        return null;
    }

    /**
     * 获取用户自定义条件
     *
     * @param annotations 字段上的注解
     * @return 对应的用户自定义条件
     */
    private ICondition getCondition(final Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            SensitiveCondition sensitiveCondition = annotation.annotationType().getAnnotation(SensitiveCondition.class);
            if (ObjectUtil.isNotNull(sensitiveCondition)) {
                Class<? extends ICondition> customClass = sensitiveCondition.value();
                return ClassUtil.newInstance(customClass);
            }
        }
        return null;
    }


    /**
     * 需要特殊处理的列表/对象类型
     *
     * @param fieldTypeClass 字段类型
     * @return 是否
     * @since 0.0.2
     */
    private boolean needHandleEntryType(final Class fieldTypeClass) {
        if (ClassTypeUtil.isBase(fieldTypeClass)
                || ClassTypeUtil.isMap(fieldTypeClass)) {
            return false;
        }

        if (ClassTypeUtil.isJavaBean(fieldTypeClass)
                || ClassTypeUtil.isArray(fieldTypeClass)
                || ClassTypeUtil.isCollection(fieldTypeClass)) {
            return true;
        }
        return false;
    }

}
