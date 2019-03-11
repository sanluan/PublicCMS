package com.publiccms.views.directive.api;

//Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import com.publiccms.common.base.AbstractAppDirective;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.component.site.StatisticsComponent;
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
                        handler.getIntegerArray("categoryIds"), false, handler.getStringArray("modelIds"),
                        handler.getLong("parentId"), handler.getBoolean("emptyParent"), handler.getLong("quoteId"),
                        handler.getBoolean("quote"), handler.getBoolean("onlyUrl"), handler.getBoolean("hasImages"),
                        handler.getBoolean("hasCover"), handler.getBoolean("hasFiles"), null, user.getId(), null,
                        handler.getDate("endPublishDate"), null),
                handler.getBoolean("containChild"), null, null, handler.getInteger("pageIndex", 1),
                handler.getInteger("count", 30));
        @SuppressWarnings("unchecked")
        List<CmsContent> list = (List<CmsContent>) page.getList();
        list.forEach(e -> {
            Integer clicks = statisticsComponent.getContentClicks(e.getId());
            if (null != clicks) {
                e.setClicks(e.getClicks() + clicks);
            }
        });
        handler.put("page", page);
    }

    @Autowired
    private CmsContentService service;
    @Autowired
    private StatisticsComponent statisticsComponent;

    @Override
    public boolean needUserToken() {
        return true;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}