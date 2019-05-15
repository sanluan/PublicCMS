package com.publiccms.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.logic.service.tools.HqlService;

import config.spring.ApplicationConfig;

/**
 *
 * SysSiteServiceTest
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class HqlTest {
    @Autowired
    HqlService hqlService;

    @BeforeClass
    public static void init() {
        // 不进入安装程序
        CmsVersion.setInitialized(true);
        // 数据目录地址，此目录中应该有 database.properties
        CommonConstants.CMS_FILEPATH = "E:\\repository\\PublicCMS\\data\\publiccms";
    }

    /**
     * 
     */
    @Test
    public void searchTest() {
        Map<String, Object> parameters = new HashMap<>();
        String hql = "select new com.publiccms.test.Bean(content.title,category.name) from CmsContent content,CmsCategory category where content.categoryId=category.id";
        PageHandler page = hqlService.getPage(hql, parameters, 1, 5);
        for (Object o : page.getList()) {
            System.out.println(o);
        }
    }
}

class Bean {
    String contentTitlle;
    String categoryName;

    public Bean(String contentTitlle, String categoryName) {
        super();
        this.contentTitlle = contentTitlle;
        this.categoryName = categoryName;
    }

    /**
     * @return the contentTitlle
     */
    public String getContentTitlle() {
        return contentTitlle;
    }

    /**
     * @param contentTitlle
     *            the contentTitlle to set
     */
    public void setContentTitlle(String contentTitlle) {
        this.contentTitlle = contentTitlle;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     *            the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Test [contentTitlle=" + contentTitlle + ", categoryName=" + categoryName + "]";
    }
}
