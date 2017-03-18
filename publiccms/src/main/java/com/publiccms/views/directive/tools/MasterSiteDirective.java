package com.publiccms.views.directive.tools;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.RenderHandler;

@Component
public class MasterSiteDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        if (siteComponent.isMaster(getSite(handler).getId())) {
            handler.render();
        }
    }

}