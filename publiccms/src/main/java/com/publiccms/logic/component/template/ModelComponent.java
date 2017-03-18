package com.publiccms.logic.component.template;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.api.SiteCache;
import com.publiccms.entities.sys.SysSite;
import com.publiccms.logic.component.site.SiteComponent;
import com.publiccms.views.pojo.CmsModel;
import com.sanluan.common.api.Json;
import com.sanluan.common.base.Base;
import com.sanluan.common.cache.CacheEntity;
import com.sanluan.common.cache.CacheEntityFactory;

/**
 *
 * ModelComponent 模型操作组件
 *
 */
@Component
public class ModelComponent extends Base implements SiteCache,Json {

    private CacheEntity<Integer, Map<String, CmsModel>> modelCache;
    @Autowired
    private SiteComponent siteComponent;

    public List<CmsModel> getList(SysSite site, String parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles) {
        List<CmsModel> modelList = new ArrayList<CmsModel>();
        Map<String, CmsModel> map = getMap(site);
        for (CmsModel model : map.values()) {
            if ((empty(parentId) && empty(model.getParentId()) || notEmpty(parentId) && parentId.equals(model.getParentId()))
                    || (notEmpty(hasChild) && hasChild.equals(model.isHasChild()))
                    || (notEmpty(onlyUrl) && onlyUrl.equals(model.isOnlyUrl()))
                    || (notEmpty(hasImages) && hasImages.equals(model.isHasImages()))
                    || (notEmpty(hasFiles) && hasFiles.equals(model.isHasFiles()))) {
                modelList.add(model);
            }
        }
        return modelList;
    }

    public Map<String, CmsModel> getMap(SysSite site) {
        Map<String, CmsModel> modelMap = modelCache.get(site.getId());
        if (empty(modelMap)) {
            File file = new File(siteComponent.getModelFilePath(site));
            if (notEmpty(file)) {
                try {
                    modelMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsModel>>() {
                    });
                } catch (IOException | ClassCastException e) {
                    modelMap = new HashMap<String, CmsModel>();
                }
            } else {
                modelMap = new HashMap<String, CmsModel>();
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
     * @return
     */
    public boolean save(SysSite site, Map<String, CmsModel> modelMap) {
        File file = new File(siteComponent.getModelFilePath(site));
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        try {
            objectMapper.writeValue(file, modelMap);
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
    public void clear(int siteId) {
        modelCache.remove(siteId);
    }

    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) {
        modelCache = cacheEntityFactory.createCacheEntity("cmsModel");
    }
}
