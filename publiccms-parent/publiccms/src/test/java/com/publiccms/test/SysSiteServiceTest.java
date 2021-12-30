package com.publiccms.test;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.logic.service.tools.SqlService;

import config.spring.ApplicationConfig;

/**
 *
 * SysSiteServiceTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("SysSite test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class SysSiteServiceTest {
    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    @Autowired
    private SysSiteService siteService;
    @Autowired
    private SqlService sqlService;

    /**
     * 
     */
    @Test
    @DisplayName("site insert test case")
    public void insertTest() {
        SysSite entity = new SysSite("test", false, "/webfile/", false, "/", false);
        siteService.save(entity);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("site query test case")
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
    @DisplayName("mybatis query test case")
    public void mybatisTest() {
        List<Map<String, Object>> list = sqlService.select("select * from sys_site");
        for (Map<String, Object> map : list) {
            System.out.println(map.get("name"));
        }
    }
}
