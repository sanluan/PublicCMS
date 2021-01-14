package com.publiccms.views.directive.log;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisitSession;
import com.publiccms.entities.log.LogVisitSessionId;
import com.publiccms.logic.service.log.LogVisitSessionService;

/**
 *
 * LogVisitSessionDirective
 * 
 */
@Component
public class LogVisitSessionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String sessionId = handler.getString("sessionId");
        Date visitDate = handler.getDate("visitDate");
        if (CommonUtils.notEmpty(sessionId) && null != visitDate) {
            LogVisitSession entity = service.getEntity(new LogVisitSessionId(getSite(handler).getId(), sessionId, visitDate));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Autowired
    private LogVisitSessionService service;

}
