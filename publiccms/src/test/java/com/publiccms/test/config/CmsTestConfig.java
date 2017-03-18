package com.publiccms.test.config;

import org.springframework.context.annotation.Import;

import com.publiccms.common.constants.CmsVersion;

import config.spring.ApplicationConfig;

/**
 * <h1>CmsRootConfig</h1>
 * <p>
 * Cms跟配置类
 * </p>
 * <p>
 * Spring Config Class
 * </p>
 *
 */
@Import(ApplicationConfig.class)
public class CmsTestConfig {
    static {
        CmsVersion.setInitialized(true);
    }
}