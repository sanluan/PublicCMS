package com.sanluan.common.handler;

import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class FreeMarkerExtendHandler {
    private String directivePrefix;
    private String directiveRemoveRegex;
    private String methodRemoveRegex;
    private Map<String, Object> freemarkerVariables = new HashMap<String, Object>();
    private final Log log = getLog(getClass());

    @Autowired
    public void init(Map<String, TemplateDirectiveModel> directiveMap, Map<String, TemplateMethodModelEx> methodMap,
            FreeMarkerConfigurer freeMarkerConfigurer) {
        log.info("Freemarker directives and methods Handler started");
        StringBuffer directives = new StringBuffer();
        for (Entry<String, TemplateDirectiveModel> entry : directiveMap.entrySet()) {
            String directiveName = directivePrefix + uncapitalize(entry.getKey().replaceAll(directiveRemoveRegex, ""));
            freemarkerVariables.put(directiveName, entry.getValue());
            if (0 != directives.length()) {
                directives.append(",");
            }
            directives.append(directiveName);
        }

        StringBuffer methods = new StringBuffer();
        for (Entry<String, TemplateMethodModelEx> entry : methodMap.entrySet()) {
            String methodName = uncapitalize(entry.getKey().replaceAll(methodRemoveRegex, ""));
            freemarkerVariables.put(methodName, entry.getValue());
            if (0 != methods.length()) {
                methods.append(",");
            }
            methods.append(methodName);
        }

        try {
            freeMarkerConfigurer.getConfiguration().setAllSharedVariables(
                    new SimpleHash(freemarkerVariables, freeMarkerConfigurer.getConfiguration().getObjectWrapper()));
            log.info((directiveMap.size()) + " directives created:[" + directives.toString() + "];" + methodMap.size()
                    + " methods created:[" + methods.toString() + "]");
        } catch (TemplateModelException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * @param directivePrefix
     *            the directivePrefix to set
     */
    public void setDirectivePrefix(String directivePrefix) {
        this.directivePrefix = directivePrefix;
    }

    /**
     * @param directiveRemoveRegex
     *            the directiveRemoveRegex to set
     */
    public void setDirectiveRemoveRegex(String directiveRemoveRegex) {
        this.directiveRemoveRegex = directiveRemoveRegex;
    }

    /**
     * @param methodRemoveRegex
     *            the methodRemoveRegex to set
     */
    public void setMethodRemoveRegex(String methodRemoveRegex) {
        this.methodRemoveRegex = methodRemoveRegex;
    }

    public Map<String, Object> getFreemarkerVariables() {
        return freemarkerVariables;
    }

    public String getDirectiveRemoveRegex() {
        return directiveRemoveRegex;
    }
}
