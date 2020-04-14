package com.publiccms.common.tools;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.publiccms.common.constants.Constants;

/**
 * 基类 Base
 * 
 */
public class CommonUtils {

    /**
     * @param <T>
     * @param list
     * @param keyMapper
     * @param consumer
     * @param filter
     * @return
     */
    public static <T> Map<String, T> listToMap(List<T> list, Function<T, String> keyMapper, Consumer<T> consumer,
            Predicate<T> filter) {
        if (null != consumer) {
            list.forEach(consumer);
        }
        Map<String, T> map;
        if (null == filter) {
            map = list.stream().collect(
                    Collectors.toMap(keyMapper, Function.identity(), Constants.defaultMegerFunction(), LinkedHashMap::new));
        } else {
            map = list.stream().filter(filter).collect(
                    Collectors.toMap(keyMapper, Function.identity(), Constants.defaultMegerFunction(), LinkedHashMap::new));
        }

        return map;
    }

    /**
     * @return 当前日期
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * @return 精确到分钟的当前日期
     */
    public static Date getMinuteDate() {
        return DateUtils.ceiling(new Date(), Calendar.MINUTE);
    }

    /**
     * @param var
     * @return 是否为非空
     */
    public static boolean notEmpty(String var) {
        return StringUtils.isNotBlank(var);
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(String var) {
        return StringUtils.isBlank(var);
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Number var) {
        return null != var;
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Number var) {
        return null == var;
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Collection<?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Map<?, ?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * @param file
     * @return 是否非空
     */
    public static boolean notEmpty(File file) {
        return null != file && file.exists();
    }

    /**
     * @param file
     * @return 是否为空
     */
    public static boolean empty(File file) {
        return null == file || !file.exists();
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Object[] var) {
        return null != var && 0 < var.length;
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Object[] var) {
        return null == var || 0 == var.length;
    }

}
