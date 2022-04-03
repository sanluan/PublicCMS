package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:06 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitSession;
import com.publiccms.entities.visit.VisitSessionId;
import com.publiccms.logic.service.visit.VisitSessionService;

/**
 *
 * VisitSessionDirective
 * 
 */
@Component
public class VisitSessionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String sessionId = handler.getString("sessionId");
        Date visitDate = handler.getDate("visitDate");
        if (CommonUtils.notEmpty(sessionId) && null != visitDate) {
            VisitSession entity = service.getEntity(new VisitSessionId(getSite(handler).getId(), sessionId, visitDate));
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
    private VisitSessionService service;

}
