package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsContentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * CmsContentFileListDirective
 * 
 */
@Component
public class CmsContentFileListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String[] fileTypes = handler.getStringArray("fileTypes");
        if (CommonUtils.empty(fileTypes) && handler.getBoolean("image", false)) {
            fileTypes = new String[] { CmsFileUtils.FILE_TYPE_IMAGE };
        }
        PageHandler page = service.getPage(handler.getLong("contentId"), handler.getLong("userId"),
                fileTypes, handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentFileService service;

}