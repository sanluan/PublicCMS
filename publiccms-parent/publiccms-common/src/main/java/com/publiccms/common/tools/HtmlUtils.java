package com.publiccms.common.tools;

import java.util.regex.Pattern;

import com.publiccms.common.constants.Constants;

/**
 * HtmlUtils
 * 
 */
public class HtmlUtils {
    /**
     * 
     */
    public static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");

    /**
     * @param source
     * @return
     */
    private static String escape(String source) {
        if (source == null) {
            return source;
        }
        StringBuffer buffer = new StringBuffer();
        char c = ' ';
        for (int i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            if (c == '&') {
                if (contains(source, i + 1, "amp;")) {// &amp;
                    buffer.append("&");
                    i += 4;
                } else if (contains(source, i + 1, "quot;")) {// &quot;
                    buffer.append("\"");
                    i += 5;
                } else if (contains(source, i + 1, "nbsp;")) {// &quot;
                    buffer.append(" ");
                    i += 5;
                } else {
                    buffer.append(c);
                }
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param startIndex
     * @param target
     * @return
     */
    private static boolean contains(String source, int startIndex, String target) {
        if (source.length() >= startIndex + target.length()) {
            for (int i = 0; i < target.length(); i++) {
                if (source.charAt(startIndex + i) != target.charAt(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @param string
     * @return result
     */
    public static String removeHtmlTag(String string) {
        if (CommonUtils.notEmpty(string)) {
            return escape(HTML_PATTERN.matcher(string).replaceAll(Constants.BLANK));
        }
        return string;
    }
}
