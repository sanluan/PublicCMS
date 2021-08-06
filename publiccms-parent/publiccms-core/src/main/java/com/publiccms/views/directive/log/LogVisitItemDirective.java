package com.publiccms.views.directive.log;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisitItem;
import com.publiccms.entities.log.LogVisitItemId;
import com.publiccms.logic.service.log.LogVisitItemService;

/**
 *
 * LogVisitItemDirective
 * 
 */
@Component
public class LogVisitItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (null != visitDate && CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            LogVisitItem entity = service.getEntity(new LogVisitItemId(getSite(handler).getId(), visitDate, itemType, itemId));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LogVisitItemService service;

}
