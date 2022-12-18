package com.publiccms.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import javax.annotation.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.logic.service.cms.CmsContentService;

import config.spring.ApplicationConfig;

/**
 * CmsContentTest https://junit.org/junit5/docs/current/user-guide/
 * 
 */
@DisplayName("counter test case")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class CounterTest {
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    @BeforeAll
    public static void init() {
        // 不进入安装程序 数据目录有 database.properties才能进行测试
        CmsVersion.setInitialized(true);
        CmsVersion.setScheduled(false);
    }

    /**
     * 
     */
    @Test
    @DisplayName("counter test case")
    public void searchTest() {
        for (int i = 0; i < 2; i++) {
            pool.execute(new CounterTestTask(6834047297580437504L, contentService));
        }
        try {
            pool.awaitTermination(2, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool.isShutdown()) {
            pool.shutdown();
        }
    }

    @Resource
    private CmsContentService contentService;
}

class CounterTestTask implements Runnable {
    CmsContentService contentService;
    Long contentId;

    public CounterTestTask(Long contentId, CmsContentService contentService) {
        this.contentId = contentId;
        this.contentService = contentService;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 1000; i++) {
            contentService.updateScores((short) 1, contentId, 1, 1);
        }
    }

}