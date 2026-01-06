package com.publiccms.logic.component.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.Cache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.views.pojo.entities.CmsPageData;
import com.publiccms.views.pojo.entities.CmsPageMetadata;
import com.publiccms.views.pojo.entities.CmsPlaceMetadata;

/**
 * 元数据组件
 *
 * Metadata Component
 *
 */
@Component
public class MetadataComponent implements Cache {
    /**
     *
     */
    public static final String METADATA_FILE = "metadata.data";
    /**
     *
     */
    public static final String DATA_FILE = "data.data";

    private CacheEntity<String, Map<String, CmsPageMetadata>> pageCache;
    private CacheEntity<String, Map<String, CmsPageData>> pageDataCache;
    private CacheEntity<String, Map<String, CmsPlaceMetadata>> placeCache;

    /**
     * 获取推荐位元数据
     *
     * @param realFilepath
     * @return place metadata
     */
    public CmsPlaceMetadata getPlaceMetadata(String realFilepath) {
        File file = new File(realFilepath);
        CmsPlaceMetadata pageMetadata = getPlaceMetadataMap(file.getParent()).get(file.getName());
        if (null != pageMetadata) {
            return pageMetadata;
        }
        return new CmsPlaceMetadata();
    }

    /**
     * 获取模板元数据
     *
     * @param realFilepath
     * @return template metadata
     */
    public CmsPageMetadata getTemplateMetadata(String realFilepath) {
        File file = new File(realFilepath);
        CmsPageMetadata pageMetadata = getTemplateMetadataMap(file.getParent()).get(file.getName());
        if (null == pageMetadata) {
            pageMetadata = new CmsPageMetadata();
            pageMetadata.setUseDynamic(true);
        }
        return pageMetadata;
    }

    /**
     * 获取模板数据
     *
     * @param realFilepath
     * @param lang
     * @return template metadata
     */
    public CmsPageData getTemplateData(String realFilepath, String lang) {
        File file = new File(realFilepath);
        CmsPageData pageMetadata = getTemplateDataMap(file.getParent(), lang).get(file.getName());
        if (null == pageMetadata) {
            pageMetadata = new CmsPageData();
        }
        return pageMetadata;
    }

