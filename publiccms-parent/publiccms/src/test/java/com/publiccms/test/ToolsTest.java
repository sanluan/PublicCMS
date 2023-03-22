package com.publiccms.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.HtmlUtils;

@DisplayName("tools test case")
public class ToolsTest {
    String html = "<div><p>1234567890<a href=\"http://www.publiccms.com\">123456111111789011223344</a><sCRipt>alert(1);</scRIpt></p><p>1234567890<img src=\"https://www.publiccms.com\"/></p><p>1234567890<a href=\"http://www.publiccms.com\">123456111111789011223344</a></p><p>1234567890<img src=\"https://www.publiccms.com\"/></p></div>";

    @Test
    @DisplayName("html cut")
    public void htmlCut() {
        for (int i = 0; i < html.length(); i++) {
            String result = HtmlUtils.keep(html, i);
            System.out.println(result);
            Assertions.assertTrue(result.length() <= i);
        }
    }

    @Test
    @DisplayName("remove html")
    public void removeHtmlTag() {
        for (int i = 0; i < html.length(); i++) {
            String result = HtmlUtils.removeHtmlTag(html);
            Assertions.assertFalse(result.toLowerCase().contains("<p>") || result.toLowerCase().contains("<a>")
                    || result.toLowerCase().contains("<div>"));
        }
    }

    @Test
    @DisplayName("clean safe html")
    public void cleanUnsafeHtml() {
        String result = HtmlUtils.cleanUnsafeHtml(html, CommonConstants.BLANK);
        Assertions.assertFalse(result.toLowerCase().contains("script"));
    }

    @Test
    @DisplayName("string cut")
    public void stringCut() {
        for (int i = 0; i < html.length(); i++) {
            String result = CommonUtils.keep(html, i);
            Assertions.assertTrue(result.length() <= i);
        }
    }
}
