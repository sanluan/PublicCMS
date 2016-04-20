package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsContentListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date endPublishDate = handler.getDate("endPublishDate");
        Integer[] status = handler.getIntegerArray("status");
        Boolean disabled = handler.getBoolean("disabled", false);
        Boolean emptyParent = handler.getBoolean("emptyParent");
        if (!handler.getBoolean("admin", false)) {
            Date now = getDate();
            if (empty(endPublishDate) || endPublishDate.after(now)) {
                endPublishDate = now;
            }
            status = new Integer[] { CmsContentService.STATUS_NORMAL };
            disabled = false;
            emptyParent = true;
        }
        PageHandler page = service.getPage(getSite(handler).getId(), status, handler.getInteger("categoryId"),
                handler.getBoolean("containChild"), disabled, handler.getIntegerArray("modelId"), handler.getInteger("parentId"),
                emptyParent, handler.getBoolean("onlyUrl"), handler.getBoolean("hasImages"), handler.getBoolean("hasFiles"),
                handler.getString("title"), handler.getInteger("userId"), handler.getInteger("checkUserId"),
                handler.getDate("startPublishDate"), endPublishDate, handler.getString("orderField"),
                handler.getString("orderType"), handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;

}