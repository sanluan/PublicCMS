package org.publiccms.test.config;

import org.publiccms.common.constants.CmsVersion;
import org.springframework.context.annotation.Import;

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