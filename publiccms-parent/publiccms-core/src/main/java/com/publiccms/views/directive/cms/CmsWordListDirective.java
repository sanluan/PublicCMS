package com.publiccms.views.directive.cms;

// Generated 2016-3-22 11:21:35 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import jakarta.annotation.Resource;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.logic.service.cms.CmsWordService;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * CmsWordListDirective
 * 
 */
@Component
public class CmsWordListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        Boolean hidden = false;
        String orderField = "searchCount";
        if (getAdvanced(handler)) {
            hidden = handler.getBoolean("hidden");
            orderField = handler.getString("orderField");
        }
        PageHandler page;
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer count = handler.getInteger("pageSize", handler.getInteger("count", 30));
        try {
            page = service.getPage(getSite(handler).getId(), hidden, handler.getDate("startCreateDate"),
                    handler.getDate("endCreateDate"), handler.getString("name"), orderField, handler.getString("orderType"),
                    pageIndex, count);
        } catch (Exception e) {
            page = new PageHandler(pageIndex, count);
        }
        handler.put("page", page).render();
    }

    @Override
    public boolean needAppToken() {
        return true;
    }

    @Override
    public boolean supportAdvanced() {
        return true;
    }

    @Resource
    private CmsWordService service;

}