package com.sanluan.common.base;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.Date;

import org.apache.commons.logging.Log;

public abstract class Base {
    protected final Log log = getLog(getClass());

    public static Date getDate() {
        return new Date();
    }

    public static Date tomorrow(Date date) {
        return addDays(date, 1);
    }

    public static boolean notEmpty(String var) {
        return isNotBlank(var);
    }

    public static boolean notEmpty(Object var) {
        return null != var;
    }

    public static boolean notEmpty(Object[] var) {
        return null != var && 0 < var.length;
    }
}
