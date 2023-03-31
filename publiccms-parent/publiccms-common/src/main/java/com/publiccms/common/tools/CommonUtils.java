package com.publiccms.common.tools;

import java.io.CharArrayWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.util.BitSet;
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
 * CommonUtils 通用Utils
 * 
 */
public class CommonUtils {

    private CommonUtils() {
    }

    private static BitSet dontNeedEncoding;
    private static final int caseDiff = ('a' - 'A');

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' ');
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    /**
     * @param <T>
     * @param <F>
     * @param list
     * @param keyMapper
     * @return
     */
    public static <T, F> Map<F, T> listToMap(List<T> list, Function<T, F> keyMapper) {
        return listToMap(list, keyMapper, null, null);
    }

    /**
     * @param <T>
     * @param <F>
     * @param list
     * @param keyMapper
     * @param consumer
     * @param filter
     * @return
     */
    public static <T, F> Map<F, T> listToMap(List<T> list, Function<T, F> keyMapper, Consumer<T> consumer, Predicate<T> filter) {
        if (null != consumer) {
            list.forEach(consumer);
        }
        Map<F, T> map;
        if (null == filter) {
            map = list.stream().collect(
                    Collectors.toMap(keyMapper, Function.identity(), Constants.defaultMegerFunction(), LinkedHashMap::new));
        } else {
            map = list.stream().filter(filter).collect(
                    Collectors.toMap(keyMapper, Function.identity(), Constants.defaultMegerFunction(), LinkedHashMap::new));
        }

        return map;
    }

    public static String encodeURI(String s) {
        Charset charset = Constants.DEFAULT_CHARSET;
        boolean needToChange = false;
        StringBuilder out = new StringBuilder(s.length());
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        for (int i = 0; i < s.length();) {
            int c = s.charAt(i);
            if (dontNeedEncoding.get(c)) {
                if (c == ' ') {
                    c = '+';
                    needToChange = true;
                }
                out.append((char) c);
                i++;
            } else {
                do {
                    charArrayWriter.write(c);
                    if (c >= 0xD800 && c <= 0xDBFF && (i + 1) < s.length()) {
                        int d = s.charAt(i + 1);
                        if (d >= 0xDC00 && d <= 0xDFFF) {
                            charArrayWriter.write(d);
                            i++;
                        }

                    }
                    i++;
                } while (i < s.length() && !dontNeedEncoding.get((c = s.charAt(i))));

                charArrayWriter.flush();
                String str = new String(charArrayWriter.toCharArray());
                byte[] ba = str.getBytes(charset);
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }
        return (needToChange ? out.toString() : s);
    }

    /**
     * @param elements
     * @return 拼接后的文本
     */
    @SafeVarargs
    public static <T> String joinString(T... elements) {
        if (null == elements || 0 == elements.length) {
            return null;
        } else if (1 == elements.length) {
            return String.valueOf(elements[0]);
        } else {
            StringBuilder sb = new StringBuilder();
            for (T e : elements) {
                if (null != e) {
                    sb.append(e);
                }
            }
            return sb.toString();
        }
    }

    /**
     * @param string
     * @param length
     * @return 截取后的文本
     */
    public static String keep(String string, int length) {
        return keep(string, length, "...");
    }

    /**
     * @param string
     * @param length
     * @param append
     * @return 截取后的文本
     */
    public static String keep(String string, int length, String append) {
        if (null == append) {
            return StringUtils.substring(string, 0, length);
        } else {
            if (null != string && string.length() > length) {
                if (length > append.length()) {
                    return joinString(string.substring(0, length - append.length()), append);
                } else {
                    return string.substring(0, length);
                }
            } else {
                return string;
            }
        }
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
     * @param value
     * @return 是否为非空
     */
    public static boolean notEmpty(String value) {
        return StringUtils.isNotBlank(value);
    }

    /**
     * @param value
     * @return 是否为空
     */
    public static boolean empty(String value) {
        return StringUtils.isBlank(value);
    }

    /**
     * @param value
     * @return 是否非空
     */
    public static boolean notEmpty(Number value) {
        return null != value;
    }

    /**
     * @param value
     * @return 是否为空
     */
    public static boolean empty(Number value) {
        return null == value;
    }

    /**
     * @param value
     * @return 是否非空
     */
    public static boolean notEmpty(Collection<?> value) {
        return null != value && !value.isEmpty();
    }

    /**
     * @param value
     * @return 是否非空
     */
    public static boolean notEmpty(Map<?, ?> value) {
        return null != value && !value.isEmpty();
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
     * @param value
     * @return 是否非空
     */
    public static boolean notEmpty(String[] value) {
        if (null != value && 0 < value.length) {
            for (String t : value) {
                if (notEmpty(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param value
     * @return 是否非空
     */
    public static boolean notEmpty(Object[] value) {
        return null != value && 0 < value.length;
    }

    /**
     * @param value
     * @return 是否为空
     */
    public static boolean empty(Object[] value) {
        return null == value || 0 == value.length;
    }

}
