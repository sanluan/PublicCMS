package com.publiccms.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import jakarta.annotation.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.drew.imaging.FileType;
import com.drew.imaging.FileTypeDetector;
import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsContent;
import com.publiccms.logic.service.cms.CmsContentService;
import com.publiccms.views.pojo.query.CmsContentQuery;

import config.spring.ApplicationConfig;

@DisplayName("Content batch import")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ContentCoverCatch {
    @Resource
    private CmsContentService contentService;

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
    @DisplayName("insert test case")
    public void insertTest() {
        CmsContentQuery query = new CmsContentQuery();
        query.setHasCover(true);
        PageHandler page = contentService.getPage(query, null, null, null, null, null, 500, null);
        while (!page.isLastPage()) {
            for (CmsContent entity : (List<CmsContent>) page.getList()) {
                if (CommonUtils.notEmpty(entity.getCover()) && entity.getCover().contains("http")) {
                    entity.setCover(getUrl(entity.getCover()));
                }
                contentService.update(entity.getId(), entity);
            }
            page = contentService.getPage(query, null, null, null, null, page.getNextPage(), 500, null);
        }
        for (CmsContent entity : (List<CmsContent>) page.getList()) {
            if (CommonUtils.notEmpty(entity.getCover()) && entity.getCover().contains("http")) {
                entity.setCover(getUrl(entity.getCover()));
            }
            contentService.update(entity.getId(), entity);
        }
    }

    public static String getUrl(String url) {
        String fileName = null;
        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(CommonConstants.defaultRequestConfig)
                .build()) {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                BufferedInputStream inputStream = new BufferedInputStream(entity.getContent());
                FileType fileType = FileTypeDetector.detectFileType(inputStream);
                String suffix = fileType.getCommonExtension();
                if (null != fileType.getMimeType() && fileType.getMimeType().startsWith("image/")
                        && CommonUtils.notEmpty(suffix)) {
                    fileName = CmsFileUtils.getUploadFileName(suffix);
                    String filePath = "D:/aaa/" + fileName;
                    CmsFileUtils.copyInputStreamToFile(inputStream, filePath, suffix);
                }
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return getUrl(url);
        }
        return fileName;

    }

}
