package com.publiccms.logic.component.workflow;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractLongWorkflowHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsPlace;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.component.template.MetadataComponent;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsPlaceService;
import com.publiccms.logic.service.sys.SysWorkflowProcessService;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;
import jakarta.annotation.Resource;

@Component
public class PlaceWorkflowHandler extends AbstractLongWorkflowHandler {
    protected final Log log = LogFactory.getLog(getClass());
    @Resource
    private TemplateComponent templateComponent;
    @Resource
    protected SiteComponent siteComponent;
    @Resource
    private MetadataComponent metadataComponent;

    @Override
    public String getItemType() {
        return SysWorkflowProcessService.ITEM_TYPE_PLACE;
    }

    @Override
    public void finish(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        CmsPlace place = service.checkInProcess(entity.getSiteId(), user.getId(), itemId);
        String placePath = CommonUtils.joinString(TemplateComponent.INCLUDE_DIRECTORY, place.getPath());
        if (site.isUseSsi() || CmsFileUtils.exists(siteComponent.getWebFilePath(site.getId(), placePath))) {
            try {
                String filepath = siteComponent.getTemplateFilePath(site.getId(), placePath);
                CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                CmsPageData data = metadataComponent.getTemplateData(filepath);
                templateComponent.staticPlace(site, place.getPath(), metadata, data);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void interrupt(SysSite site, SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        service.rejectInProcess(entity.getSiteId(), user.getId(), itemId);
    }

    @Resource
    private CmsPlaceService service;
}
