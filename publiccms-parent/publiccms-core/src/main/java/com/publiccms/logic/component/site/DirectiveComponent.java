package com.publiccms.logic.component.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.base.BaseMethod;
import com.publiccms.common.constants.CommonConstants;

/**
 * 
 * DirectiveComponent 指令处理组件
 *
 */
public class DirectiveComponent {
    private String directiveRemoveRegex;
    private String methodRemoveRegex;
    protected final Log log = LogFactory.getLog(getClass());

    private Map<String, AbstractTemplateDirective> templateDirectiveMap = new HashMap<>();
    private Map<String, AbstractTaskDirective> taskDirectiveMap = new HashMap<>();
    private Map<String, BaseMethod> methodMap = new HashMap<>();

    public String getDirectiveName(String className) {
        return StringUtils.uncapitalize(className.replaceAll(directiveRemoveRegex, CommonConstants.BLANK));
    }

    @Autowired
    private void init(List<AbstractTemplateDirective> templateDirectiveList, List<AbstractTaskDirective> taskDirectiveList,
            List<BaseMethod> methodList) {
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
                method.setName(StringUtils.uncapitalize(method.getClass().getSimpleName().replaceAll(methodRemoveRegex, CommonConstants.BLANK)));
            }
            methodMap.put(method.getName(), method);
        }
        log.info(new StringBuilder().append(methodMap.size()).append(" methods created:").append(methodMap.keySet()).toString());
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
