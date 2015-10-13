package com.sanluan.common.tools;

import static org.apache.commons.logging.LogFactory.getLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.ui.ModelMap;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerUtils {
    private final static Log log = getLog(FreeMarkerUtils.class);

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param config
     * @param model
     * @throws IOException
     * @throws TemplateException
     */
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration config,
            Map<String, Object> model) throws IOException, TemplateException {
        makeFileByFile(templateFilePath, destFilePath, config, model, true, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param config
     * @param model
     * @param override
     * @throws IOException
     * @throws TemplateException
     */
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration config,
            Map<String, Object> model, boolean override) throws IOException, TemplateException {
        makeFileByFile(templateFilePath, destFilePath, config, model, override, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param config
     * @param model
     * @param override
     * @param append
     * @throws IOException
     * @throws TemplateException
     */
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration config,
            Map<String, Object> model, boolean override, boolean append) throws IOException, TemplateException {
        Template t = config.getTemplate(templateFilePath);
        File destFile = new File(destFilePath);
        if (override || append || !destFile.exists()) {
            File parent = destFile.getParentFile();
            if (null != parent) {
                parent.mkdirs();
            }
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile, append), "utf-8"));
            t.process(model, out);
            out.close();
            log.info(destFilePath + "    saved！");
        } else {
            log.error(destFilePath + "    already exists！");
        }
    }

    /**
     * @param template
     * @param configuration
     * @return
     */
    public static String makeStringByFile(String template, Configuration configuration) {
        return makeStringByFile(template, configuration, new ModelMap());
    }

    /**
     * @param template
     * @param configuration
     * @param model
     * @return
     */
    public static String makeStringByFile(String template, Configuration configuration, ModelMap model) {
        try {
            Template tpl = configuration.getTemplate(template);
            return FreeMarkerTemplateUtils.processTemplateIntoString(tpl, model);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    /**
     * @param templateContent
     * @param config
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String makeStringByString(String templateContent, Configuration config, Map<String, Object> model)
            throws IOException, TemplateException {
        Template t = new Template(String.valueOf(templateContent.hashCode()), templateContent, config);
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
    }
}
