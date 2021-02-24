package com.publiccms.test;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.service.cms.CmsContentService;

import config.spring.ApplicationConfig;

/**
 * CmsContentTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("CmsContent test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class CmsContentTest {

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("search and hightliter test case")
    public void searchTest() {
        Date now = CommonUtils.getDate();
        String text = "你好天津黑核科技有限公司";
        CmsContent entity = new CmsContent((short) 1, text, 1, 1, "1", false, false, false, false, false, 0, 0, 0, 0, now, now, 0,
                CmsContentService.STATUS_NORMAL, false);
        entity.setDescription(text);
        contentService.save(entity);
        PageHandler page = contentService.query(false, true, true, (short) 1, "天津黑核科技有限公司", null, null, null, null, null, null,
                null, "<em>", "</em>", null, null, CommonUtils.getMinuteDate(), null, null, null);
        for (CmsContent site : (List<CmsContent>) page.getList()) {
            System.out.println(site.getTitle() + "\t" + site.getDescription());
        }
    }

    @Autowired
    private CmsContentService contentService;
}
