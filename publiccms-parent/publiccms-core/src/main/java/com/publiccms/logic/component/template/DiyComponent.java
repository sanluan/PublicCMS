package com.publiccms.logic.component.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CmsFileUtils;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.BeanComponent;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.diy.CmsDiyData;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsLayoutData;
import com.publiccms.views.pojo.diy.CmsModule;
import com.publiccms.views.pojo.diy.CmsModuleData;
import com.publiccms.views.pojo.diy.CmsRegionData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

/**
 * 元数据组件
 *
 * MetaData Component
 *
 */
@Component
public class DiyComponent implements SiteCache {
    /**
    *
    */
    public static final String LAYOUT_FILE = "diy-layout.data";
    /**
    *
    */
    public static final String MODULE_FILE = "diy-module.data";
    /**
     *
     */
    public static final String DATA_FILE = "diy-data.data";

    private CacheEntity<Short, Map<String, CmsDiyData>> diyDataCache;

    private CacheEntity<Short, Map<String, CmsLayout>> layoutCache;

    private CacheEntity<Short, Map<String, CmsModule>> moduleCache;

    @Autowired
    private SiteComponent siteComponent;

    /**
     * 获取布局列表
     * 
     * @param site
     * @param id
     *
     * @return region
     */

    public List<CmsLayout> getLayoutList(SysSite site) {
        Map<String, CmsLayout> map = getLayoutMap(site);
        if (null == map) {
            return new ArrayList<>();
        } else {
            return map.values().stream().collect(Collectors.toList());
        }
    }

    /**
     * 获取模块列表
     * 
     * @param site
     * @param id
     *
     * @return region
     */

    public List<CmsModule> getModuleList(SysSite site) {
        Map<String, CmsModule> map = getModuleMap(site);
        if (null == map) {
            return null;
        } else {
            return map.values().stream().collect(Collectors.toList());
        }
    }

    /**
     * 获取区域元数据
     * 
     * @param site
     * @param id
     *
     * @return layout
     */

    public CmsLayout getLayout(SysSite site, String id) {
        return getLayoutMap(site).get(id);
    }

    /**
     * 获取区域元数据
     * 
     * @param site
     * @param id
     *
     * @return module
     */

    public CmsModule getModule(SysSite site, String id) {
        return getModuleMap(site).get(id);
    }

    /**
     * 获取区域元数据
     * 
     * @param site
     * @param filepath
     *
     * @return diy data
     */

    public CmsDiyData getDiyData(SysSite site, String filepath) {
        return getDiyDataMap(site).get(filepath);
    }

