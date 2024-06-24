package com.publiccms.logic.component;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.logic.component.config.ConfigDataComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.task.ScheduledTask;
import com.publiccms.logic.component.template.ModelComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.log.LogTaskService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.sys.SysTaskService;

/**
 * 
 * BeanComponent Bean操作组件
 *
 */
@Component
public class BeanComponent {
    private static SiteComponent siteComponent;
    private static ConfigDataComponent configDataComponent;
    private static SysTaskService sysTaskService;
    private static LogTaskService logTaskService;
    private static SysSiteService siteService;
    private static ScheduledTask scheduledTask;
    private static CmsContentAttributeService contentAttributeService;
    private static TemplateComponent templateComponent;
    private static ModelComponent modelComponent;

    /**
     * @return the siteComponent
     */
    public static SiteComponent getSiteComponent() {
        return siteComponent;
    }

    /**
     * @return the configComponent
     */
    public static ConfigDataComponent getConfigDataComponent() {
        return configDataComponent;
    }

    /**
     * @return the sysTaskService
     */
    public static SysTaskService getSysTaskService() {
        return sysTaskService;
    }

    /**
     * @return the logTaskService
     */
    public static LogTaskService getLogTaskService() {
        return logTaskService;
    }

    /**
     * @return the siteService
     */
    public static SysSiteService getSiteService() {
        return siteService;
    }

    /**
     * @return the scheduledTask
     */
    public static ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    /**
     * @return the templateComponent
     */
    public static TemplateComponent getTemplateComponent() {
        return templateComponent;
    }

    /**
     * @return the contentAttributeService
     */
    public static CmsContentAttributeService getContentAttributeService() {
        return contentAttributeService;
    }
    /**
     * @param siteComponent
     *            the siteComponent to set
     */
    @Resource
    public void setSiteComponent(SiteComponent siteComponent) {
        BeanComponent.siteComponent = siteComponent;
    }

    /**
     * @param configDataComponent
     *            the configDataComponent to set
     */
    @Resource
    public void setConfigDataComponent(ConfigDataComponent configDataComponent) {
        BeanComponent.configDataComponent = configDataComponent;
    }

    /**
     * @param sysTaskService
     *            the sysTaskService to set
     */
    @Resource
    public void setSysTaskService(SysTaskService sysTaskService) {
        BeanComponent.sysTaskService = sysTaskService;
    }

    /**
     * @param logTaskService
     *            the logTaskService to set
     */
    @Resource
    public void setLogTaskService(LogTaskService logTaskService) {
        BeanComponent.logTaskService = logTaskService;
    }

    /**
     * @param siteService
     *            the siteService to set
     */
    @Resource
    public void setSiteService(SysSiteService siteService) {
        BeanComponent.siteService = siteService;
    }

    /**
     * @param scheduledTask
     *            the scheduledTask to set
     */
    @Resource
    public void setScheduledTask(ScheduledTask scheduledTask) {
        BeanComponent.scheduledTask = scheduledTask;
    }

    /**
     * @param contentAttributeService
     *            the contentAttributeService to set
     */
    @Resource
    public void setContentAttributeService(CmsContentAttributeService contentAttributeService) {
        BeanComponent.contentAttributeService = contentAttributeService;
    }
    
    /**
     * @param templateComponent
     *            the templateComponent to set
     */
    @Resource
    public void setTemplateComponent(TemplateComponent templateComponent) {
        BeanComponent.templateComponent = templateComponent;
    }
    

    /**
     * @return the modelComponent
     */
    public static ModelComponent getModelComponent() {
        return modelComponent;
    }

    /**
     * @param modelComponent the modelComponent to set
     */
    @Resource
    public void setModelComponent(ModelComponent modelComponent) {
        BeanComponent.modelComponent = modelComponent;
    }
}
