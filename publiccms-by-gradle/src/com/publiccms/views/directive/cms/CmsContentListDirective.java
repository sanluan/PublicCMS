package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by SourceMaker

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsContentListDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Date endPublishDate = handler.getDate("endPublishDate");
        Integer[] status = handler.getIntegerArray("status");
        Boolean disabled = handler.getBoolean("disabled", false);
        if (handler.getBoolean("admin", false)) {
        } else {
            Date now = new Date();
            if (null == endPublishDate || endPublishDate.after(now)) {
                endPublishDate = now;
            }
            status = new Integer[] { CmsContentService.STATUS_NORMAL };
            disabled = false;
        }
        PageHandler page = service.getPage(status, handler.getInteger("categoryId"), handler.getBoolean("containChild"),
                disabled, handler.getIntegerArray("modelId"), handler.getInteger("parentId"), handler.getString("title"),
                handler.getInteger("userId"), handler.getDate("startPublishDate"), endPublishDate, handler.getString("extend1"),
                handler.getString("extend2"), handler.getString("extend3"), handler.getString("extend4"),
                handler.getString("modelExtend1"), handler.getString("modelExtend2"), handler.getString("modelExtend3"),
                handler.getString("modelExtend4"), handler.getString("orderField"), handler.getString("orderType"),
                handler.getInteger("pageIndex", 1), handler.getInteger("count", 30));
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;

}