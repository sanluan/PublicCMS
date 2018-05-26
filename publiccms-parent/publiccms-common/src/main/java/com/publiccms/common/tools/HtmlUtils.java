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
     * @param string
     * @return result
     */
    public static String removeHtmlTag(String string) {
        if (CommonUtils.notEmpty(string)) {
            return HTML_PATTERN.matcher(string).replaceAll(Constants.BLANK);
        }
        return string;
    }
}
