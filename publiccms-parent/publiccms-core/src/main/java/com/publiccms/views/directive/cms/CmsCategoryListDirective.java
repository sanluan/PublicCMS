package com.publiccms.views.directive.cms;

// Generated 2015-5-10 17:54:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

/**
 *
 * CmsCategoryListDirective
 * 
 */
@Component
public class CmsCategoryListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        CmsCategoryQuery queryEntity = new CmsCategoryQuery();
        queryEntity.setQueryAll(handler.getBoolean("queryAll"));
        if (handler.getBoolean("advanced", false)) {
            queryEntity.setDisabled(handler.getBoolean("disabled", false));
            queryEntity.setHidden(handler.getBoolean("hidden"));
        } else {
            queryEntity.setDisabled(false);
            queryEntity.setHidden(false);
        }
        queryEntity.setSiteId(site.getId());
        queryEntity.setParentId(handler.getInteger("parentId"));
        queryEntity.setTypeId(handler.getInteger("typeId"));
        queryEntity.setAllowContribute(handler.getBoolean("allowContribute"));

        PageHandler page = service.getPage(queryEntity, handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", handler.getInteger("count", 30)));
        @SuppressWarnings("unchecked")
        List<CmsCategory> list = (List<CmsCategory>) page.getList();
        if (null != list && handler.getBoolean("absoluteURL", true)) {
            list.forEach(e -> {
                templateComponent.initCategoryUrl(site, e);
            });
        }
        handler.put("page", page).render();
    }

    @Autowired
    private CmsCategoryService service;
    @Autowired
    private TemplateComponent templateComponent;

}