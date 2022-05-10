package com.publiccms.logic.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.component.config.ConfigComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.task.ScheduledTask;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentAttributeService;
import com.publiccms.logic.service.cms.CmsContentTextService;
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
    private static ConfigComponent configComponent;
    private static SysTaskService sysTaskService;
    private static LogTaskService logTaskService;
    private static SysSiteService siteService;
    private static ScheduledTask scheduledTask;

    private static CmsContentAttributeService contentAttributeService;
    private static CmsContentTextService contentService;
    private static TemplateComponent templateComponent;
    private static MetadataComponent metadataComponent;

    /**
     * @return the siteComponent
     */
    public static SiteComponent getSiteComponent() {
        return siteComponent;
    }

    /**
     * @return the configComponent
     */
    public static ConfigComponent getConfigComponent() {
        return configComponent;
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
     * @return the contentService
     */
    public static CmsContentTextService getContentService() {
        return contentService;
    }
    /**
     * @param siteComponent
     *            the siteComponent to set
     */
    @Autowired
    public void setSiteComponent(SiteComponent siteComponent) {
        BeanComponent.siteComponent = siteComponent;
    }

    /**
     * @param configComponent
     *            the configComponent to set
     */
    @Autowired
    public void setConfigComponent(ConfigComponent configComponent) {
        BeanComponent.configComponent = configComponent;
    }

    /**
     * @param sysTaskService
     *            the sysTaskService to set
     */
    @Autowired
    public void setSysTaskService(SysTaskService sysTaskService) {
        BeanComponent.sysTaskService = sysTaskService;
    }

    /**
     * @param logTaskService
     *            the logTaskService to set
     */
    @Autowired
    public void setLogTaskService(LogTaskService logTaskService) {
        BeanComponent.logTaskService = logTaskService;
    }

    /**
     * @param siteService
     *            the siteService to set
     */
    @Autowired
    public void setSiteService(SysSiteService siteService) {
        BeanComponent.siteService = siteService;
    }

    /**
     * @param scheduledTask
     *            the scheduledTask to set
     */
    @Autowired
    public void setScheduledTask(ScheduledTask scheduledTask) {
        BeanComponent.scheduledTask = scheduledTask;
    }

    /**
     * @param contentAttributeService
     *            the contentAttributeService to set
     */
    @Autowired
    public void setContentAttributeService(CmsContentAttributeService contentAttributeService) {
        BeanComponent.contentAttributeService = contentAttributeService;
    }
    
    /**
     * @param contentService
     *            the contentService to set
     */
    @Autowired
    public void setContentService(CmsContentTextService contentService) {
        BeanComponent.contentService = contentService;
    }

    /**
     * @param templateComponent
     *            the templateComponent to set
     */
    @Autowired
    public void setTemplateComponent(TemplateComponent templateComponent) {
        BeanComponent.templateComponent = templateComponent;
    }

    /**
     * @return the metadataComponent
     */
    public static MetadataComponent getMetadataComponent() {
        return metadataComponent;
    }

    /**
     * @param metadataComponent the metadataComponent to set
     */
    @Autowired
    public void setMetadataComponent(MetadataComponent metadataComponent) {
        BeanComponent.metadataComponent = metadataComponent;
    }
}
