package com.publiccms.logic.component.template;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.publiccms.common.api.Cache;
import com.publiccms.views.pojo.CmsPageMetadata;
import com.publiccms.views.pojo.CmsPlaceMetadata;
import com.sanluan.common.api.Json;
import com.sanluan.common.base.Base;
import com.sanluan.common.cache.CacheEntity;
import com.sanluan.common.cache.CacheEntityFactory;

@Component
public class MetadataComponent extends Base implements Cache, Json {
    public static final String METADATA_FILE = "metadata.data";

    private CacheEntity<String, Map<String, CmsPageMetadata>> pageCache;
    private CacheEntity<String, Map<String, CmsPlaceMetadata>> placeCache;

    /**
     * 获取推荐位元数据
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public CmsPlaceMetadata getPlaceMetadata(String filePath) {
        File file = new File(filePath);
        CmsPlaceMetadata pageMetadata = getPlaceMetadataMap(file.getParent()).get(file.getName());
        if (null != pageMetadata) {
            return pageMetadata;
        }
        return new CmsPlaceMetadata();
    }

    /***
     * 获取模板元数据**
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public CmsPageMetadata getTemplateMetadata(String filePath) {
        return getTemplateMetadata(filePath, false);
    }

    /**
     * 获取模板元数据
     * 
     * @param filePath
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    public CmsPageMetadata getTemplateMetadata(String filePath, boolean allowNullValue) {
        File file = new File(filePath);
        CmsPageMetadata pageMetadata = getTemplateMetadataMap(file.getParent()).get(file.getName());
        if (null != pageMetadata) {
            return pageMetadata;
        }
        if (allowNullValue) {
            return null;
        } else {
            return new CmsPageMetadata();
        }
    }

    /**
     * 更新模板元数据
     * 
     * @param filePath
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public boolean updateTemplateMetadata(String filePath, CmsPageMetadata metadata) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPageMetadata> metadataMap = getTemplateMetadataMap(dirPath);
        metadataMap.put(file.getName(), metadata);
        try {
            saveTemplateMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 更新推荐位元数据
     * 
     * @param filePath
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public boolean updatePlaceMetadata(String filePath, CmsPlaceMetadata metadata) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPlaceMetadata> metadataMap = getPlaceMetadataMap(dirPath);
        metadataMap.put(file.getName(), metadata);
        try {
            savePlaceMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除模板元数据
     * 
     * @param filePath
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public boolean deleteTemplateMetadata(String filePath) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPageMetadata> metadataMap = getTemplateMetadataMap(dirPath);
        metadataMap.remove(file.getName());
        try {
            saveTemplateMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除推荐位元数据
     * 
     * @param filePath
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public boolean deletePlaceMetadata(String filePath) {
        File file = new File(filePath);
        String dirPath = file.getParent();
        Map<String, CmsPlaceMetadata> metadataMap = getPlaceMetadataMap(dirPath);
        metadataMap.remove(file.getName());
        try {
            savePlaceMetadata(dirPath, metadataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取目录元数据
     * 
     * @param dirPath
     * @return
     */
    public Map<String, CmsPlaceMetadata> getPlaceMetadataMap(String dirPath) {
        Map<String, CmsPlaceMetadata> metadataMap = placeCache.get(dirPath);
        if (empty(metadataMap)) {
            File file = new File(dirPath + SEPARATOR + METADATA_FILE);
            if (notEmpty(file)) {
                try {
                    metadataMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsPlaceMetadata>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    metadataMap = new HashMap<String, CmsPlaceMetadata>();
                }
            } else {
                metadataMap = new HashMap<String, CmsPlaceMetadata>();
            }
            placeCache.put(dirPath, metadataMap);
        }
        return metadataMap;
    }

    /**
     * 获取目录元数据
     * 
     * @param dirPath
     * @return
     */
    public Map<String, CmsPageMetadata> getTemplateMetadataMap(String dirPath) {
        Map<String, CmsPageMetadata> metadataMap = pageCache.get(dirPath);
        if (empty(metadataMap)) {
            File file = new File(dirPath + SEPARATOR + METADATA_FILE);
            if (notEmpty(file)) {
                try {
                    metadataMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsPageMetadata>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    metadataMap = new HashMap<String, CmsPageMetadata>();
                }
            } else {
                metadataMap = new HashMap<String, CmsPageMetadata>();
            }
            pageCache.put(dirPath, metadataMap);
        }
        return metadataMap;
    }

    /**
     * 保存模板元数据
     * 
     * @param dirPath
     * @param metadataMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void saveTemplateMetadata(String dirPath, Map<String, CmsPageMetadata> metadataMap)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(dirPath + SEPARATOR + METADATA_FILE);
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        objectMapper.writeValue(file, metadataMap);
        pageCache.clear();
    }

    /**
     * 保存推荐位元数据
     * 
     * @param dirPath
     * @param metadataMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private void savePlaceMetadata(String dirPath, Map<String, CmsPlaceMetadata> metadataMap)
            throws JsonGenerationException, JsonMappingException, IOException {
        File file = new File(dirPath + SEPARATOR + METADATA_FILE);
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        objectMapper.writeValue(file, metadataMap);
        placeCache.clear();
    }

    @Override
    public void clear() {
        placeCache.clear();
        pageCache.clear();
    }

    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) {
        pageCache = cacheEntityFactory.createCacheEntity("pageMetadata");
        placeCache = cacheEntityFactory.createCacheEntity("placeMetadata");
    }
}
