package com.publiccms.logic.component.site;

import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.common.base.AbstractTaskDirective;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.base.Base;
import com.sanluan.common.base.BaseMethod;

/**
 * 
 * DirectiveComponent 指令处理组件
 *
 */
public class DirectiveComponent extends Base {
    private String directiveRemoveRegex;
    private String methodRemoveRegex;

    private Map<String, AbstractTemplateDirective> templateDirectiveMap = new HashMap<String, AbstractTemplateDirective>();
    private Map<String, AbstractTaskDirective> taskDirectiveMap = new HashMap<String, AbstractTaskDirective>();
    private Map<String, AbstractAppDirective> appDirectiveMap = new HashMap<String, AbstractAppDirective>();
    private Map<String, BaseMethod> methodMap = new HashMap<String, BaseMethod>();

    @Autowired
    private void init(List<AbstractTemplateDirective> templateDirectiveList, List<AbstractTaskDirective> taskDirectiveList,
            List<AbstractAppDirective> appDirectiveList, List<BaseMethod> methodList) {
        for (AbstractTemplateDirective directive : templateDirectiveList) {
            if (null == directive.getName()) {
                directive.setName(uncapitalize(directive.getClass().getSimpleName().replaceAll(directiveRemoveRegex, BLANK)));
            }
            templateDirectiveMap.put(directive.getName(), directive);
        }
        log.info(new StringBuilder().append(templateDirectiveMap.size()).append(" template directives created:")
                .append(templateDirectiveMap.keySet()).toString());
        for (AbstractTaskDirective directive : taskDirectiveList) {
            if (null == directive.getName()) {
                directive.setName(uncapitalize(directive.getClass().getSimpleName().replaceAll(directiveRemoveRegex, BLANK)));
            }
            taskDirectiveMap.put(directive.getName(), directive);
        }
        log.info(new StringBuilder().append(taskDirectiveMap.size()).append(" task directives created:")
                .append(taskDirectiveMap.keySet()).toString());
        for (AbstractAppDirective directive : appDirectiveList) {
            if (null == directive.getName()) {
                directive.setName(uncapitalize(directive.getClass().getSimpleName().replaceAll(directiveRemoveRegex, BLANK)));
            }
            appDirectiveMap.put(directive.getName(), directive);
        }
        log.info(new StringBuilder().append(appDirectiveMap.size()).append(" app directives created:")
                .append(appDirectiveMap.keySet()).toString());
        for (BaseMethod method : methodList) {
            if (null == method.getName()) {
                method.setName(uncapitalize(method.getClass().getSimpleName().replaceAll(methodRemoveRegex, BLANK)));
            }
            methodMap.put(method.getName(), method);
        }
        log.info(new StringBuilder().append(methodMap.size()).append(" methods created:").append(methodMap.keySet()).toString());
    }

    public void setDirectiveRemoveRegex(String directiveRemoveRegex) {
        this.directiveRemoveRegex = directiveRemoveRegex;
    }

    public void setMethodRemoveRegex(String methodRemoveRegex) {
        this.methodRemoveRegex = methodRemoveRegex;
    }

    public Map<String, AbstractTemplateDirective> getTemplateDirectiveMap() {
        return templateDirectiveMap;
    }

    public Map<String, AbstractAppDirective> getAppDirectiveMap() {
        return appDirectiveMap;
    }

    public Map<String, AbstractTaskDirective> getTaskDirectiveMap() {
        return taskDirectiveMap;
    }

    public Map<String, BaseMethod> getMethodMap() {
        return methodMap;
    }

}
