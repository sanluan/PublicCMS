package com.publiccms.common.tools;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 基类 Base
 * 
 */
public class CommonUtils {

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
        return DateUtils.setSeconds(DateUtils.setMilliseconds(getDate(), 0), 0);
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
    public static boolean notEmpty(Long var) {
        return null != var && 0 != var;
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Long var) {
        return null == var || 0 == var;
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Integer var) {
        return null != var && 0 != var;
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Integer var) {
        return null == var || 0 == var;
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Short var) {
        return null != var && 0 != var;
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Short var) {
        return null == var || 0 == var;
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(List<?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(List<?> var) {
        return null == var || var.isEmpty();
    }

    /**
     * @param var
     * @return 是否非空
     */
    public static boolean notEmpty(Map<?, ?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * @param var
     * @return 是否为空
     */
    public static boolean empty(Map<?, ?> var) {
        return null == var || var.isEmpty();
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
