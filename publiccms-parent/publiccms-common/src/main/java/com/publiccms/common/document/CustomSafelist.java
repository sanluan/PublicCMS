package com.publiccms.common.document;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class CustomSafelist extends Safelist {
    public static Safelist relaxed() {
        return new CustomSafelist()
                .addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl", "dt",
                        "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small", "span",
                        "strike", "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", "u", "ul")

                .addAttributes("a", "href", "title").addAttributes("blockquote", "cite").addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width").addAttributes("ol", "start", "type")
                .addAttributes("q", "cite").addAttributes("table", "summary", "width")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width")
                .addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope", "width").addAttributes("ul", "type")

                .addProtocols("a", "href", "ftp", "http", "https", "mailto").addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https").addProtocols("img", "src", "http", "https")
                .addProtocols("q", "cite", "http", "https");
    }

    @Override
    protected boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
        return ("img".equals(tagName) && "src".equals(attr.getKey()) || "a".equals(tagName) && "href".equals(attr.getKey())
                || "iframe".equals(tagName) && "src".equals(attr.getKey())) && attr.getValue().startsWith("//")
                || super.isSafeAttribute(tagName, el, attr);
    }
}
