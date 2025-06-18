package com.publiccms.logic.component.workflow;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractLongWorkflowHandler;
import com.publiccms.controller.admin.cms.CmsContentAdminController;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

@Component
public class ContentWorkflowHandler extends AbstractLongWorkflowHandler {
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private CmsCategoryService categoryService;

    @Override
    public String getItemType() {
        return SysWorkflowProcessService.ITEM_TYPE_CONTENT;
    }

    @Override
    public void finish(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        CmsContent content = service.checkInProcess(entity.getSiteId(), user.getId(), itemId);
        try {
            CmsCategory category = categoryService.getEntity(content.getCategoryId());
            templateComponent.createContentFile(site, content, category, null);
            if (null != category) {
                templateComponent.createCategoryFile(site, category, null, null);
            }
            if (null != content.getParentId()) {
                CmsContent parent = service.getEntity(content.getParentId());
                if (null != parent) {
                    templateComponent.createContentFile(site, parent, category, null);
                }
            }
        } catch (IOException | TemplateException e) {
        }
    }

    @Override
    public void interrupt(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        CmsContent content = service.rejectInProcess(entity.getSiteId(), user.getId(), itemId);
        CmsContentAdminController.deleteFile(site, content, siteComponent);
    }

    @Resource
    private CmsContentService service;
}
