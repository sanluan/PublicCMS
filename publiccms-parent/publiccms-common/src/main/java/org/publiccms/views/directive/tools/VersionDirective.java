package org.publiccms.views.directive.tools;

import java.io.IOException;

import org.publiccms.common.base.AbstractTemplateDirective;
import org.publiccms.common.constants.CmsVersion;
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
        handler.put("preview", CmsVersion.isPreview());
        handler.put("businessEdition", CmsVersion.isBusinessEdition());
        handler.put("cluster", CmsVersion.getClusterId());
        handler.put("master", CmsVersion.isMaster());
        handler.render();
    }

}
