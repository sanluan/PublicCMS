package com.publiccms.common.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;
import org.apache.commons.text.translate.NumericEntityUnescaper;

import com.publiccms.common.constants.Constants;

/**
 * HtmlUtils
 * 
 */
public class HtmlUtils {
    public static final Map<CharSequence, CharSequence> BASIC_ESCAPE;
    static {
        final Map<CharSequence, CharSequence> initialMap = new HashMap<>();
        initialMap.put("\"", "&quot;"); // " - double-quote
        initialMap.put("&", "&amp;"); // & - ampersand
        BASIC_ESCAPE = Collections.unmodifiableMap(initialMap);
    }
    public static final Map<CharSequence, CharSequence> BASIC_UNESCAPE;
    static {
        BASIC_UNESCAPE = Collections.unmodifiableMap(EntityArrays.invert(BASIC_ESCAPE));
    }
    public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(BASIC_UNESCAPE),
            new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE),
            new NumericEntityUnescaper());
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
            return UNESCAPE_HTML4.translate(HTML_PATTERN.matcher(string).replaceAll(Constants.BLANK));
        }
        return string;
    }
}
