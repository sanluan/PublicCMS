package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitItem;
import com.publiccms.entities.visit.VisitItemId;
import com.publiccms.logic.service.visit.VisitItemService;

/**
 *
 * VisitItemDirective
 * 
 */
@Component
public class VisitItemDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        String itemType = handler.getString("itemType");
        String itemId = handler.getString("itemId");
        if (null != visitDate && CommonUtils.notEmpty(itemType) && CommonUtils.notEmpty(itemId)) {
            VisitItem entity = service.getEntity(new VisitItemId(getSite(handler).getId(), visitDate, itemType, itemId));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Resource
    private VisitItemService service;

}
