package com.publiccms.logic.component.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.LanguagesUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.views.pojo.entities.ExtendField;

/**
 *
 * ConfigComponent 配置组件
 *
 */
@Component
public class CorsConfigComponent implements SiteCache, Config {
    private static final String CONFIG_ALLOWED_ORIGINS = "allowed_origins";
    private static final String CONFIG_ALLOWED_METHODS = "allowed_methods";
    private static final String CONFIG_ALLOWED_HEADERS = "allowed_headers";
    private static final String CONFIG_EXPOSED_HEADERS = "exposed_headers";
    private static final String CONFIG_ALLOW_CREDENTIALS = "allow_credentials";
    private static final String CONFIG_MAXAGE = "max_age";

    private CacheEntity<Short, CorsConfiguration> cache;

    @Autowired
    private ConfigComponent configComponent;

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    public CorsConfiguration getConfig(SysSite site) {
        CorsConfiguration config = cache.get(site.getId());
        if (null == config) {
            Map<String, String> configData = configComponent.getConfigData(site.getId(), CONFIG_CODE_SITE);
            config = new CorsConfiguration();
            config.applyPermitDefaultValues();
            if (CommonUtils.notEmpty(configData)) {
                config = new CorsConfiguration();
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_ORIGINS))) {
                    config.setAllowedOrigins(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_ALLOWED_ORIGINS), CommonConstants.COMMA_DELIMITED)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_METHODS))) {
                    config.setAllowedMethods(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_ALLOWED_METHODS), CommonConstants.COMMA_DELIMITED)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_HEADERS))) {
                    config.setExposedHeaders(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_ALLOWED_HEADERS), CommonConstants.COMMA_DELIMITED)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_EXPOSED_HEADERS))) {
                    config.setExposedHeaders(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_EXPOSED_HEADERS), CommonConstants.COMMA_DELIMITED)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOW_CREDENTIALS))) {
                    config.setAllowCredentials("true".equals(configData.get(CONFIG_ALLOW_CREDENTIALS)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOW_CREDENTIALS))) {
                    try {
                        config.setMaxAge(Long.parseLong(configData.get(CONFIG_MAXAGE)));
                    } catch (NumberFormatException e) {

                    }
                }
            }
            cache.put(site.getId(), config);
        }
        return config;
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        cache = cacheEntityFactory.createCacheEntity("cors");
    }

    @Override
    public List<ExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<ExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new ExtendField(CONFIG_ALLOWED_ORIGINS, INPUTTYPE_TEXT, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALLOWED_ORIGINS),
                null, "*"));
        extendFieldList.add(new ExtendField(CONFIG_ALLOWED_METHODS, INPUTTYPE_TEXT, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALLOWED_METHODS),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_ALLOWED_HEADERS, INPUTTYPE_TEXT, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALLOWED_HEADERS),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_EXPOSED_HEADERS, INPUTTYPE_TEXT, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_EXPOSED_HEADERS),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_ALLOW_CREDENTIALS, INPUTTYPE_BOOLEAN, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_ALLOW_CREDENTIALS),
                null, null));
        extendFieldList.add(new ExtendField(CONFIG_MAXAGE, INPUTTYPE_TEXT, false,
                LanguagesUtils.getMessage(CommonConstants.applicationContext, locale,
                        CONFIG_CODE_DESCRIPTION + CommonConstants.DOT + CONFIG_MAXAGE),
                null, null));
        return extendFieldList;
    }

}