    /**
     * 更新模板元数据
     *
     * @param realFilepath
     * @param metadata
     * @return whether the update is successful
     */
    public boolean updateTemplateMetadata(String realFilepath, CmsPageMetadata metadata) {
        File file = new File(realFilepath);
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
     * 更新模板元数据
     *
     * @param realFilepath
     * @param lang
     * @param data
     * @return whether the update is successful
     */
    public boolean updateTemplateData(String realFilepath, String lang, CmsPageData data) {
        File file = new File(realFilepath);
        String dirPath = file.getParent();
        Map<String, CmsPageData> dataMap = getTemplateDataMap(dirPath, lang);
        dataMap.put(file.getName(), data);
        try {
            saveTemplateData(dirPath, lang, dataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 更新推荐位元数据
     *
     * @param realFilepath
     * @param metadata
     * @return whether the update is successful
     */
    public boolean updatePlaceMetadata(String realFilepath, CmsPlaceMetadata metadata) {
        File file = new File(realFilepath);
        String dirPath = file.getParent();
        placeCache.remove(dirPath);
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
     * 删除模板数据
     *
     * @param realFilepath
     * @param lang
     * @return whether the delete is successful
     */
    public boolean deleteTemplateData(String realFilepath, String lang) {
        File file = new File(realFilepath);
        String dirPath = file.getParent();
        Map<String, CmsPageData> dataMap = getTemplateDataMap(dirPath, lang);
        dataMap.remove(file.getName());
        try {
            saveTemplateData(dirPath, lang, dataMap);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除模板元数据
     *
     * @param realFilepath
     * @return whether the delete is successful
     */
    public boolean deleteTemplateMetadata(String realFilepath) {
        File file = new File(realFilepath);
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
     * @param realFilepath
     * @return whether the delete is successful
     */
    public boolean deletePlaceMetadata(String realFilepath) {
        File file = new File(realFilepath);
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
     * 获取页面片段目录元数据
     *
     * @param dirPath
     * @return place metadata map
     */
    private Map<String, CmsPlaceMetadata> getPlaceMetadataMap(String dirPath) {
        Map<String, CmsPlaceMetadata> metadataMap = placeCache.get(dirPath);
        if (null == metadataMap) {
            File file = new File(CommonUtils.joinString(dirPath, Constants.SEPARATOR, METADATA_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    metadataMap = Constants.objectMapper.readValue(file, Constants.objectMapper.getTypeFactory()
                            .constructMapType(CaseInsensitiveMap.class, String.class, CmsPlaceMetadata.class));
                } catch (IOException | ClassCastException e) {
                    metadataMap = new CaseInsensitiveMap<>();
                }
            } else {
                metadataMap = new CaseInsensitiveMap<>();
            }
            placeCache.put(dirPath, metadataMap);
        }
        return metadataMap;
    }

    /**
     * 获取目录元数据
     *
     * @param dirPath
     * @return template metadata map
     */
    private Map<String, CmsPageMetadata> getTemplateMetadataMap(String dirPath) {
        Map<String, CmsPageMetadata> metadataMap = pageCache.get(dirPath);
        if (null == metadataMap) {
            File file = new File(CommonUtils.joinString(dirPath, Constants.SEPARATOR, METADATA_FILE));
            if (CommonUtils.notEmpty(file)) {
                try {
                    metadataMap = Constants.objectMapper.readValue(file, Constants.objectMapper.getTypeFactory()
                            .constructMapType(CaseInsensitiveMap.class, String.class, CmsPageMetadata.class));
                } catch (IOException | ClassCastException e) {
                    metadataMap = new CaseInsensitiveMap<>();
                }
            } else {
                metadataMap = new CaseInsensitiveMap<>();
            }
            pageCache.put(dirPath, metadataMap);
        }
        return metadataMap;
    }

    /**
     * 获取目录数据
     *
     * @param dirPath
     * @param lang
     * @return template metadata map
     */
    private Map<String, CmsPageData> getTemplateDataMap(String dirPath, String lang) {
        Map<String, CmsPageData> dataMap = pageDataCache.get(dirPath);
        if (null == dataMap) {
            String fileName = null;
            if (CommonUtils.notEmpty(lang)) {
                fileName = CommonUtils.joinString(dirPath, Constants.SEPARATOR, lang, Constants.UNDERLINE, DATA_FILE);
            } else {
                fileName = CommonUtils.joinString(dirPath, Constants.SEPARATOR, DATA_FILE);
            }
            File file = new File(fileName);
            if (CommonUtils.notEmpty(file)) {
                try {
                    dataMap = Constants.objectMapper.readValue(file, Constants.objectMapper.getTypeFactory()
                            .constructMapLikeType(CaseInsensitiveMap.class, String.class, CmsPageData.class));
                } catch (IOException | ClassCastException e) {
                    dataMap = new CaseInsensitiveMap<>();
                }
            } else {
                dataMap = new CaseInsensitiveMap<>();
            }
            pageDataCache.put(dirPath, dataMap);
        }
        return dataMap;
    }

    /**
     * 保存模板数据
     *
     * @param dirPath
     * @param dataMap
     * @throws IOException
     */
    private void saveTemplateData(String dirPath, String lang, Map<String, CmsPageData> dataMap) throws IOException {
        String fileName = null;
        if (CommonUtils.notEmpty(lang)) {
            fileName = CommonUtils.joinString(dirPath, Constants.SEPARATOR, lang, Constants.UNDERLINE, DATA_FILE);
        } else {
            fileName = CommonUtils.joinString(dirPath, Constants.SEPARATOR, DATA_FILE);
        }
        File file = new File(fileName);
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Constants.objectMapper.writeValue(file, dataMap);
        }
        pageDataCache.clear(false);
    }

    /**
     * 保存模板元数据
     *
     * @param dirPath
     * @param metadataMap
     * @throws IOException
     */
    private void saveTemplateMetadata(String dirPath, Map<String, CmsPageMetadata> metadataMap) throws IOException {
        File file = new File(CommonUtils.joinString(dirPath, Constants.SEPARATOR, METADATA_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Constants.objectMapper.writeValue(file, metadataMap);
        }
        pageCache.clear(false);
    }

    /**
     * 保存推荐位元数据
     *
     * @param dirPath
     * @param metadataMap
     * @throws IOException
     */
    private void savePlaceMetadata(String dirPath, Map<String, CmsPlaceMetadata> metadataMap) throws IOException {
        File file = new File(CommonUtils.joinString(dirPath, Constants.SEPARATOR, METADATA_FILE));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        Constants.objectMapper.writeValue(file, metadataMap);
        placeCache.clear(false);
    }

    @Override
    public void clear() {
        placeCache.clear(false);
        pageCache.clear(false);
        pageDataCache.clear(false);
    }

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    @Resource
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        pageCache = cacheEntityFactory.createCacheEntity("pageMetadata");
        pageDataCache = cacheEntityFactory.createCacheEntity("pageDataMetadata");
        placeCache = cacheEntityFactory.createCacheEntity("placeMetadata");
    }
}
