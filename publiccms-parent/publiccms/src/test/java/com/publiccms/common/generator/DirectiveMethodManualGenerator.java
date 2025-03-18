package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.directive.Directive;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.JavaDocUtils;
import com.publiccms.common.tools.ScanClassUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateMethodModelEx;

/**
 *
 * SourceMaker 代码生成工具
 *
 */
public class DirectiveMethodManualGenerator {
    protected static final Log log = LogFactory.getLog(DirectiveMethodManualGenerator.class);
    /**
    *
    */
    public static final String DOC_PATH = "../../doc/";
    public static final String DEFAULT_NAMESPACE = "cms";
    public static final String directiveRemoveRegex = "Cms|Directive";
    public static final String methodRemoveRegex = "Method";
    public static final Map<String, Map<String, String>> messages = new HashMap<>();

    /**
     * @param arg
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] arg) throws ClassNotFoundException, IOException {
        DirectiveMethodManualGenerator generator = new DirectiveMethodManualGenerator();
        String basePackage = "com.publiccms.views";// 基础包名
        {
            Map<String, String> message = new HashMap<>();
            message.put("zh", "zh-Hans");
            message.put("ja", "ja");
            message.put("en", "en");
            messages.put("lang", message);
        }
        {
            Map<String, String> message = new HashMap<>();
            message.put("zh", "指令与函数手册");
            message.put("ja", "指令および方法マニュアル");
            message.put("en", "Directive and Method Manual");
            messages.put("title", message);
        }
        {
            Map<String, String> message = new HashMap<>();
            message.put("zh", "指令");
            message.put("ja", "指令");
            message.put("en", "Directive");
            messages.put("directive", message);
        }
        {
            Map<String, String> message = new HashMap<>();
            message.put("zh", "函数");
            message.put("ja", "方法");
            message.put("en", "Method");
            messages.put("method", message);
        }
        {
            Map<String, String> message = new HashMap<>();
            message.put("zh", "命名空间");
            message.put("ja", "名前空間");
            message.put("en", "namespace");
            messages.put("namespace", message);
        }
        generator.generate(basePackage, "directive", "method");
    }

    /**
     * 生成某个实体类的代码
     *
     * @param basePackage
     * @param directivePackage
     * @param methodPackage
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void generate(String basePackage, String directivePackage, String methodPackage) throws ClassNotFoundException, IOException {
        String directiveFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, directivePackage);
        Map<String, Object> model = new HashMap<>();
        Map<String, Map<String, Map<String, String>>> namespaceMap = new HashMap<>();
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { directiveFullPackage })) {
            if (Directive.class.isAssignableFrom(c)) {
                String namespace = getDirectiveNamespace(c);
                Map<String, Map<String, String>> directiveMap = namespaceMap.computeIfAbsent(namespace, k -> new TreeMap<>(Comparable::compareTo));
                Map<String, String> zh = directiveMap.computeIfAbsent("zh", k -> new TreeMap<>(Comparable::compareTo));
                Map<String, String> en = directiveMap.computeIfAbsent("en", k -> new TreeMap<>(Comparable::compareTo));
                Map<String, String> ja = directiveMap.computeIfAbsent("ja", k -> new TreeMap<>(Comparable::compareTo));
                String shortName = getDirectiveShortName(namespace, c.getSimpleName());
                String comment = JavaDocUtils.getClassComment(c.getName());
                zh.put(shortName, filterHtml(comment, "zh"));
                en.put(shortName, filterHtml(comment, "en"));
                ja.put(shortName, filterHtml(comment, "ja"));
            }
        }
        model.put("namespace", namespaceMap);
        String methodFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, methodPackage);
        Map<String, Map<String, String>> methodMap = new HashMap<>();
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { methodFullPackage })) {
            if (TemplateMethodModelEx.class.isAssignableFrom(c)) {
                Map<String, String> zh = methodMap.computeIfAbsent("zh", k -> new TreeMap<>(Comparable::compareTo));
                Map<String, String> en = methodMap.computeIfAbsent("en", k -> new TreeMap<>(Comparable::compareTo));
                Map<String, String> ja = methodMap.computeIfAbsent("ja", k -> new TreeMap<>(Comparable::compareTo));
                String name = StringUtils.uncapitalize(c.getSimpleName().replaceAll(methodRemoveRegex, Constants.BLANK));
                String comment = JavaDocUtils.getClassComment(c.getName());
                zh.put(name, filterHtml(comment, "zh"));
                en.put(name, filterHtml(comment, "en"));
                ja.put(name, filterHtml(comment, "ja"));
            }
        }
        model.put("methodMap", methodMap);
        model.put("version", CmsVersion.getVersion().substring(CmsVersion.getVersion().lastIndexOf(".") + 1));
        model.put("messages", messages);
        try {
            model.put("lang", "zh");
            FreeMarkerUtils.generateFileByFile("template.html", CommonUtils.joinString(DOC_PATH, "PublicCMS 指令与函数手册.html"), config, model, true);
            model.put("lang", "en");
            FreeMarkerUtils.generateFileByFile("template.html", CommonUtils.joinString(DOC_PATH, "PublicCMS Directive and Method Manual.html"), config, model, true);
            model.put("lang", "ja");
            FreeMarkerUtils.generateFileByFile("template.html", CommonUtils.joinString(DOC_PATH, "PublicCMS 指令および方法マニュアル.html"), config, model, true);
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (TemplateException e) {
            log.info(e.getMessage());
        }
    }

    private static String filterHtml(String comment, String lang) {
        if (comment == null) {
            return "";
        }
        Document document = Jsoup.parse(comment);
        Element body = document.body();
        body.getElementsByAttribute("lang").forEach(e -> {
            if (e.hasAttr("lang") && !lang.equalsIgnoreCase(e.attr("lang"))) {
                e.remove();
            }
        });
        return body.html();
    }

    private String getDirectiveNamespace(Class<?> clazz) {
        String packagename = clazz.getPackage().getName();
        if (packagename.contains(Constants.DOT)) {
            return packagename.substring(packagename.lastIndexOf(Constants.DOT) + 1);
        }
        return DEFAULT_NAMESPACE;
    }

    private String getDirectiveShortName(String namespace, String className) {
        if (className.toLowerCase().startsWith(namespace)) {
            return StringUtils.uncapitalize(className.substring(namespace.length(), className.length()).replaceAll(directiveRemoveRegex, Constants.BLANK));
        }
        return StringUtils.uncapitalize(className.replaceAll(directiveRemoveRegex, Constants.BLANK));
    }

    private Configuration config;

    /**
     * @throws IOException
     *
     */
    public DirectiveMethodManualGenerator() throws IOException {
        config = new freemarker.template.Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDirectoryForTemplateLoading(new File("src/test/resources/generator/doc/"));
        config.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

}
