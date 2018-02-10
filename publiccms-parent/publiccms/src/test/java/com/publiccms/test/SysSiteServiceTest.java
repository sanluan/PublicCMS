package com.publiccms.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.tools.SqlService;
import com.publiccms.test.config.CmsTestConfig;

/**
 *
 * SysSiteServiceTest
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CmsTestConfig.class)
@Transactional
public class SysSiteServiceTest {
    @Autowired
    CmsContentService cmsService;

    @Autowired
    SysSiteService siteService;

    @Autowired
    SqlService sqlService;

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void searchTest() {
        PageHandler page = cmsService.query((short) 1, "啊", null, null, null, null, null, null, CommonUtils.getDate(), null,
                null);
        for (CmsContent site : (List<CmsContent>) page.getList()) {
            System.out.println(site.getTitle());
        }
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void queryTest() {
        PageHandler page = siteService.getPage(null, null, null, null);
        for (SysSite site : (List<SysSite>) page.getList()) {
            System.out.println(site.getName());
        }
    }

    /**
     * 
     */
    @Test
    public void mybatisTest() {
        List<Map<String, Object>> list = sqlService.select("select * from sys_site");
        for (Map<String, Object> map : list) {
            System.out.println(map.get("name"));
        }
    }
}
