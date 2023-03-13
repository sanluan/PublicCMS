package com.publiccms.logic.component.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.site.SiteComponent;

/**
 *
 * CorsConfigComponent 跨域配置组件
 *
 */
@Component
public class CorsConfigComponent implements SiteCache, Config {
    /**
     * 
     */
    public static final String CONFIG_CODE = "cors";

    /**
     * 
     */
    public static final String CONFIG_CODE_DESCRIPTION = CommonUtils.joinString(CONFIGPREFIX, CONFIG_CODE);

    private static final String CONFIG_ALLOWED_ORIGINS = "allowed_origins";
    private static final String CONFIG_ALLOWED_METHODS = "allowed_methods";
    private static final String CONFIG_ALLOWED_HEADERS = "allowed_headers";
    private static final String CONFIG_EXPOSED_HEADERS = "exposed_headers";
    private static final String CONFIG_ALLOW_CREDENTIALS = "allow_credentials";
    private static final String CONFIG_MAXAGE = "max_age";

    private CacheEntity<Short, CorsConfiguration> cache;

    @Resource
    private SiteComponent siteComponent;

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
            Map<String, String> configData = BeanComponent.getConfigComponent().getConfigData(site.getId(), CONFIG_CODE);
            config = new CorsConfiguration();
            config.applyPermitDefaultValues();
            if (null != configData) {
                config = new CorsConfiguration();
                config.setAllowCredentials(ConfigComponent.getBoolean(configData.get(CONFIG_ALLOW_CREDENTIALS), true));

                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_ORIGINS))) {
                    String[] array = StringUtils.split(configData.get(CONFIG_ALLOWED_ORIGINS), CommonConstants.COMMA);
                    for (String p : array) {
                        if (p.contains(CorsConfiguration.ALL)) {
                            config.addAllowedOriginPattern(p);
                        } else {
                            config.addAllowedOrigin(p);
                        }
                    }
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_METHODS))) {
                    config.setAllowedMethods(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_ALLOWED_METHODS), CommonConstants.COMMA)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_ALLOWED_HEADERS))) {
                    config.setAllowedHeaders(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_ALLOWED_HEADERS), CommonConstants.COMMA)));
                }
                if (CommonUtils.notEmpty(configData.get(CONFIG_EXPOSED_HEADERS))) {
                    config.setExposedHeaders(Arrays
                            .asList(StringUtils.split(configData.get(CONFIG_EXPOSED_HEADERS), CommonConstants.COMMA)));
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
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        cache = cacheEntityFactory.createCacheEntity("cors", CacheEntityFactory.MEMORY_CACHE_ENTITY);
    }

    /**
     * @param siteId
     * @param showAll
     * @return config code or null
     */
    @Override
    public String getCode(short siteId, boolean showAll) {
        return CONFIG_CODE;
    }

    /**
     * @param locale
     * @return
     */
    @Override
    public String getCodeDescription(Locale locale) {
        return getMessage(locale, CONFIG_CODE_DESCRIPTION);
    }

    @Override
    public List<SysExtendField> getExtendFieldList(SysSite site, Locale locale) {
        List<SysExtendField> extendFieldList = new ArrayList<>();
        extendFieldList.add(new SysExtendField(CONFIG_ALLOWED_ORIGINS, INPUTTYPE_TEXT, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_ORIGINS)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_ORIGINS,
                        CONFIG_CODE_DESCRIPTION_SUFFIX)),
                "*"));
        extendFieldList.add(new SysExtendField(CONFIG_ALLOWED_METHODS, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_METHODS)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_METHODS,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_ALLOWED_HEADERS, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_HEADERS)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOWED_HEADERS,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_EXPOSED_HEADERS, INPUTTYPE_TEXT,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_EXPOSED_HEADERS)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_EXPOSED_HEADERS,
                        CONFIG_CODE_DESCRIPTION_SUFFIX))));
        extendFieldList.add(new SysExtendField(CONFIG_ALLOW_CREDENTIALS, INPUTTYPE_BOOLEAN, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOW_CREDENTIALS)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_ALLOW_CREDENTIALS,
                        CONFIG_CODE_DESCRIPTION_SUFFIX)),
                "true"));
        extendFieldList.add(new SysExtendField(CONFIG_MAXAGE, INPUTTYPE_TEXT, false,
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_MAXAGE)),
                getMessage(locale, CommonUtils.joinString(CONFIG_CODE_DESCRIPTION, CommonConstants.DOT, CONFIG_MAXAGE,
                        CONFIG_CODE_DESCRIPTION_SUFFIX)),
                "1800"));
        return extendFieldList;
    }

    @Override
    public boolean exportable() {
        return false;
    }
}
