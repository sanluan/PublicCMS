package com.publiccms.logic.component.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.diy.CmsLayout;
import com.publiccms.views.pojo.diy.CmsLayoutData;
import com.publiccms.views.pojo.diy.CmsModule;
import com.publiccms.views.pojo.diy.CmsModuleData;
import com.publiccms.views.pojo.diy.CmsRegion;
import com.publiccms.views.pojo.diy.CmsRegionData;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

import freemarker.template.TemplateException;

/**
 * 元数据组件
 *
 * MetaData Component
 *
 */
@Component
public class DiyComponent implements SiteCache {
    protected final Log log = LogFactory.getLog(getClass());
    /**
    *
    */
    public static final String REGION_FILE = "diy-region.data";
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

    private CacheEntity<Short, Map<String, CmsRegionData>> regionDataCache;

    private CacheEntity<Short, Map<String, CmsRegion>> regionCache;

    private CacheEntity<Short, Map<String, CmsLayout>> layoutCache;

    private CacheEntity<Short, Map<String, CmsModule>> moduleCache;

    @Autowired
    private SiteComponent siteComponent;
    @Autowired
    private TemplateComponent templateComponent;
    @Autowired
    private MetadataComponent metadataComponent;

    /**
     * 获取区域列表
     * 
     * @param site
     *
     * @return region list
     */

    public List<CmsRegion> getRegionList(SysSite site) {
        Map<String, CmsRegion> map = getRegionMap(site);
        if (null == map) {
            return new ArrayList<>();
        } else {
            return map.values().stream().collect(Collectors.toList());
        }
    }

    /**
     * 获取布局列表
     * 
     * @param site
     * @param region
     *
     * @return layout list
     */

    public List<CmsLayout> getLayoutList(SysSite site, String region) {
        Map<String, CmsLayout> map = getLayoutMap(site);
        if (null == map) {
            return new ArrayList<>();
        } else {
            return map.values().stream().filter(layout -> {
                return CommonUtils.empty(region) || CommonUtils.empty(layout.getRegion()) || region.equals(layout.getRegion());
            }).collect(Collectors.toList());
        }
    }

    /**
     * 获取模块列表
     * 
     * @param site
     * @param region
     *
     * @return module list
     */

    public List<CmsModule> getModuleList(SysSite site, String region) {
        Map<String, CmsModule> map = getModuleMap(site);
        if (null == map) {
            return null;
        } else {
            return map.values().stream().filter(module -> {
                return CommonUtils.empty(region) || CommonUtils.empty(module.getRegion()) || region.equals(module.getRegion());
            }).collect(Collectors.toList());
        }
    }

    /**
     * 获取区域元数据
     * 
     * @param site
     * @param id
     *
     * @return region
     */

