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
     * full date format
     */
    public static final String FULL_DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    /**
     * download file name format
     */
    public static final String DOWNLOAD_FORMAT_STRING = "-yyyy-MM-dd-HH-mm";
    /**
     * file name format
     */
    public static final String FILE_NAME_FORMAT_STRING = "yyyy-MM-dd_HH-mm-ssSSSS";
    /**
     * upload file name format
     */
    public static final String UPLOAD_FILE_NAME_FORMAT_STRING = "yyyy/MM-dd/HH-mm-ssSSSS";

    /**
     * short date format
     */
    public static final String SHORT_DATE_FORMAT_STRING = "yyyy-MM-dd";
    /**
     * short date format length
     */
    public static final int FULL_DATE_LENGTH = FULL_DATE_FORMAT_STRING.length();
    /**
     * short date format length
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
            format = map.computeIfAbsent(pattern, SimpleDateFormat::new);
        }
        return format;
    }
}
