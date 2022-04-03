package com.publiccms.views.directive.visit;

// Generated 2021-1-14 22:44:12 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;
import java.util.Date;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.visit.VisitUrl;
import com.publiccms.entities.visit.VisitUrlId;
import com.publiccms.logic.service.visit.VisitUrlService;

/**
 *
 * VisitUrlDirective
 * 
 */
@Component
public class VisitUrlDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date visitDate = handler.getDate("visitDate");
        String urlMd5 = handler.getString("urlMd5");
        String urlSha = handler.getString("urlSha");
        if (null != visitDate && CommonUtils.notEmpty(urlMd5) && CommonUtils.notEmpty(urlSha)) {
            VisitUrl entity = service.getEntity(new VisitUrlId(getSite(handler).getId(), visitDate, urlMd5, urlSha));
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
    private VisitUrlService service;

}
