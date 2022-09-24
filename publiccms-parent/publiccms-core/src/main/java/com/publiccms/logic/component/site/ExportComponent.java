package com.publiccms.logic.component.site;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;

/**
 * ExportComponent 数据导出组件
 * 
 */
@Component
public class ExportComponent {
    @Autowired
    private CmsCategoryService categoryService;
    @Autowired
    private CmsContentService contentService;

    public void exportCategory(short siteId, ZipOutputStream zipOutputStream) {
        categoryService.batchWork(siteId, list -> {

        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void importCategory(short siteId) {

    }

    public void importContent(short siteId) {
        contentService.batchWork(siteId, null, null, list -> {

        }, PageHandler.MAX_PAGE_SIZE);
    }

    public void exportContent(short siteId) {

    }
}
