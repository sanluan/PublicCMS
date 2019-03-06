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
import com.publiccms.views.pojo.entities.CmsModel;

/**
 *
 * ModelComponent 模型操作组件
 *
 */
@Component
public class ModelComponent implements SiteCache {

    private CacheEntity<Short, Map<String, CmsModel>> modelCache;
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
    public List<CmsModel> getList(SysSite site, String parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles) {
        List<CmsModel> modelList = new ArrayList<>();
        Map<String, CmsModel> map = getMap(site);
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
     * @return model map
     */
    public Map<String, CmsModel> getMap(SysSite site) {
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
     * 保存模型
     *
     * @param site
     * @param modelMap
     * @return whether the save is successful
     */
    public boolean save(SysSite site, Map<String, CmsModel> modelMap) {
        File file = new File(siteComponent.getModelFilePath(site));
        if (CommonUtils.empty(file)) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(file);) {
            CommonConstants.objectMapper.writeValue(file, modelMap);
        } catch (IOException e) {
            return false;
        }
        clear(site.getId());
        return true;
    }

    @Override
    public void clear() {
        modelCache.clear();
    }

    @Override
    public void clear(short siteId) {
        modelCache.remove(siteId);
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
    }
}
