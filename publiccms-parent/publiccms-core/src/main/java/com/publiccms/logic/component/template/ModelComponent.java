package com.publiccms.logic.component.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.entities.CmsModel;

import jakarta.annotation.Resource;

/**
 *
 * ModelComponent 模型操作组件
 *
 */
@Component
public class ModelComponent implements SiteCache {
    protected final Log log = LogFactory.getLog(getClass());

    private CacheEntity<Short, Map<String, CmsModel>> modelCache;
    private CacheEntity<Short, Map<String, CmsCategoryType>> typeCache;
    /**
     *
     */
    public static final String MODEL_LINK = "link";
    @Resource
    private SiteComponent siteComponent;

    /**
     * @param site
     * @param parentId
     * @param queryAll
     * @param hasChild
     * @param onlyUrl
     * @param hasImages
     * @param hasFiles
     * @return models list
     */
    public List<CmsModel> getModelList(SysSite site, String parentId, boolean queryAll, Boolean hasChild, Boolean onlyUrl,
            Boolean hasImages, Boolean hasFiles) {
        List<CmsModel> modelList = new ArrayList<>();
        Map<String, CmsModel> map = getModelMap(site);
        for (CmsModel model : map.values()) {
            if ((CommonUtils.empty(parentId) && (queryAll || CommonUtils.empty(model.getParentId()))
                    || CommonUtils.notEmpty(parentId) && parentId.equals(model.getParentId()))
                    || (null != hasChild && hasChild.equals(model.isHasChild()))
                    || (null != onlyUrl && onlyUrl.equals(model.isOnlyUrl()))
                    || (null != hasImages && hasImages.equals(model.isHasImages()))
                    || (null != hasFiles && hasFiles.equals(model.isHasFiles()))) {
                modelList.add(model);
            }
        }
        return modelList;
    }

    /**
     * @param siteId
     * @return category types list
     */
    public List<CmsCategoryType> getCategoryTypeList(short siteId) {
        Map<String, CmsCategoryType> map = getCategoryTypeMap(siteId);
        List<CmsCategoryType> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(CmsCategoryType::getSort));
        return list;
    }

    /**
     * @param site
     * @param modelId
     * @return model
     */
    public CmsModel getModel(SysSite site, String modelId) {
        return getModelMap(site).get(modelId);
    }

    /**
     * @param site
     * @return model map
     */
    public Map<String, CmsModel> getModelMap(SysSite site) {
        short siteId = getSiteId(site);
        Map<String, CmsModel> modelMap = modelCache.get(siteId);
        if (null == modelMap) {
            File file = new File(siteComponent.getModelFilePath(siteId));
            if (CommonUtils.notEmpty(file)) {
                try {
                    modelMap = Constants.objectMapper.readValue(file, Constants.objectMapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, CmsModel.class));
                } catch (IOException | ClassCastException e) {
                    modelMap = new HashMap<>();
                }
            } else {
                modelMap = new HashMap<>();
            }
            modelCache.put(siteId, modelMap);
        }
        return modelMap;
    }

    /**
     * @param siteId
     * @param typeId
     * @return category type
     */
    public CmsCategoryType getCategoryType(short siteId, String typeId) {
        return getCategoryTypeMap(siteId).get(typeId);
    }

    /**
     * @param siteId
     * @return model map
     */
    public Map<String, CmsCategoryType> getCategoryTypeMap(short siteId) {
        Map<String, CmsCategoryType> typeMap = typeCache.get(siteId);
        if (null == typeMap) {
            File file = new File(siteComponent.getCategoryTypeFilePath(siteId));
            if (CommonUtils.notEmpty(file)) {
                try {
                    typeMap = Constants.objectMapper.readValue(file, Constants.objectMapper.getTypeFactory()
                            .constructMapType(HashMap.class, String.class, CmsCategoryType.class));
                } catch (IOException | ClassCastException e) {
                    typeMap = new HashMap<>();
                }
            } else {
                typeMap = new HashMap<>();
            }
            typeCache.put(siteId, typeMap);
        }
        return typeMap;
    }

    /**
     * 保存模型
     *
     * @param siteId
     * @param modelMap
     * @return whether the save is successful
     */
    public boolean saveModel(short siteId, Map<String, CmsModel> modelMap) {
        File file = new File(siteComponent.getModelFilePath(siteId));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Constants.objectMapper.writeValue(file, modelMap);
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        modelCache.remove(siteId);
        return true;
    }

    /**
     * 保存模型
     * 
     * @param siteId
     * @param typeMap
     * @return whether the save is successful
     */
    public boolean saveCategoryType(short siteId, Map<String, CmsCategoryType> typeMap) {
        File file = new File(siteComponent.getCategoryTypeFilePath(siteId));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            Constants.objectMapper.writeValue(file, typeMap);
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        typeCache.remove(siteId);
        return true;
    }

    private short getSiteId(SysSite site) {
        return null == site.getParentId() ? site.getId() : site.getParentId();
    }

    @Override
    public void clear() {
        modelCache.clear(false);
        typeCache.clear(false);
    }

    @Override
    public void clear(short siteId) {
        modelCache.remove(siteId);
        typeCache.remove(siteId);
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
        modelCache = cacheEntityFactory.createCacheEntity("cmsModel");
        typeCache = cacheEntityFactory.createCacheEntity("cmsCategoryType");
    }
}
