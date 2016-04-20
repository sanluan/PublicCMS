package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.common.base.AbstractTemplateDirective;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class CmsSearchDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        String word = handler.getString("word");
        String tagId = handler.getString("tagId");
        if (notEmpty(word) || notEmpty(tagId)) {
            PageHandler page = service.query(getSite(handler).getId(), word, tagId, handler.getInteger("pageIndex", 1),
                    handler.getInteger("count", 30));
            handler.put("page", page).render();
        }
    }

    @Autowired
    private CmsContentService service;

}