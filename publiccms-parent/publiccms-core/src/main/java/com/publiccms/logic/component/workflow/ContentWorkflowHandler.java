package com.publiccms.logic.component.workflow;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.base.AbstractLongWorkflowHandler;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.entities.sys.SysWorkflowProcess;
import com.publiccms.entities.sys.SysWorkflowProcessHistory;
import com.publiccms.logic.service.cms.CmsContentService;

import jakarta.annotation.Resource;

@Component
public class ContentWorkflowHandler extends AbstractLongWorkflowHandler {

    @Override
    public String getItemType() {
        return Config.INPUTTYPE_CONTENT;
    }

    @Override
    public void finish(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        service.checkInProcess(entity.getSiteId(), user, itemId);
    }

    @Override
    public void interrupt(SysWorkflowProcess entity, SysUser user, SysWorkflowProcessHistory history, Long itemId) {
        service.rejectInProcess(entity.getSiteId(), user, itemId);
    }

    @Resource
    private CmsContentService service;
}
