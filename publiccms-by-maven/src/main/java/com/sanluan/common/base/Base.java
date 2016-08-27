package com.sanluan.common.base;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.logging.LogFactory.getLog;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;

public abstract class Base {
    public static final Random r = new Random();
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String SEPARATOR = "/";
    public static final String BLANK = "";
    public static final String BLANK_SPACE = " ";
    public static final String COMMA_DELIMITED = ",";
    protected final Log log = getLog(getClass());

    public static Date getDate() {
        return new Date();
    }

    public static boolean notEmpty(String var) {
        return isNotBlank(var);
    }

    public static boolean empty(String var) {
        return isBlank(var);
    }

    public static boolean notEmpty(Object var) {
        return null != var;
    }

    public static boolean notEmpty(List<?> var) {
        return null != var && !var.isEmpty();
    }

    public static boolean notEmpty(Map<?, ?> var) {
        return null != var && !var.isEmpty();
    }

    public static boolean empty(List<?> var) {
        return null == var || var.isEmpty();
    }

    public static boolean empty(Map<?, ?> var) {
        return null == var || var.isEmpty();
    }

    public static boolean empty(Object var) {
        return null == var;
    }

    public static boolean empty(File file) {
        return null == file || !file.exists();
    }

    public static boolean notEmpty(File file) {
        return null != file && file.exists();
    }

    public static boolean empty(Object[] var) {
        return null == var || 0 == var.length;
    }

    public static boolean notEmpty(Object[] var) {
        return null != var && 0 < var.length;
    }
}
