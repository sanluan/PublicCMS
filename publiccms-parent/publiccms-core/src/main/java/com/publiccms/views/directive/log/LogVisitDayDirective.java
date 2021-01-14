package com.publiccms.views.directive.log;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.log.LogVisitDay;
import com.publiccms.entities.log.LogVisitDayId;
import com.publiccms.logic.service.log.LogVisitDayService;

/**
 *
 * LogVisitDayDirective
 * 
 */
@Component
public class LogVisitDayDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        Byte visitHour = handler.getByte("visitHour");
        if (null != visitDate && null != visitHour) {
            LogVisitDay entity = service.getEntity(new LogVisitDayId(getSite(handler).getId(), visitDate, visitHour));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Autowired
    private LogVisitDayService service;

}
