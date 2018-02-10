package com.publiccms.test.config;

import java.beans.PropertyVetoException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.publiccms.common.constants.CmsVersion;
import com.publiccms.common.constants.CommonConstants;

import config.spring.ApplicationConfig;

/**
 * <h1>CmsRootConfig</h1>
 * <p>
 * Cms根配置类
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

    /**
     * @param env
     * @throws IOException
     * @throws PropertyVetoException
     */
    @Autowired
    public void init(Environment env) throws IOException, PropertyVetoException {
        CommonConstants.CMS_FILEPATH = env.getProperty("cms.filePath");
    }
}