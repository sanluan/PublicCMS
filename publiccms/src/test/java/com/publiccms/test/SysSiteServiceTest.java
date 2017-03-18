package com.publiccms.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.service.sys.SysSiteService;
import com.publiccms.test.config.CmsTestConfig;
import com.sanluan.common.handler.PageHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CmsTestConfig.class)
@Transactional
public class SysSiteServiceTest {
    @Autowired
    SysSiteService service;

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        PageHandler page = service.getPage(null, null, null, null);
        for (SysSite site : (List<SysSite>) page.getList()) {
            System.out.println(site.getName());
        }
    }
}
