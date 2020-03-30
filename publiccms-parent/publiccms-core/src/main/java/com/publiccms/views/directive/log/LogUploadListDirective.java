package com.publiccms.views.directive.log;

// Generated 2016-5-24 20:56:00 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.logic.service.log.LogUploadService;

/**
 *
 * LogUploadListDirective
 * 
 */
@Component
public class LogUploadListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String[] fileTypes = handler.getStringArray("fileTypes");
        if (CommonUtils.empty(fileTypes) && handler.getBoolean("image", false)) {
            fileTypes = new String[] { CmsFileUtils.FILE_TYPE_IMAGE };
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getString("channel"),
                fileTypes, handler.getString("originalName"), handler.getString("filePath"), handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private LogUploadService service;

}