    /**
     * 更新布局数据
     * 
     * @param site
     * @param layout
     * @return whether the update is successful
     */
    public boolean updateLayout(SysSite site, CmsLayout layout) {
        Map<String, CmsLayout> map = getLayoutMap(site);
        map.put(layout.getId(), layout);
        try {
            saveLayout(site, map);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 更新模块数据
     * 
     * @param site
     * @param module
     * @return whether the update is successful
     */
    public boolean updateModule(SysSite site, CmsModule module) {
        Map<String, CmsModule> map = getModuleMap(site);
        map.put(module.getId(), module);
        try {
            saveModule(site, map);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 更新DIY数据
     * 
     * @param site
     * @param diydata
     * @return whether the update is successful
     */
    public boolean updateDiyData(SysSite site, CmsDiyData diydata) {
        Map<String, CmsDiyData> map = getDiyDataMap(site);
        map.put(diydata.getPath(), diydata);
        try {
            saveDiyData(site, map);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除布局
     * 
     * @param site
     * @param id
     *
     * @return whether the delete is successful
     */
    public CmsLayout deleteLayout(SysSite site, String id) {
        Map<String, CmsLayout> map = getLayoutMap(site);
        CmsLayout value = map.remove(id);
        try {
            saveLayout(site, map);
            return value;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 删除模块
     * 
     * @param site
     * @param id
     *
     * @return whether the delete is successful
     */
    public CmsModule deleteModule(SysSite site, String id) {
        Map<String, CmsModule> map = getModuleMap(site);
        CmsModule value = map.remove(id);
        try {
            saveModule(site, map);
            return value;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 删除DIY数据
     * 
     * @param site
     * @param filepath
     * @return whether the delete is successful
     */
    public CmsDiyData deleteDiyData(SysSite site, String filepath) {
        Map<String, CmsDiyData> map = getDiyDataMap(site);
        CmsDiyData value = map.remove(filepath);
        try {
            saveDiyData(site, map);
            return value;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 布局数据
     *
     * @param site
     * @return layout map
     */
    private Map<String, CmsLayout> getLayoutMap(SysSite site) {
        Map<String, CmsLayout> metadataMap = layoutCache.get(site.getId());
        if (null == metadataMap) {
            File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, LAYOUT_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    metadataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsLayout.class));
                } catch (IOException | ClassCastException e) {
                    metadataMap = new CaseInsensitiveMap<>();
                }
            } else {
                metadataMap = new CaseInsensitiveMap<>();
            }
            layoutCache.put(site.getId(), metadataMap);
        }
        return metadataMap;
    }

    /**
     * 模块数据
     *
     * @param site
     * @return module map
     */
    private Map<String, CmsModule> getModuleMap(SysSite site) {
        Map<String, CmsModule> metadataMap = moduleCache.get(site.getId());
        if (null == metadataMap) {
            File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, MODULE_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    metadataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsModule.class));
                } catch (IOException | ClassCastException e) {
                    metadataMap = new CaseInsensitiveMap<>();
                }
            } else {
                metadataMap = new CaseInsensitiveMap<>();
            }
            moduleCache.put(site.getId(), metadataMap);
        }
        return metadataMap;
    }

    /**
     * @param site
     * @param path
     * @param diydataString
     * @return diydata
     */
    public CmsDiyData getDiyData(SysSite site, String path, String diydataString) {
        try {
            List<Map<String, Object>> datalist = CommonConstants.objectMapper.readValue(diydataString,
                    CommonConstants.objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Map.class));
            CmsDiyData diydata = new CmsDiyData();
            diydata.setPath(path);
            if (null != datalist) {
                Map<String, CmsRegionData> regionMap = new HashMap<>();
                for (Map<String, Object> map : datalist) {
                    if (null != map) {
                        CmsRegionData data = parseRegion(site, map);
                        if (null != data && null != data.getLayoutList() && !data.getLayoutList().isEmpty()) {
                            regionMap.put(data.getId(), data);
                        }
                    }
                }
                diydata.setRegionMap(regionMap);
            }
            return diydata;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private List<CmsModuleData> parseModuleList(SysSite site, Map<String, Object> moduleListMap) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> moduleListItems = (List<Map<String, Object>>) moduleListMap.get("items");
        if (null != moduleListItems) {
            List<CmsModuleData> moduleDataList = new ArrayList<>();
            for (Map<String, Object> modulMap : moduleListItems) {
                String id = (String) modulMap.get("id");
                if (CommonUtils.notEmpty(id)) {
                    CmsModuleData moduleData = new CmsModuleData();
                    moduleData.setId(id);
                    moduleData.setName((String) modulMap.get("name"));
                    moduleData.setPath((String) modulMap.get("path"));
                    if (CommonUtils.empty(moduleData.getPath())) {
                        CmsModule module = getModule(site, id);
                        if (module.isClone()) {
                            String path = "/diy/" + UUID.randomUUID().toString() + ".html";
                            String filepath = siteComponent.getWebTemplateFilePath(site,
                                    TemplateComponent.INCLUDE_DIRECTORY + module.getPath());
                            try {
                                String destFilepath = siteComponent.getWebTemplateFilePath(site,
                                        TemplateComponent.INCLUDE_DIRECTORY + path);
                                CmsFileUtils.createFile(destFilepath, CmsFileUtils.getFileContent(filepath));
                                CmsPlaceMetadata metadata = BeanComponent.getMetadataComponent().getPlaceMetadata(filepath);
                                metadata.setAlias(moduleData.getName());
                                BeanComponent.getMetadataComponent().updatePlaceMetadata(destFilepath, metadata);
                                moduleData.setPath(path);
                            } catch (IOException e) {
                            }
                        } else {
                            moduleData.setPath(module.getPath());
                        }
                    }
                    moduleDataList.add(moduleData);
                }
            }
            return moduleDataList;
        }
        return null;
    }

    private CmsLayoutData parseLayout(SysSite site, Map<String, Object> layoutMap) {
        if (null != (String) layoutMap.get("id")) {
            CmsLayoutData layoutData = new CmsLayoutData();
            layoutData.setId((String) layoutMap.get("id"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> layoutModuleListItems = (List<Map<String, Object>>) layoutMap.get("items");
            if (null != layoutModuleListItems) {
                List<List<CmsModuleData>> layoutModuleDataList = new ArrayList<>();
                for (Map<String, Object> moduleListMap : layoutModuleListItems) {
                    layoutModuleDataList.add(parseModuleList(site, moduleListMap));
                }
                layoutData.setModuleList(layoutModuleDataList);
            }
            return layoutData;
        }
        return null;
    }

    private CmsRegionData parseRegion(SysSite site, Map<String, Object> map) {
        CmsRegionData data = new CmsRegionData();
        data.setId((String) map.get("id"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
        if (null != items) {
            List<CmsLayoutData> layoutList = new ArrayList<>();
            for (Map<String, Object> layoutMap : items) {
                CmsLayoutData layoutData = parseLayout(site, layoutMap);
                if (null != layoutData) {
                    layoutList.add(layoutData);
                }
            }
            data.setLayoutList(layoutList);
        }
        return data;
    }

    /**
     * 获取DIY数据
     *
     * @param dirPath
     * @return template metadata map
     */
    private Map<String, CmsDiyData> getDiyDataMap(SysSite site) {
        Map<String, CmsDiyData> dataMap = diyDataCache.get(site.getId());
        if (null == dataMap) {
            File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, DATA_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    dataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsDiyData.class));
                } catch (IOException | ClassCastException e) {
                    dataMap = new CaseInsensitiveMap<>();
                }
            } else {
                dataMap = new CaseInsensitiveMap<>();
            }
            diyDataCache.put(site.getId(), dataMap);
        }
        return dataMap;
    }

    /**
     * 保存布局
     *
     * @param site
     * @param map
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void saveLayout(SysSite site, Map<String, CmsLayout> map)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, LAYOUT_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, map);
        }
        layoutCache.remove(site.getId());
    }

    /**
     * 保存模块
     *
     * @param site
     * @param map
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void saveModule(SysSite site, Map<String, CmsModule> map)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, MODULE_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, map);
        }
        moduleCache.remove(site.getId());
    }

    /**
     * 保存区域
     *
     * @param site
     * @param map
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void saveDiyData(SysSite site, Map<String, CmsDiyData> map)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(siteComponent.getWebTemplateFilePath() + SiteComponent.getFullTemplatePath(site, DATA_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, map);
        }
        diyDataCache.remove(site.getId());
    }

    @Override
    public void clear(short siteId) {
        diyDataCache.remove(siteId);
        layoutCache.remove(siteId);
        moduleCache.remove(siteId);
    }

    @Override
    public void clear() {
        diyDataCache.clear();
        layoutCache.clear();
        moduleCache.clear();
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
        diyDataCache = cacheEntityFactory.createCacheEntity("diyDataCache");
        layoutCache = cacheEntityFactory.createCacheEntity("diyLayoutCache");
        moduleCache = cacheEntityFactory.createCacheEntity("diyModuleCache");
    }

}
