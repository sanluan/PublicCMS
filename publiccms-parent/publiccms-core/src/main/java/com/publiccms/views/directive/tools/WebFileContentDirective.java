package com.publiccms.views.directive.tools;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * WebContentDirective
 * 
 */
@Component
public class WebFileContentDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path");
        if (CommonUtils.notEmpty(path)) {
            handler.put("object", CmsFileUtils.getFileContent(siteComponent.getWebFilePath(getSite(handler), path))).render();
        }
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}