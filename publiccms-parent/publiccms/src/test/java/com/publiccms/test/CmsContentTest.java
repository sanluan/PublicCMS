package com.publiccms.test;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.annotation.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.base.HighLighterQuery;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.query.CmsContentSearchQuery;

import config.spring.ApplicationConfig;

/**
 * CmsContentTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("CmsContent test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class CmsContentTest {
    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("search and hightliter test case")
    public void searchTest() {
        Date now = CommonUtils.getDate();
        String text = "你好天津黑核科技有限公司";
        CmsContent entity = new CmsContent((short) 1, text, 1, 1, "1", false, false, false, false, 0, now, now, 0,
                CmsContentService.STATUS_NORMAL);
        entity.setDescription(text);
        contentService.save(entity);
        HighLighterQuery highLighterQuery = new HighLighterQuery(true);
        highLighterQuery.setPreTag("<em>");
        highLighterQuery.setPostTag("</em>");
        PageHandler page = contentService.query(new CmsContentSearchQuery((short) 1, false, true, highLighterQuery, "天津黑核科技有限公司",
                null, new String[] { "title", "description" }, null, null, null, 1, null, new String[] { "1" }, null, null, null,
                null, null, CommonUtils.getMinuteDate()), false, null, null, null, null);
        for (CmsContent content : (List<CmsContent>) page.getList()) {
            System.out.println(content.getTitle() + "\t" + content.getDescription());
        }
    }

    @Resource
    private CmsContentService contentService;
}
