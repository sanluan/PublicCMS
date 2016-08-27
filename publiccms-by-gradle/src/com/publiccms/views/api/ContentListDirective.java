package com.publiccms.views.api;

//Generated 2015-5-10 17:54:56 by com.sanluan.common.source.SourceMaker

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractAppV1Directive;
import com.publiccms.entities.sys.SysApp;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.service.cms.CmsContentService;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.RenderHandler;

@Component
public class ContentListDirective extends AbstractAppV1Directive {

    @Override
    public void execute(RenderHandler handler, SysApp app, SysUser user) throws IOException, Exception {
        Date endPublishDate = handler.getDate("endPublishDate");
        Integer categoryId = handler.getInteger("categoryId");
        Boolean containChild = handler.getBoolean("containChild");
        Long parentId = handler.getLong("parentId");
        Boolean onlyUrl = handler.getBoolean("onlyUrl");
        Boolean hasImages = handler.getBoolean("hasImages");
        Boolean hasFiles = handler.getBoolean("hasFiles");
        Long authorId = handler.getLong("authorId");
        Integer pageIndex = handler.getInteger("pageIndex", 1);
        Integer count = handler.getInteger("count", 30);
        Date now = getDate();
        if (empty(endPublishDate) || endPublishDate.after(now)) {
            endPublishDate = now;
        }
        Integer[] status = new Integer[] { CmsContentService.STATUS_NORMAL };
        Boolean disabled = false;
        Boolean emptyParent = true;
        PageHandler page = service.getPage(getSite(handler).getId(), status, categoryId, containChild, disabled, null, parentId,
                emptyParent, onlyUrl, hasImages, hasFiles, null, authorId, null, null, endPublishDate, null, null, pageIndex,
                count);
        handler.put("page", page).render();
    }

    @Autowired
    private CmsContentService service;

    @Override
    public boolean needUserToken() {
        return false;
    }

    @Override
    public boolean needAppToken() {
        return false;
    }

}