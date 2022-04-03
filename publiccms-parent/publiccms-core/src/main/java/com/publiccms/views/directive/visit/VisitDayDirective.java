package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.visit.VisitDay;
import com.publiccms.entities.visit.VisitDayId;
import com.publiccms.logic.service.visit.VisitDayService;

/**
 *
 * VisitDayDirective
 * 
 */
@Component
public class VisitDayDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        Byte visitHour = handler.getByte("visitHour");
        if (null != visitDate && null != visitHour) {
            VisitDay entity = service.getEntity(new VisitDayId(getSite(handler).getId(), visitDate, visitHour));
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
    private VisitDayService service;

}
