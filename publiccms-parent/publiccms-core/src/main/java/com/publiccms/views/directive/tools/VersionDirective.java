package com.publiccms.views.directive.tools;

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CmsVersion;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;

/**
 * 
 * VersionDirective 技术框架版本指令
 *
 */
@Component
public class VersionDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        handler.put("cms", CmsVersion.getVersion());
        handler.put("revision", CmsVersion.getRevision());
        boolean authorizationEdition = CmsVersion.isAuthorizationEdition();
        handler.put("authorizationEdition", authorizationEdition);
        if (authorizationEdition) {
            handler.put("authorizationStartDate", CmsVersion.getLicense().getStartDate());
            handler.put("authorizationEndDate", CmsVersion.getLicense().getEndDate());
            handler.put("authorizationOrganization", CmsVersion.getLicense().getOrganization());
        }
        handler.put("cluster", CmsVersion.getClusterId());
        handler.put("master", CmsVersion.isMaster());
        handler.render();
    }

}
