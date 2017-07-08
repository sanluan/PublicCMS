package org.publiccms.logic.component.site;

import static org.apache.commons.lang3.StringUtils.uncapitalize;
import static org.apache.commons.logging.LogFactory.getLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.publiccms.common.base.AbstractAppDirective;
import org.publiccms.common.base.AbstractTaskDirective;
import org.publiccms.common.base.AbstractTemplateDirective;
import org.springframework.beans.factory.annotation.Autowired;

import com.publiccms.common.base.Base;
import com.publiccms.common.base.BaseMethod;

/**
 * 
 * DirectiveComponent 指令处理组件
 *
 */
public class DirectiveComponent implements Base {
    private String directiveRemoveRegex;
    private String methodRemoveRegex;
    protected final Log log = getLog(getClass());

    private Map<String, AbstractTemplateDirective> templateDirectiveMap = new HashMap<>();
    private Map<String, AbstractTaskDirective> taskDirectiveMap = new HashMap<>();
    private Map<String, AbstractAppDirective> appDirectiveMap = new HashMap<>();
    private Map<String, BaseMethod> methodMap = new HashMap<>();

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
     * @return
     */
    public Map<String, AbstractTemplateDirective> getTemplateDirectiveMap() {
        return templateDirectiveMap;
    }

    /**
     * @return
     */
    public Map<String, AbstractAppDirective> getAppDirectiveMap() {
        return appDirectiveMap;
    }

    /**
     * @return
     */
    public Map<String, AbstractTaskDirective> getTaskDirectiveMap() {
        return taskDirectiveMap;
    }

    /**
     * @return
     */
    public Map<String, BaseMethod> getMethodMap() {
        return methodMap;
    }

}
