package com.publiccms.logic.component.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.logic.service.sys.SysConfigDataService;

import jakarta.annotation.Resource;

/**
 *
 * ConfigDataComponent 配置数据组件
 *
 */
@Component
public class ConfigDataComponent implements SiteCache {
    @Resource
    private SysConfigDataService service;
    private CacheEntity<Short, Map<String, Map<String, String>>> cache;

    /**
     * @param siteId
     * @param code
     * @return config data
     */
    public Map<String, String> getConfigData(short siteId, String code) {
        Map<String, Map<String, String>> siteMap = cache.get(siteId);
        if (null == siteMap) {
            siteMap = new HashMap<>();
        }
        Map<String, String> configMap = siteMap.get(code);
        if (null == configMap) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(siteId, code));
            if (null != entity && CommonUtils.notEmpty(entity.getData())) {
                configMap = ExtendUtils.getExtendMap(entity.getData());
            } else {
                configMap = new HashMap<>();
            }
            siteMap.put(code, configMap);
            cache.put(siteId, siteMap);
        }
        return configMap;
    }

    public static int getInt(String value, int defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static long getLong(String value, long defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    public static boolean getBoolean(String value, boolean defaultValue) {
        if (CommonUtils.empty(value)) {
            return defaultValue;
        } else {
            return "true".equalsIgnoreCase(value);
        }
    }

    /**
     * @param siteId
     * @param code
     */
    public void removeCache(short siteId, String code) {
        Map<String, Map<String, String>> map = cache.get(siteId);
        if (CommonUtils.notEmpty(map)) {
            map.remove(code);
        }
    }

    @Override
    public void clear(short siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear(false);
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, SecurityException {
        cache = cacheEntityFactory.createCacheEntity("config");
    }

}
