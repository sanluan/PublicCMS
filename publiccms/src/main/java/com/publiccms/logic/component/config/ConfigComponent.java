package com.publiccms.logic.component.config;

import static com.publiccms.common.tools.ExtendUtils.getExtendMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.views.pojo.ExtendField;
import com.publiccms.views.pojo.SysConfig;
import com.sanluan.common.api.Json;
import com.sanluan.common.base.Base;
import com.sanluan.common.cache.CacheEntity;
import com.sanluan.common.cache.CacheEntityFactory;

/**
 * 
 * ConfigComponent 配置组件
 *
 */
@Component
public class ConfigComponent extends Base implements SiteCache, Json {
    @Autowired
    private SysConfigDataService service;
    @Autowired(required = false)
    private List<Config> configPluginList;
    private CacheEntity<Integer, Map<String, Map<String, String>>> cache;

    public ConfigInfo getConfig(SysSite site, String code, Locale locale) {
        Map<String, SysConfig> map = getMap(site);
        SysConfig entity = map.get(code);
        ConfigInfo configInfo = null;
        if (null != entity) {
            configInfo = new ConfigInfo(entity.getCode(), entity.getDescription());
            configInfo.setCustomed(true);
        } 
        if (notEmpty(configPluginList)) {
            for (Config configPlugin : configPluginList) {
                if (configPlugin.getCode(site).equals(code)) {
                    configInfo = new ConfigInfo(code, configPlugin.getCodeDescription(site, locale));
                }
            }
        }
        return configInfo;
    }

    public List<ConfigInfo> getConfigList(SysSite site, Locale locale) {
        List<ConfigInfo> configList = new ArrayList<ConfigInfo>();
        List<String> configCodeList = new ArrayList<String>();
        if (notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String code = config.getCode(site);
                if (!configCodeList.contains(code)) {
                    configList.add(new ConfigInfo(config.getCode(site), config.getCodeDescription(site, locale)));
                    configCodeList.add(code);
                }
            }
        }
        for (Entry<String, SysConfig> entry : getMap(site).entrySet()) {
            if (!configCodeList.contains(entry.getKey())) {
                ConfigInfo configInfo = new ConfigInfo(entry.getKey(), entry.getValue().getDescription());
                configInfo.setCustomed(true);
                configList.add(configInfo);
                configCodeList.add(entry.getKey());
            }
        }
        return configList;
    }

    public List<ExtendField> getFieldList(SysSite site, String code, Boolean customed, Locale locale) {
        List<ExtendField> fieldList = new ArrayList<ExtendField>();
        if ((empty(customed) || !customed) && notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                if (config.getCode(site).equals(code)) {
                    fieldList.addAll(config.getExtendFieldList(site, locale));
                }
            }
        }
        if ((empty(customed) || customed)) {
            SysConfig sysConfig = getMap(site).get(code);
            if (null != sysConfig && notEmpty(sysConfig.getExtendList())) {
                fieldList.addAll(sysConfig.getExtendList());
            }
        }
        return fieldList;
    }

    public Map<String, String> getConfigData(int siteId, String code) {
        Map<String, Map<String, String>> siteMap = cache.get(siteId);
        if (empty(siteMap)) {
            siteMap = new HashMap<String, Map<String, String>>();
        }
        Map<String, String> configMap = siteMap.get(code);
        if (empty(configMap)) {
            SysConfigData entity = service.getEntity(new SysConfigDataId(siteId, code));
            if (null != entity && notEmpty(entity.getData())) {
                configMap = getExtendMap(entity.getData());
            } else {
                configMap = new HashMap<String, String>();
            }
            siteMap.put(code, configMap);
            cache.put(siteId, siteMap);
        }
        return configMap;
    }

    @Autowired
    private SiteComponent siteComponent;

    public Map<String, SysConfig> getMap(SysSite site) {
        Map<String, SysConfig> modelMap;
        File file = new File(siteComponent.getConfigFilePath(site));
        if (notEmpty(file)) {
            try {
                modelMap = objectMapper.readValue(file, new TypeReference<Map<String, SysConfig>>() {
                });
            } catch (IOException | ClassCastException e) {
                modelMap = new HashMap<String, SysConfig>();
            }
        } else {
            modelMap = new HashMap<String, SysConfig>();
        }
        return modelMap;
    }

    /**
     * 保存配置
     * 
     * @param site
     * @param modelMap
     * @return
     */
    public boolean save(SysSite site, Map<String, SysConfig> modelMap) {
        File file = new File(siteComponent.getConfigFilePath(site));
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        try {
            objectMapper.writeValue(file, modelMap);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void removeCache(int siteId, String code) {
        Map<String, Map<String, String>> map = cache.get(siteId);
        if (notEmpty(map)) {
            map.remove(code);
        }
    }

    @Override
    public void clear(int siteId) {
        cache.remove(siteId);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) {
        cache = cacheEntityFactory.createCacheEntity("config");
    }

    public class ConfigInfo implements java.io.Serializable {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String code;
        private String description;
        private boolean customed;

        public ConfigInfo(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isCustomed() {
            return customed;
        }

        public void setCustomed(boolean customed) {
            this.customed = customed;
        }
    }
}
