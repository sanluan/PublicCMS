package com.publiccms.test.logic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryModel;
import com.publiccms.entities.cms.CmsCategoryModelId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.template.TemplateComponent;
import com.publiccms.logic.service.cms.CmsCategoryModelService;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysSiteService;

import config.spring.ApplicationConfig;
import jakarta.annotation.Resource;

/**
 * BatchTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("Batch test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class BatchTest {
    protected final Log log = LogFactory.getLog(getClass());

    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    /**
     * 
     */
    @Test
    @DisplayName("batch test case")
    public void searchTest() {
        long start = System.currentTimeMillis();
        SysSite site = siteService.getEntity((short) 1);
        log.info(start);
        CmsCategory category = categoryService.getEntity(1);
        CmsCategoryModel categoryModel = categoryModelService.getEntity(new CmsCategoryModelId(1,"categoryModel"));
        contentService.batchWorkId(site.getId(), 1, "standard", (list, i) -> {
            templateComponent.createContentFile(site, list, category, categoryModel);
            log.info((System.currentTimeMillis() - start) + " batch " + i + " publish size : " + list.size());
        }, PageHandler.MAX_PAGE_SIZE);
        log.info(System.currentTimeMillis() - start);
    }

    @Resource
    private SysSiteService siteService;
    @Resource
    private CmsCategoryService categoryService;
    @Resource
    private CmsCategoryModelService categoryModelService;
    @Resource
    private CmsContentService contentService;
    @Resource
    private TemplateComponent templateComponent;
}
