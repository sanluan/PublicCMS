package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by SourceMaker

import java.io.IOException;

import org.hibernate.search.exception.EmptyQueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.base.BaseDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsSearchDirective extends BaseDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        try {
            PageHandler page = service.query(handler.getString("word"), handler.getInteger("pageIndex", 1),
                    handler.getInteger("count", 30));
            handler.put("page", page).render();
        } catch (EmptyQueryException e) {
            log.debug(e.getMessage());
        }
    }

    @Autowired
    private CmsContentService service;

}