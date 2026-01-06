package com.publiccms.logic.component.workflow;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractLongWorkflowHandler;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryLangService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentLangService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;

import freemarker.template.TemplateException;

@Component
public class ContentWorkflowHandler extends AbstractLongWorkflowHandler {
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsCategoryLangService categoryLangService;

    @Override
    public String getItemType() {
        return SysWorkflowProcessService.ITEM_TYPE_CONTENT;
    }

    @Override
    public void finish(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        CmsContent content = service.checkInProcess(entity.getSiteId(), user.getId(), itemId);
        try {
            templateComponent.publish(site, content);
        } catch (IOException | TemplateException e) {
        }
    }

    @Override
    public void interrupt(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        CmsContent content = service.rejectInProcess(entity.getSiteId(), user.getId(), itemId);
        templateComponent.deleteStaticFile(site.getId(), content);
    }

    @Resource
    private CmsContentService service;
    @Resource
    private CmsContentLangService langService;
}
