package com.publiccms.test.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;

@DisplayName("html utils test case")
class HtmlUtilsTest {
    protected final Log log = LogFactory.getLog(getClass());
    
    String html = "<div><p>1234567890<a href=\"http://www.publiccms.com\">123456111111789011223344</a><sCRipt>alert(1);</scRIpt></p><p>1234567890<img src=\"https://www.publiccms.com\"/></p><p>1234567890<a href=\"http://www.publiccms.com\">123456111111789011223344</a></p><p>1234567890<img src=\"https://www.publiccms.com\"/></p></div>";

    @Test
    @DisplayName("html cut")
    void htmlCut() {
        for (int i = 0; i < html.length(); i++) {
            String result = HtmlUtils.keep(html, i);
            log.info(result);
            Assertions.assertTrue(result.length() <= i);
        }
    }

    @Test
    @DisplayName("remove html")
    void removeHtmlTag() {
        for (int i = 0; i < html.length(); i++) {
            String result = HtmlUtils.removeHtmlTag(html);
            Assertions.assertFalse(result.toLowerCase().contains("<p>") || result.toLowerCase().contains("<a>")
                    || result.toLowerCase().contains("<div>"));
        }
    }

    @Test
    @DisplayName("clean safe html")
    void cleanUnsafeHtml() {
        String result = HtmlUtils.cleanUnsafeHtml(html, Constants.BLANK);
        Assertions.assertFalse(result.toLowerCase().contains("script"));
    }
    @Test
    @DisplayName("swap word")
    void swapWord() {
        Map<Character, Character> swapWordMap = new HashMap<>();
        swapWordMap.put('a', 'b');
        swapWordMap.put('p', 'c');
        swapWordMap.put('c', 'p');
        swapWordMap.put('b', 'a');
        Assertions.assertTrue(HtmlUtils.swapWord("publiccms<a href=\"http://www.publiccms.com\">publiccms</a>publiccms", swapWordMap, true).contains("<a"));
        Assertions.assertFalse(HtmlUtils.swapWord("publiccms<a href=\"http://www.publiccms.com\">publiccms</a>publiccms", swapWordMap, false).contains("<a"));
    }

    @Test
    @DisplayName("string cut")
    void stringCut() {
        for (int i = 0; i < html.length(); i++) {
            String result = CommonUtils.keep(html, i);
            Assertions.assertTrue(result.length() <= i);
        }
    }
}
