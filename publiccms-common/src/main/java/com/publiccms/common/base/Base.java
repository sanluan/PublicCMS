package com.publiccms.common.base;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;

/**
 * 基类
 * Base
 * 
 */
public abstract class Base {
    /**
     * 随机数
     * 
     * Random
     */
    public static final Random r = new Random();
    /**
     * 默认字符编码名称
     * 
     * Default CharSet Name
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    /**
     * 默认字符编码
     * 
     * Default CharSet
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    /**
     * 间隔符
     * 
     * separator
     */
    public static final String SEPARATOR = "/";
    /**
     * 空白字符串
     * 
     * blank
     */
    public static final String BLANK = "";
    /**
     * 点
     * 
     * dot
     */
    public static final String DOT = ".";
    /**
     * 空格
     * 
     * blank space
     */
    public static final String BLANK_SPACE = " ";
    /**
     * 逗号分隔符
     * 
     * comma delimited
     */
    public static final String COMMA_DELIMITED = ",";
    protected final Log log = getLog(getClass());

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
