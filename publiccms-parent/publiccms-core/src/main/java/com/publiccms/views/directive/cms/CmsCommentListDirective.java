package com.publiccms.views.directive.cms;

// Generated 2018-11-7 16:25:07 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsCommentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsCommentListDirective
 * 
 */
@Component
public class CmsCommentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Integer status;
        Long checkUserId = null;
        Boolean disabled;
        if (handler.getBoolean("advanced", false)) {
            status = handler.getInteger("status");
            checkUserId = handler.getLong("checkUserId");
            disabled = handler.getBoolean("disabled", false);
        } else {
            status = CmsCommentService.STATUS_NORMAL;
            disabled = false;
        }
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("userId"), handler.getLong("replyId"),
                handler.getBoolean("emptyReply", false), handler.getLong("replyUserId"), handler.getLong("contentId"),
                checkUserId, status, disabled, handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("pageSize", handler.getInteger("count", 30)));
        handler.put("page", page).render();
    }
    
    @Override
    public boolean needAppToken() {
        return true;
    }

    @Autowired
    private CmsCommentService service;

}