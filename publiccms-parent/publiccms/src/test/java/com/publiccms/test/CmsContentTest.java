package com.publiccms.test;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.base.HighLighterQuery;
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
        HighLighterQuery highLighterQuery = new HighLighterQuery(true);
        highLighterQuery.setPreTag("<em>");
        highLighterQuery.setPostTag("</em>");
        PageHandler page = contentService.query((short) 1, false, true, highLighterQuery, "天津黑核科技有限公司",
                new String[] { "title", "description" }, null, 1, false, null, new String[] { "1" }, null, null, null,
                CommonUtils.getMinuteDate(), null, null, null);
        for (CmsContent content : (List<CmsContent>) page.getList()) {
            System.out.println(content.getTitle() + "\t" + content.getDescription());
        }
    }

    @Autowired
    private CmsContentService contentService;
}
