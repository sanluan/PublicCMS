package com.publiccms.common.tools;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DateUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 基类 Base
 * 
 */
public class CommonUtils {

    /**
     * 获取日期
     * 
     * Get Date
     * 
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 获取日期
     * 
     * Get Date
     * 
     * @return
     */
    public static Date getMinuteDate() {
        return setSeconds(setMilliseconds(getDate(), 0), 0);
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(String var) {
        return isNotBlank(var);
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(String var) {
        return isBlank(var);
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(long var) {
        return 0 != var;
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(long var) {
        return 0 == var;
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(int var) {
        return 0 != var;
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(int var) {
        return 0 == var;
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(Object var) {
        return null != var;
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(Object var) {
        return null == var;
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(List<?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(List<?> var) {
        return null == var || var.isEmpty();
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(Map<?, ?> var) {
        return null != var && !var.isEmpty();
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(Map<?, ?> var) {
        return null == var || var.isEmpty();
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param file
     * @return
     */
    public static boolean notEmpty(File file) {
        return null != file && file.exists();
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param file
     * @return
     */
    public static boolean empty(File file) {
        return null == file || !file.exists();
    }

    /**
     * 非空
     * 
     * Not Empty
     * 
     * @param var
     * @return
     */
    public static boolean notEmpty(Object[] var) {
        return null != var && 0 < var.length;
    }

    /**
     * 空
     * 
     * Empty
     * 
     * @param var
     * @return
     */
    public static boolean empty(Object[] var) {
        return null == var || 0 == var.length;
    }

}