    public CmsRegion getRegion(SysSite site, String id) {
        return getRegionMap(site).get(id);
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
     * 获取DIY数据
     * 
     * @param site
     * @param id
     *
     * @return diy data
     */

    public CmsRegionData getRegionData(SysSite site, String id) {
        return getRegionDataMap(site).get(id);
    }

    /**
     * 更新区域数据
     * 
     * @param site
     * @param region
     * @return whether the update is successful
     */
    public boolean updateRegion(SysSite site, CmsRegion region) {
        Map<String, CmsRegion> map = getRegionMap(site);
        map.put(region.getId(), region);
        try {
            saveRegion(site, map);
            return true;
        } catch (IOException e) {
            return false;
        }
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
     * @param regionData
     * @return whether the update is successful
     */
    public boolean updateRegionData(SysSite site, CmsRegionData regionData) {
        Map<String, CmsRegionData> map = getRegionDataMap(site);
        map.put(regionData.getId(), regionData);
        try {
            saveRegionData(site, map);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除区域
     * 
     * @param site
     * @param id
     *
     * @return whether the delete is successful
     */
    public CmsRegion deleteRegion(SysSite site, String id) {
        Map<String, CmsRegion> map = getRegionMap(site);
        CmsRegion value = map.remove(id);
        try {
            saveRegion(site, map);
            return value;
        } catch (IOException e) {
            return null;
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
     * @param id
     * @return whether the delete is successful
     */
    public CmsRegionData deleteRegionData(SysSite site, String id) {
        Map<String, CmsRegionData> map = getRegionDataMap(site);
        CmsRegionData value = map.remove(id);
        try {
            saveRegionData(site, map);
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
    private Map<String, CmsRegion> getRegionMap(SysSite site) {
        Map<String, CmsRegion> metadataMap = regionCache.get(site.getId());
        if (null == metadataMap) {
            File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, REGION_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    metadataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsRegion.class));
                } catch (IOException | ClassCastException e) {
                    metadataMap = new CaseInsensitiveMap<>();
                }
            } else {
                metadataMap = new CaseInsensitiveMap<>();
            }
            regionCache.put(site.getId(), metadataMap);
        }
        return metadataMap;
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
            File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, LAYOUT_FILE));
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
            File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, MODULE_FILE));
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
     * @param category
     * @param diydataString
     * @return diydata
     */
    public CmsRegionData getRegionData(SysSite site, CmsCategory category, String diydataString) {
        try {
            List<Map<String, Object>> datalist = CommonConstants.objectMapper.readValue(diydataString,
                    CommonConstants.objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Map.class));
            if (null != datalist && 1 == datalist.size()) {
                Map<String, Object> map = datalist.get(0);
                if (null != map) {
                    return parseRegion(site, category, map);
                }
            }
            return null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private List<CmsModuleData> parseModuleList(SysSite site, CmsCategory category, Map<String, Object> moduleListMap) {
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
                    moduleData.setPlace((String) modulMap.get("place"));
                    if (CommonUtils.empty(moduleData.getPlace())) {
                        CmsModule module = getModule(site, id);
                        if (module.isClone()) {
                            try {
                                String placePath = templateComponent.generatePlaceFilePath(module.getFilePath(), category,
                                        modulMap);
                                String destFilepath = siteComponent.getTemplateFilePath(site,
                                        TemplateComponent.INCLUDE_DIRECTORY + placePath);
                                if (!CmsFileUtils.exists(destFilepath)) {
                                    String filepath = siteComponent.getTemplateFilePath(site,
                                            TemplateComponent.INCLUDE_DIRECTORY + module.getPlace());
                                    CmsFileUtils.createFile(destFilepath, CmsFileUtils.getFileContent(filepath));
                                    CmsPlaceMetadata metadata = metadataComponent.getPlaceMetadata(filepath);
                                    metadata.setAlias(moduleData.getName());
                                    metadataComponent.updatePlaceMetadata(destFilepath, metadata);
                                    moduleData.setPlace(placePath);
                                }
                            } catch (IOException | TemplateException e) {
                                log.error(e);
                            }
                        } else {
                            moduleData.setPlace(module.getPlace());
                        }
                    }
                    moduleDataList.add(moduleData);
                }
            }
            return moduleDataList;
        }
        return null;
    }

    private CmsLayoutData parseLayout(SysSite site, CmsCategory category, Map<String, Object> layoutMap) {
        if (null != (String) layoutMap.get("id")) {
            CmsLayoutData layoutData = new CmsLayoutData();
            layoutData.setId((String) layoutMap.get("id"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> layoutModuleListItems = (List<Map<String, Object>>) layoutMap.get("items");
            if (null != layoutModuleListItems) {
                List<List<CmsModuleData>> layoutModuleDataList = new ArrayList<>();
                for (Map<String, Object> moduleListMap : layoutModuleListItems) {
                    layoutModuleDataList.add(parseModuleList(site, category, moduleListMap));
                }
                layoutData.setModuleList(layoutModuleDataList);
            }
            return layoutData;
        }
        return null;
    }

    private CmsRegionData parseRegion(SysSite site, CmsCategory category, Map<String, Object> map) {
        CmsRegionData data = getRegionData(site, (String) map.get("id"));
        if (null == data) {
            data = new CmsRegionData();
        }
        data.setId((String) map.get("id"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) map.get("items");
        if (null != items) {
            List<CmsLayoutData> layoutList = new ArrayList<>();
            for (Map<String, Object> layoutMap : items) {
                CmsLayoutData layoutData = parseLayout(site, category, layoutMap);
                if (null != layoutData) {
                    layoutList.add(layoutData);
                }
            }
            if (null != category) {
                if (null == data.getCategoryLayoutMap()) {
                    data.setCategoryLayoutMap(new HashMap<>());
                }
                data.getCategoryLayoutMap().put(category.getId(), layoutList);
            } else {
                data.setLayoutList(layoutList);
            }
        }
        return data;
    }

    /**
     * 获取DIY数据
     *
     * @param site
     * @return template metadata map
     */
    private Map<String, CmsRegionData> getRegionDataMap(SysSite site) {
        Map<String, CmsRegionData> dataMap = regionDataCache.get(site.getId());
        if (null == dataMap) {
            File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, DATA_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    dataMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsRegionData.class));
                } catch (IOException | ClassCastException e) {
                    dataMap = new CaseInsensitiveMap<>();
                }
            } else {
                dataMap = new CaseInsensitiveMap<>();
            }
            regionDataCache.put(site.getId(), dataMap);
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
    private void saveRegion(SysSite site, Map<String, CmsRegion> map)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, REGION_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            CommonConstants.objectMapper.writeValue(file, map);
        }
        regionCache.remove(site.getId());
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
        File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, LAYOUT_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
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
        File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, MODULE_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
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
    private void saveRegionData(SysSite site, Map<String, CmsRegionData> map)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(siteComponent.getTemplateFilePath() + SiteComponent.getFullTemplatePath(site, DATA_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            CommonConstants.objectMapper.writeValue(file, map);
        }
        regionDataCache.remove(site.getId());
    }

    @Override
    public void clear(short siteId) {
        regionDataCache.remove(siteId);
        regionCache.remove(siteId);
        layoutCache.remove(siteId);
        moduleCache.remove(siteId);
    }

    @Override
    public void clear() {
        regionDataCache.clear();
        regionCache.clear();
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
        regionDataCache = cacheEntityFactory.createCacheEntity("regionDataCache");
        regionCache = cacheEntityFactory.createCacheEntity("diyRegionCache");
        layoutCache = cacheEntityFactory.createCacheEntity("diyLayoutCache");
        moduleCache = cacheEntityFactory.createCacheEntity("diyModuleCache");
    }

}
