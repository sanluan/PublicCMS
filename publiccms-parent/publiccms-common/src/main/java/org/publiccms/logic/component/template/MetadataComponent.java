package org.publiccms.logic.component.template;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.publiccms.common.api.Cache;
import org.publiccms.views.pojo.CmsPageMetadata;
import org.publiccms.views.pojo.CmsPlaceMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.publiccms.common.base.Base;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;

/**
 * 元数据组件
 * 
 * MetaData Component
 * 
 */
@Component
public class MetadataComponent implements Cache, Base {
    /**
     * 
     */
    public static final String METADATA_FILE = "metadata.data";

    private CacheEntity<String, Map<String, CmsPageMetadata>> pageCache;
    private CacheEntity<String, Map<String, CmsPlaceMetadata>> placeCache;

    /**
     * 获取推荐位元数据
     * 
     * @param filePath
     * @return
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
     */
    public CmsPageMetadata getTemplateMetadata(String filePath) {
        return getTemplateMetadata(filePath, false);
    }

    /**
     * 获取模板元数据
     * 
     * @param filePath
     * @param allowNullValue
     * @return
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
     * @param metadata
     * @return
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
     * @param metadata
     * @return
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
                    metadataMap = new HashMap<>();
                }
            } else {
                metadataMap = new HashMap<>();
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
                    metadataMap = new HashMap<>();
                }
            } else {
                metadataMap = new HashMap<>();
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
        file.setReadable(true, false);
        file.setWritable(true, false);
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
        file.setReadable(true, false);
        file.setWritable(true, false);
        placeCache.clear();
    }

    @Override
    public void clear() {
        placeCache.clear();
        pageCache.clear();
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        pageCache = cacheEntityFactory.createCacheEntity("pageMetadata");
        placeCache = cacheEntityFactory.createCacheEntity("placeMetadata");
    }
}
