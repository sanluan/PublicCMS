package com.publiccms.common.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * DateFormatUtil
 *
 */
public class DateFormatUtils {
    private static ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<>();
    /**
     * 
     */
    public static final String FULL_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    /**
     * 
     */
    public static final String SHORT_DATE_FORMAT_STRING = "yyyy-MM-dd";
    /**
     * 
     */
    public static final int FULL_DATE_LENGTH = FULL_DATE_FORMAT_STRING.length();
    /**
     * 
     */
    public static final int SHORT_DATE_LENGTH = SHORT_DATE_FORMAT_STRING.length();

    /**
     * @param pattern
     * @return date format
     */
    public static DateFormat getDateFormat(String pattern) {
        Map<String, DateFormat> map = threadLocal.get();
        DateFormat format = null;
        if (null == map) {
            map = new HashMap<>();
            format = new SimpleDateFormat(pattern);
            map.put(pattern, format);
            threadLocal.set(map);
        } else {
            format = map.get(pattern);
            if (null == format) {
                format = new SimpleDateFormat(pattern);
                map.put(pattern, format);
            }
        }
        return format;
    }
}
