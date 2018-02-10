package com.publiccms.common.tools;

import java.util.regex.Pattern;

import com.publiccms.common.base.Base;

/**
 * HtmlUtils
 * 
 */
public class HtmlUtils implements Base {

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
            return HTML_PATTERN.matcher(string).replaceAll(BLANK);
        }
        return string;
    }
}
