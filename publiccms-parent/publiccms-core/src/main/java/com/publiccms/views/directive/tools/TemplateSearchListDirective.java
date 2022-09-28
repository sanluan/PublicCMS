package com.publiccms.views.directive.tools;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.generator.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.constants.CommonConstants;

import org.springframework.stereotype.Component;

import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;

/**
 *
 * TemplateSearchListDirective
 * 
 */
@Component
public class TemplateSearchListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String path = handler.getString("path", CommonConstants.SEPARATOR);
        handler.put("list", CmsFileUtils.searchFileList(siteComponent.getTemplateFilePath(getSite(handler), path), path,
                handler.getString("word"))).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

}