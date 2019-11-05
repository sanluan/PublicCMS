package com.publiccms.logic.component.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Config;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.ExtendUtils;
import com.publiccms.entities.sys.SysConfigData;
import com.publiccms.entities.sys.SysConfigDataId;
import com.publiccms.entities.sys.SysExtendField;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.logic.service.sys.SysConfigDataService;
import com.publiccms.views.pojo.entities.SysConfig;

/**
 *
 * ConfigComponent 配置组件
 *
 */
@Component
public class ConfigComponent implements SiteCache {
    @Autowired
    private SysConfigDataService service;
    @Autowired(required = false)
    private List<Config> configPluginList;
    private CacheEntity<Short, Map<String, Map<String, String>>> cache;

    /**
     * @param site
     * @param code
     * @param locale
     * @return config
     */
    public ConfigInfo getConfig(SysSite site, String code, Locale locale) {
        Map<String, SysConfig> map = getMap(site);
        SysConfig entity = map.get(code);
        ConfigInfo configInfo = null;
        if (null != entity) {
            configInfo = new ConfigInfo(entity.getCode(), entity.getDescription());
            configInfo.setCustomed(true);
        }
        if (CommonUtils.notEmpty(configPluginList)) {
            for (Config configPlugin : configPluginList) {
                String configCode = configPlugin.getCode(site);
                if (null != configCode && configCode.equals(code)) {
                    configInfo = new ConfigInfo(code, configPlugin.getCodeDescription(locale));
                }
            }
        }
        return configInfo;
    }

    /**
     * @param site
     * @param locale
     * @param showAll
     * @return config list
     */
    public List<ConfigInfo> getConfigList(SysSite site, Locale locale, boolean showAll) {
        List<ConfigInfo> configList = new ArrayList<>();
        List<String> configCodeList = new ArrayList<>();
        if (CommonUtils.notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String code = config.getCode(site, showAll);
                if (CommonUtils.notEmpty(code) && !configCodeList.contains(code)) {
                    configList.add(new ConfigInfo(code, config.getCodeDescription(locale)));
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

    /**
     * @param site
     * @param code
     * @param customed
     * @param locale
     * @return field list
     */
    public List<SysExtendField> getFieldList(SysSite site, String code, Boolean customed, Locale locale) {
        List<SysExtendField> fieldList = new ArrayList<>();
        if ((null == customed || !customed) && CommonUtils.notEmpty(configPluginList)) {
            for (Config config : configPluginList) {
                String configCode = config.getCode(site);
                if (null != configCode && configCode.equals(code)) {
                    List<SysExtendField> extendFieldList = config.getExtendFieldList(site, locale);
                    if (null != extendFieldList) {
                        fieldList.addAll(extendFieldList);
                    }
                }
            }
        }
        if (null == customed || customed) {
            SysConfig sysConfig = getMap(site).get(code);
            if (null != sysConfig && CommonUtils.notEmpty(sysConfig.getExtendList())) {
                fieldList.addAll(sysConfig.getExtendList());
            }
        }
        return fieldList;
    }

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

    @Autowired
    private SiteComponent siteComponent;

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

    /**
     * @param site
     * @return config map
     */
    public Map<String, SysConfig> getMap(SysSite site) {
        Map<String, SysConfig> modelMap;
        File file = new File(siteComponent.getConfigFilePath(site));
        if (CommonUtils.notEmpty(file)) {
            try {
                modelMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                        .constructMapLikeType(HashMap.class, String.class, SysConfig.class));
            } catch (IOException | ClassCastException e) {
                modelMap = new HashMap<>();
            }
        } else {
            modelMap = new HashMap<>();
        }
        return modelMap;
    }

    /**
     * 保存配置
     *
     * @param site
     * @param modelMap
     * @return whether to save successfully
     */
    public boolean save(SysSite site, Map<String, SysConfig> modelMap) {
        File file = new File(siteComponent.getConfigFilePath(site));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(outputStream, modelMap);
        } catch (IOException e) {
            return false;
        }
        return true;
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
        cache.clear();
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
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        cache = cacheEntityFactory.createCacheEntity("config");
    }

    /**
     *
     * ConfigInfo
     *
     */
    public class ConfigInfo implements java.io.Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String code;
        private String description;
        private boolean customed;

        /**
         * @param code
         * @param description
         */
        public ConfigInfo(String code, String description) {
            this.code = code;
            this.description = description;
        }

        /**
         * @return code
         */
        public String getCode() {
            return code;
        }

        /**
         * @param code
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * @return description
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * @return customed
         */
        public boolean isCustomed() {
            return customed;
        }

        /**
         * @param customed
         */
        public void setCustomed(boolean customed) {
            this.customed = customed;
        }
    }
}
