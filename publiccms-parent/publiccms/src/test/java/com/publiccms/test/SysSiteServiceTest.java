package com.publiccms.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.tools.SqlService;

import config.spring.ApplicationConfig;

/**
 *
 * SysSiteServiceTest
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class SysSiteServiceTest {
    @Autowired
    private CmsContentService cmsService;
    @Autowired
    private SysSiteService siteService;
    @Autowired
    private SqlService sqlService;

    @BeforeClass
    public static void init() {
        // 不进入安装程序
        CmsVersion.setInitialized(true);
        // 数据目录地址，此目录中应该有 database.properties
        CommonConstants.CMS_FILEPATH = "D:\\Users\\repository\\PublicCMS\\data\\publiccms";
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void searchTest() {
        Date now = CommonUtils.getDate();
        String text = "你好天津黑核科技有限公司";
        CmsContent entity = new CmsContent((short) 1, text, 1, 1, "1", false, false, false, false, false, 0, 0, 0, 0, now, now, 0,
                CmsContentService.STATUS_NORMAL, false);
        entity.setDescription(text);
        cmsService.save(entity);
        PageHandler page = cmsService.query(false, true, true, (short) 1, "天津黑核科技有限公司", null, null, null, null, null, null, null,
                "<em>", "</em>", null, null, CommonUtils.getMinuteDate(), null, null, null);
        for (CmsContent site : (List<CmsContent>) page.getList()) {
            System.out.println(site.getTitle() + "\t" + site.getDescription());
        }
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void queryTest() {
        PageHandler page = siteService.getPage(null, null, null, null, null);
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
