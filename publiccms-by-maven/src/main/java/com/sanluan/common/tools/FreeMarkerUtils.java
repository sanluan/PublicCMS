package com.sanluan.common.tools;

import static org.apache.commons.logging.LogFactory.getLog;
import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.ui.ModelMap;

import com.sanluan.common.base.Base;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarker工具类
 * 
 * @author kerneler
 *
 */
public class FreeMarkerUtils extends Base {
    private final static Log log = getLog(FreeMarkerUtils.class);

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param config
     * @param model
     * @throws IOException
     * @throws TemplateException
     */
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model) throws IOException, TemplateException {
        makeFileByFile(templateFilePath, destFilePath, configuration, model, true, false);
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
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model, boolean override) throws IOException, TemplateException {
        makeFileByFile(templateFilePath, destFilePath, configuration, model, override, false);
    }

    /**
     * @param templateFilePath
     * @param destFilePath
     * @param configuration
     * @param model
     * @param override
     * @param append
     * @throws ParseException
     * @throws MalformedTemplateNameException
     * @throws IOException
     * @throws TemplateException
     */
    public static void makeFileByFile(String templateFilePath, String destFilePath, Configuration configuration,
            Map<String, Object> model, boolean override, boolean append)
            throws MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Template t = configuration.getTemplate(templateFilePath);
        File destFile = new File(destFilePath);
        if (override || append || !destFile.exists()) {
            File parent = destFile.getParentFile();
            if (null != parent) {
                parent.mkdirs();
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(destFile, append);
                Writer out = new BufferedWriter(new OutputStreamWriter(outputStream, DEFAULT_CHARSET));
                t.process(model, out);
                out.close();
            } finally {
                try {
                    if (notEmpty(outputStream)) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    throw e;
                }
            }
            log.info(destFilePath + "    saved!");
        } else {
            log.error(destFilePath + "    already exists!");
        }
    }

    /**
     * @param template
     * @param configurationuration
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public static String makeStringByFile(String template, Configuration configuration) throws IOException, TemplateException {
        return makeStringByFile(template, configuration, new ModelMap());
    }

    /**
     * @param template
     * @param configuration
     * @param model
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public static String makeStringByFile(String template, Configuration configuration, Map<String, Object> model)
            throws IOException, TemplateException {
        Template tpl = configuration.getTemplate(template);
        return processTemplateIntoString(tpl, model);
    }

    /**
     * @param templateContent
     * @param configuration
     * @param model
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public static String makeStringByString(String templateContent, Configuration configuration, Map<String, Object> model)
            throws IOException, TemplateException {
        Template t = new Template(String.valueOf(templateContent.hashCode()), templateContent, configuration);
        return processTemplateIntoString(t, model);
    }
}
