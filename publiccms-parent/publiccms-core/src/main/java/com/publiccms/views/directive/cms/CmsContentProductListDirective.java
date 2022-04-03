package com.publiccms.views.directive.cms;

// Generated 2021-6-26 17:53:08 by com.publiccms.common.generator.SourceGenerator
import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContentProduct;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsContentProductService;

/**
 *
 * CmsContentProductListDirective
 * 
 */
@Component
public class CmsContentProductListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        PageHandler page = service.getPage(getSite(handler).getId(), handler.getLong("contentId"), handler.getLong("userId"),
                handler.getString("title"), handler.getBigDecimal("startPrice"), handler.getBigDecimal("endPrice"),
                handler.getString("orderField"), handler.getString("orderType"), handler.getInteger("pageIndex", 1),
                handler.getInteger("pageSize", 30));
        @SuppressWarnings("unchecked")
        List<CmsContentProduct> list = (List<CmsContentProduct>) page.getList();
        if (null != list) {
            boolean absoluteURL = handler.getBoolean("absoluteURL", true);
            SysSite site = getSite(handler);
            list.forEach(e -> {
                if (absoluteURL) {
                    e.setCover(TemplateComponent.getUrl(site, true, e.getCover()));
                }
            });
        }
        handler.put("page", page).render();
    }

    @Resource
    private CmsContentProductService service;

}