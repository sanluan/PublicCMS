package com.publiccms.test;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.logic.service.cms.CmsCategoryService;
import com.publiccms.views.pojo.query.CmsCategoryQuery;

import config.spring.ApplicationConfig;

/**
 * CmsContentTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("category test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class CmsCategoryGenerateChildIds {

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("generate category childIds test case")
    public void generateChildIds() {
        CmsCategoryQuery query = new CmsCategoryQuery();
        query.setQueryAll(true);
        PageHandler page = categoryService.getPage(query, null, null);
        for (CmsCategory category : (List<CmsCategory>) page.getList()) {
            categoryService.generateChildIds(category.getSiteId(), category.getId());
        }
    }

    @Autowired
    private CmsCategoryService categoryService;
}
