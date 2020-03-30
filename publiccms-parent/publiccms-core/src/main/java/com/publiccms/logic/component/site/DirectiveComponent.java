package com.publiccms.logic.component.site;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.logic.component.template.NoCacheDirective;
import com.publiccms.logic.component.template.TemplateCacheComponent;
import com.publiccms.logic.component.template.TemplateComponent;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;

/**
 * 
 * DirectiveComponent 指令处理组件
 *
 */
public class DirectiveComponent {
    private String directiveRemoveRegex;
    private String methodRemoveRegex;
    private String directivePrefix;
    protected final Log log = LogFactory.getLog(getClass());

    private Map<String, AbstractTemplateDirective> templateDirectiveMap = new HashMap<>();
    private Map<String, AbstractTaskDirective> taskDirectiveMap = new HashMap<>();
    private Map<String, BaseMethod> methodMap = new HashMap<>();

    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private SiteComponent siteComponent;

    public String getDirectiveName(String className) {
        return StringUtils.uncapitalize(className.replaceAll(directiveRemoveRegex, CommonConstants.BLANK));
    }

    @Autowired
    private void init(FreeMarkerConfigurer freeMarkerConfigurer, List<AbstractTemplateDirective> templateDirectiveList,
            List<AbstractTaskDirective> taskDirectiveList, List<BaseMethod> methodList)
            throws TemplateModelException, IOException {
        for (AbstractTemplateDirective directive : templateDirectiveList) {
            if (null == directive.getName()) {
                directive.setName(getDirectiveName(directive.getClass().getSimpleName()));
            }
            templateDirectiveMap.put(directive.getName(), directive);
        }
        log.info(new StringBuilder().append(templateDirectiveMap.size()).append(" template directives created:")
                .append(templateDirectiveMap.keySet()).toString());
        for (AbstractTaskDirective directive : taskDirectiveList) {
            if (null == directive.getName()) {
                directive.setName(getDirectiveName(directive.getClass().getSimpleName()));
            }
            taskDirectiveMap.put(directive.getName(), directive);
        }
        log.info(new StringBuilder().append(taskDirectiveMap.size()).append(" task directives created:")
                .append(taskDirectiveMap.keySet()).toString());
        for (BaseMethod method : methodList) {
            if (null == method.getName()) {
                method.setName(StringUtils
                        .uncapitalize(method.getClass().getSimpleName().replaceAll(methodRemoveRegex, CommonConstants.BLANK)));
            }
            methodMap.put(method.getName(), method);
        }
        log.info(new StringBuilder().append(methodMap.size()).append(" methods created:").append(methodMap.keySet()).toString());
        initTemplateComponent(freeMarkerConfigurer, directivePrefix);
    }

    private void initTemplateComponent(FreeMarkerConfigurer freeMarkerConfigurer, String directivePrefix)
            throws IOException, TemplateModelException {
        Map<String, Object> freemarkerVariables = new HashMap<>();
        Configuration adminConfiguration = freeMarkerConfigurer.getConfiguration();
        for (Entry<String, AbstractTemplateDirective> entry : getTemplateDirectiveMap().entrySet()) {
            freemarkerVariables.put(directivePrefix + entry.getKey(), entry.getValue());
        }
        freemarkerVariables.putAll(methodMap);
        adminConfiguration.setAllSharedVariables(new SimpleHash(freemarkerVariables, adminConfiguration.getObjectWrapper()));
        templateComponent.setAdminConfiguration(adminConfiguration);
        Configuration webConfiguration = new Configuration(Configuration.getVersion());
        File webFile = new File(siteComponent.getWebTemplateFilePath());
        webFile.mkdirs();
        webConfiguration.setDirectoryForTemplateLoading(webFile);
        copyConfig(adminConfiguration, webConfiguration);
        Map<String, Object> webFreemarkerVariables = new HashMap<>(freemarkerVariables);
        webFreemarkerVariables.put(TemplateCacheComponent.CONTENT_CACHE, new NoCacheDirective());
        webConfiguration.setAllSharedVariables(new SimpleHash(webFreemarkerVariables, webConfiguration.getObjectWrapper()));
        templateComponent.setWebConfiguration(webConfiguration);

        Configuration taskConfiguration = new Configuration(Configuration.getVersion());
        File taskFile = new File(siteComponent.getTaskTemplateFilePath());
        taskFile.mkdirs();
        taskConfiguration.setDirectoryForTemplateLoading(taskFile);
        copyConfig(adminConfiguration, taskConfiguration);
        for (Entry<String, AbstractTaskDirective> entry : taskDirectiveMap.entrySet()) {
            freemarkerVariables.put(directivePrefix + entry.getKey(), entry.getValue());
        }
        taskConfiguration.setAllSharedVariables(new SimpleHash(freemarkerVariables, taskConfiguration.getObjectWrapper()));
        templateComponent.setTaskConfiguration(taskConfiguration);
    }

    private static void copyConfig(Configuration source, Configuration target) {
        target.setNewBuiltinClassResolver(source.getNewBuiltinClassResolver());
        target.setTemplateUpdateDelayMilliseconds(source.getTemplateUpdateDelayMilliseconds());
        target.setDefaultEncoding(source.getDefaultEncoding());
        target.setLocale(source.getLocale());
        target.setBooleanFormat(source.getBooleanFormat());
        target.setDateTimeFormat(source.getDateTimeFormat());
        target.setDateFormat(source.getDateFormat());
        target.setTimeFormat(source.getTimeFormat());
        target.setShowErrorTips(source.getShowErrorTips());
        target.setNumberFormat(source.getNumberFormat());
        target.setOutputFormat(source.getOutputFormat());
        target.setURLEscapingCharset(source.getURLEscapingCharset());
        target.setLazyAutoImports(source.getLazyAutoImports());
        target.setTemplateExceptionHandler(source.getTemplateExceptionHandler());
        target.setLogTemplateExceptions(source.getLogTemplateExceptions());
    }

    /**
     * @param directiveRemoveRegex
     */
    public void setDirectiveRemoveRegex(String directiveRemoveRegex) {
        this.directiveRemoveRegex = directiveRemoveRegex;
    }

    /**
     * @param methodRemoveRegex
     */
    public void setMethodRemoveRegex(String methodRemoveRegex) {
        this.methodRemoveRegex = methodRemoveRegex;
    }

    public void setDirectivePrefix(String directivePrefix) {
        this.directivePrefix = directivePrefix;
    }

    /**
     * @return template directive map
     */
    public Map<String, AbstractTemplateDirective> getTemplateDirectiveMap() {
        return templateDirectiveMap;
    }

    /**
     * @return task directive map
     */
    public Map<String, AbstractTaskDirective> getTaskDirectiveMap() {
        return taskDirectiveMap;
    }

    /**
     * @return method map
     */
    public Map<String, BaseMethod> getMethodMap() {
        return methodMap;
    }

}
