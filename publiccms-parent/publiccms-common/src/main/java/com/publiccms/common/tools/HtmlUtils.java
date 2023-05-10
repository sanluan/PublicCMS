package com.publiccms.common.tools;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;

import com.publiccms.common.constants.Constants;

/**
 * HtmlUtils
 * 
 */
public class HtmlUtils {
    private HtmlUtils() {
    }

    /**
     * 
     */
    public static final Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
    public static final Pattern SRC_HREF_PATTERN = Pattern
            .compile("<(a|A|img|IMG)\\s+[^>]*(href|HREF|src|SRC)=(\"|\')([^\"\']*)(\"|\')[^>]*>");

    public static final Safelist SAFELIST = Safelist.relaxed()
            .addTags("abbr", "address", "aside", "article", "bdi", "bdo", "big", "center", "del", "details", "dfn", "figcaption",
                    "figure", "font", "footer", "header", "hr", "iframe", "ins", "kbd", "label", "main", "mark", "nav",
                    "progress", "s", "samp", "section", "summary", "time", "var", "wbr")
            .addAttributes(":all", "class", "dir", "lang", "style").addAttributes("a", "name", "download", "id", "target")
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

    public static void getFileList(String html, Set<String> set) {
        if (CommonUtils.notEmpty(html) && null != set) {
            Matcher matcher = SRC_HREF_PATTERN.matcher(html);
            while (matcher.find()) {
                set.add(matcher.group(4));
            }
        }
    }

    public static String keep(String string, int length) {
        if (CommonUtils.notEmpty(string) && string.length() > length) {
            Document document = Jsoup.parse(string);
            Element body = document.body();
            Document cloneDocument = document.shallowClone();
            cloneDocument.outputSettings().prettyPrint(false);
            Element clone = body.shallowClone();
            cloneDocument.appendChild(clone);
            clone(clone, body.childNodes(), new AtomicInteger(length));
            return clone.html();
        } else {
            return string;
        }
    }

    public static String cleanUnsafeHtml(String string, String baseUri) {
        if (CommonUtils.notEmpty(string)) {
            if (CommonUtils.notEmpty(baseUri) && baseUri.startsWith("//")) {
                baseUri = CommonUtils.joinString("http:", baseUri);
            }
            Document dirty = Jsoup.parseBodyFragment(string, baseUri);
            Cleaner cleaner = new Cleaner(SAFELIST);
            Document clean = cleaner.clean(dirty);
            clean.outputSettings().prettyPrint(false);
            return clean.body().html();
        }
        return string;
    }

    private static Element clone(Element element, AtomicInteger counter) {
        Element clone = element.shallowClone();
        int thisLength = clone.outerHtml().length();
        if (0 < counter.addAndGet(-thisLength)) {
            clone(clone, element.childNodes(), counter);
            return clone;
        } else {
            return null;
        }
    }

    private static void clone(Element cloneParent, List<Node> nodeList, AtomicInteger counter) {
        for (int i = 0; i < nodeList.size(); i++) {
            Node current = nodeList.get(i);
            if (current instanceof Element) {
                Element child = clone((Element) current, counter);
                if (null != child) {
                    cloneParent.appendChild(child);
                }
                if (0 > counter.get()) {
                    break;
                }
            } else {
                int thisLength = current.outerHtml().length();
                counter.addAndGet(-thisLength);
                if (0 <= counter.get()) {
                    cloneParent.appendChild(current.shallowClone());
                } else {
                    if (current instanceof Comment && -7 > counter.get()) {
                        cloneParent.appendChild(
                                new Comment(CommonUtils.keep(((Comment) current).getData(), -7 - counter.get(), null)));
                    } else if (current instanceof TextNode) {
                        cloneParent.appendChild(
                                new TextNode(CommonUtils.keep(((TextNode) current).text(), thisLength + counter.get(), null)));
                    }
                    break;
                }
            }
        }
    }

}