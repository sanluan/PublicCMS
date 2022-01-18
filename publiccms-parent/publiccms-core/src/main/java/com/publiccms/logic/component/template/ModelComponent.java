package com.publiccms.logic.component.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.publiccms.common.api.SiteCache;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.entities.CmsCategoryType;
import com.publiccms.views.pojo.entities.CmsModel;

/**
 *
 * ModelComponent 模型操作组件
 *
 */
@Component
public class ModelComponent implements SiteCache {

    private CacheEntity<Short, Map<String, CmsModel>> modelCache;
    private CacheEntity<Short, Map<String, CmsCategoryType>> typeCache;
    /**
     *
     */
    public static final String MODEL_LINK = "link";
    @Autowired
    private SiteComponent siteComponent;

    /**
     * @param site
     * @param parentId
     * @param hasChild
     * @param onlyUrl
     * @param hasImages
     * @param hasFiles
     * @return models list
     */
    public List<CmsModel> getModelList(SysSite site, String parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles) {
        List<CmsModel> modelList = new ArrayList<>();
        Map<String, CmsModel> map = getModelMap(site);
        for (CmsModel model : map.values()) {
            if ((CommonUtils.empty(parentId) && CommonUtils.empty(model.getParentId())
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
     * @param site
     * @return category types list
     */
    public List<CmsCategoryType> getCategoryTypeList(SysSite site) {
        Map<String, CmsCategoryType> map = getCategoryTypeMap(site);
        return new ArrayList<>(map.values());
    }

    /**
     * @param site
     * @return model map
     */
    public Map<String, CmsModel> getModelMap(SysSite site) {
        Map<String, CmsModel> modelMap = modelCache.get(site.getId());
        if (null == modelMap) {
            File file = new File(siteComponent.getModelFilePath(site));
            if (CommonUtils.notEmpty(file)) {
                try {
                    modelMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(HashMap.class, String.class, CmsModel.class));
                } catch (IOException | ClassCastException e) {
                    modelMap = new HashMap<>();
                }
            } else {
                modelMap = new HashMap<>();
            }
            modelCache.put(site.getId(), modelMap);
        }
        return modelMap;
    }

    /**
     * @param site
     * @return model map
     */
    public Map<String, CmsCategoryType> getCategoryTypeMap(SysSite site) {
        Map<String, CmsCategoryType> typeMap = typeCache.get(site.getId());
        if (null == typeMap) {
            File file = new File(siteComponent.getCategoryTypeFilePath(site));
            if (CommonUtils.notEmpty(file)) {
                try {
                    typeMap = CommonConstants.objectMapper.readValue(file, CommonConstants.objectMapper.getTypeFactory()
                            .constructMapLikeType(HashMap.class, String.class, CmsCategoryType.class));
                } catch (IOException | ClassCastException e) {
                    typeMap = new HashMap<>();
                }
            } else {
                typeMap = new HashMap<>();
            }
            typeCache.put(site.getId(), typeMap);
        }
        return typeMap;
    }

    /**
     * 保存模型
     *
     * @param site
     * @param modelMap
     * @return whether the save is successful
     */
    public boolean saveModel(SysSite site, Map<String, CmsModel> modelMap) {
        File file = new File(siteComponent.getModelFilePath(site));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, modelMap);
        } catch (IOException e) {
            return false;
        }
        modelCache.remove(site.getId());
        return true;
    }

    /**
     * 保存模型
     *
     * @param site
     * @param typeMap
     * @return whether the save is successful
     */
    public boolean saveCategoryType(SysSite site, Map<String, CmsCategoryType> typeMap) {
        File file = new File(siteComponent.getCategoryTypeFilePath(site));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, typeMap);
        } catch (IOException e) {
            return false;
        }
        typeCache.remove(site.getId());
        return true;
    }

    @Override
    public void clear() {
        modelCache.clear();
        typeCache.clear();
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
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        modelCache = cacheEntityFactory.createCacheEntity("cmsModel");
        typeCache = cacheEntityFactory.createCacheEntity("cmsCategoryType");
    }
}
