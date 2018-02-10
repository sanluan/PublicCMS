package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.query.CmsContentQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;

/**
 *
 * MyContentListDirective
 * 
 */
@Component
public class MyContentListDirective extends AbstractAppDirective {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        PageHandler page = service.getPage(
                new CmsContentQuery(getSite(handler).getId(), handler.getIntegerArray("status"), handler.getInteger("categoryId"),
                        handler.getIntegerArray("categoryIds"), false, null, handler.getLong("parentId"),
                        handler.getBoolean("emptyParent"), handler.getBoolean("onlyUrl"), handler.getBoolean("hasImages"),
                        handler.getBoolean("hasFiles"), null, user.getId(), null, handler.getDate("endPublishDate")),
                handler.getBoolean("containChild"), null, null, handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        handler.put("page", page);
    }

    @Autowired
    private CmsContentService service;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}