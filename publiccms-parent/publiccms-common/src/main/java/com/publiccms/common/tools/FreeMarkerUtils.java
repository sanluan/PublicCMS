package com.publiccms.common.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.constants.Constants;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarker工具类
 * 
 * FreeMarkerUtils
 * 
 */
public class FreeMarkerUtils {
    private static final Log log = LogFactory.getLog(FreeMarkerUtils.class);

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @throws IOException
     * @throws TemplateException
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model) throws IOException, TemplateException {
        generateFileByFile(templateFilePath, destFilePath, configuration, model, true, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @param override
     * @throws IOException
     * @throws TemplateException
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model, boolean override) throws IOException, TemplateException {
        generateFileByFile(templateFilePath, destFilePath, configuration, model, override, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @param override
     * @param append
     * @throws IOException
     * @throws TemplateException
     */
    public static void generateFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model, boolean override, boolean append) throws IOException, TemplateException {
        Template t = configuration.getTemplate(templateFilePath);
        Path destPath = Paths.get(destFilePath);
        if (override || append || !Files.exists(destPath)) {
            Path parent = destPath.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (OutputStream outputStream = append
                    ? Files.newOutputStream(Paths.get(destFilePath), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
                    : Files.newOutputStream(Paths.get(destFilePath))) {
                Writer out = new OutputStreamWriter(outputStream, Constants.DEFAULT_CHARSET);
                t.process(model, out);
                log.info(CommonUtils.joinString(destFilePath, " saved!"));
            }
        } else {
            log.error(CommonUtils.joinString(destFilePath, " already exists!"));
        }
    }

    /**
     * @param template
     * @param configuration
     * @return render result
     * @throws TemplateException
     * @throws IOException
     */
    public static String generateStringByFile(String template, Configuration configuration)
            throws IOException, TemplateException {
        Map<String, Object> model = Collections.emptyMap();
        return generateStringByFile(template, configuration, model);
    }

    /**
     * @param template
     * @param configuration
     * @param model
     * @return render result
     * @throws IOException
     * @throws TemplateException
     */
    public static String generateStringByFile(String template, Configuration configuration, Map<String, Object> model)
            throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        generateStringByFile(writer, template, configuration, model);
        return writer.toString();
    }

    /**
     * @param writer
     * @param template
     * @param configuration
     * @param model
     * @throws IOException
     * @throws TemplateException
     */
    public static void generateStringByFile(Writer writer, String template, Configuration configuration,
            Map<String, Object> model) throws IOException, TemplateException {
        Template tpl = configuration.getTemplate(template);
        tpl.process(model, writer);
    }

    /**
     * @param templateContent
     * @param configuration
     * @param model
     * @return render result
     * @throws IOException
     * @throws TemplateException
     */
    public static String generateStringByString(String templateContent, Configuration configuration, Map<String, Object> model)
            throws IOException, TemplateException {
        Template tpl = new Template(null, templateContent, configuration);
        StringWriter writer = new StringWriter();
        tpl.process(model, writer);
        return writer.toString();
    }
}
