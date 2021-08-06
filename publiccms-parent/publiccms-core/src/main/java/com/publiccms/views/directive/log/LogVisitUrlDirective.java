package com.publiccms.views.directive.log;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.log.LogVisitUrl;
import com.publiccms.entities.log.LogVisitUrlId;
import com.publiccms.logic.service.log.LogVisitUrlService;

/**
 *
 * LogVisitUrlDirective
 * 
 */
@Component
public class LogVisitUrlDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        String urlMd5 = handler.getString("urlMd5");
        String urlSha = handler.getString("urlSha");
        if (null != visitDate && CommonUtils.notEmpty(urlMd5) && CommonUtils.notEmpty(urlSha)) {
            LogVisitUrl entity = service.getEntity(new LogVisitUrlId(getSite(handler).getId(), visitDate, urlMd5, urlSha));
            if (null != entity) {
                handler.put("object", entity).render();
            }
        }
    }

    @Autowired
    private LogVisitUrlService service;

}
