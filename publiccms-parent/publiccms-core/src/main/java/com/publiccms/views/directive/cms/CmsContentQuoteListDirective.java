package com.publiccms.views.directive.cms;

// Generated 2016-2-18 23:41:56 by com.publiccms.common.source.SourceGenerator

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.base.AbstractTemplateDirective;
import com.publiccms.common.database.CmsDataSource;
import com.publiccms.common.handler.RenderHandler;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.DatasourceComponent;
import com.publiccms.logic.service.cms.CmsContentTextService;

/**
 *
 * CmsContentFileListDirective
 * 
 */
@Component
public class CmsContentQuoteListDirective extends AbstractTemplateDirective {

    @Override
    public void execute(RenderHandler handler) throws IOException, Exception {
        SysSite site = getSite(handler);
        try {
            CmsDataSource.setDataSourceName(datasourceComponent.getRandomDatasource(site.getId()));
            List<CmsContent> list = service.getListByQuoteId(site.getId(), handler.getLong("quoteId"));
            handler.put("list", list).render();
        } finally {
            CmsDataSource.resetDataSourceName();
        }
    }

    @Autowired
    private CmsContentTextService service;
    @Autowired
    private DatasourceComponent datasourceComponent;
}