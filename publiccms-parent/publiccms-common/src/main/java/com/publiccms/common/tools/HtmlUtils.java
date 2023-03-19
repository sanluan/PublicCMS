package com.publiccms.common.tools;

import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

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

    public static final Safelist SAFELIST = Safelist.relaxed()
            .addTags("abbr", "address", "aside", "article", "bdi", "bdo", "big", "center", "del", "details", "dfn", "figcaption",
                    "figure", "font", "footer", "header", "hr", "iframe", "ins", "kbd", "label", "main", "mark", "nav",
                    "progress", "s", "samp", "section", "summary", "time", "var", "wbr")
            .addAttributes(":all", "class", "dir", "lang", "style").addAttributes("a", "name", "id", "target")
            .addAttributes("audio", "autoplay", "controls", "loop", "muted", "preload", "src")
            .addAttributes("iframe", "src", "width", "height").addAttributes("meter", "max", "min", "value")
            .addAttributes("progress", "max", "value")
            .addAttributes("video", "autoplay", "controls", "data-setup", "height", "loop", "muted", "preload", "poster", "src",
                    "width")
            .addAttributes("source", "media", "sizes", "src", "srcset", "type")
            .addAttributes("track", "default", "kind", "label", "src", "srclang").addProtocols("a", "href", "#")
            .addProtocols("img", "src", "data").addProtocols("iframe", "src", "http", "https").preserveRelativeLinks(true);

    /**
     * @param string
     * @return result
     */
    public static String removeHtmlTag(String string) {
        if (CommonUtils.notEmpty(string)) {
            return StringEscapeUtils.unescapeHtml4(HTML_PATTERN.matcher(string).replaceAll(Constants.BLANK));
        }
        return string;
    }

    public static String cleanUnsafeHtml(String string, String baseUri) {
        if (CommonUtils.notEmpty(string)) {
            if (CommonUtils.notEmpty(baseUri) && baseUri.startsWith("//")) {
                baseUri = CommonUtils.joinString("http:", baseUri);
            }
            return Jsoup.clean(string, baseUri, SAFELIST);
        }
        return string;
    }
}
