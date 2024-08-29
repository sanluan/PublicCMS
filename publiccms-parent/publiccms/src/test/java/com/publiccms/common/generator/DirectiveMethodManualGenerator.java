package com.publiccms.common.generator;

import static com.publiccms.common.constants.Constants.DEFAULT_CHARSET_NAME;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    /**
     * @param arg
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] arg) throws ClassNotFoundException, IOException {
        DirectiveMethodManualGenerator generator = new DirectiveMethodManualGenerator();
        String basePackage = "com.publiccms.views";// 基础包名
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
    public void generate(String basePackage, String directivePackage, String methodPackage)
            throws ClassNotFoundException, IOException {
        String directiveFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, directivePackage);
        Map<String, Object> model = new HashMap<>();
        Map<String, Map<String, String>> namespaceMap = new TreeMap<>(Comparable::compareTo);
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { directiveFullPackage })) {
            if (Directive.class.isAssignableFrom(c)) {
                String namespace = getDirectiveNamespace(c);
                Map<String, String> directiveMap = namespaceMap.computeIfAbsent(namespace,
                        k -> new TreeMap<>(Comparable::compareTo));
                directiveMap.put(getDirectiveShortName(namespace, c.getSimpleName()), JavaDocUtils.getClassComment(c.getName()));
                namespaceMap.put(namespace, directiveMap);
            }
        }
        model.put("namespace", namespaceMap);
        String methodFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, methodPackage);
        Map<String, String> method = new TreeMap<>(Comparable::compareTo);
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { methodFullPackage })) {
            if (TemplateMethodModelEx.class.isAssignableFrom(c)) {
                String name = StringUtils.uncapitalize(c.getSimpleName().replaceAll(methodRemoveRegex, Constants.BLANK));
                method.put(name, JavaDocUtils.getClassComment(c.getName()));
            }
        }
        model.put("methodMap", method);
        try {
            FreeMarkerUtils.generateFileByFile("template.html",
                    CommonUtils.joinString(DOC_PATH, "PublicCMS Directive and Method Manual.html"), config, model, true);
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (TemplateException e) {
            log.info(e.getMessage());
        }
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
            return StringUtils.uncapitalize(className.substring(namespace.length(), className.length())
                    .replaceAll(directiveRemoveRegex, Constants.BLANK));
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
        config.setDefaultEncoding(DEFAULT_CHARSET_NAME);
    }

}
