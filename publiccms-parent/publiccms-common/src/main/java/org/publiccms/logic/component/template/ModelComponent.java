package org.publiccms.logic.component.template;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.publiccms.common.api.SiteCache;
import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.component.site.SiteComponent;
import org.publiccms.views.pojo.CmsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.publiccms.common.base.Base;
import com.publiccms.common.cache.CacheEntity;
import com.publiccms.common.cache.CacheEntityFactory;

/**
 *
 * ModelComponent 模型操作组件
 *
 */
@Component
public class ModelComponent implements SiteCache,Base {

    private CacheEntity<Integer, Map<String, CmsModel>> modelCache;
    @Autowired
    private SiteComponent siteComponent;

    /**
     * @param site
     * @param parentId
     * @param hasChild
     * @param onlyUrl
     * @param hasImages
     * @param hasFiles
     * @return
     */
    public List<CmsModel> getList(SysSite site, String parentId, Boolean hasChild, Boolean onlyUrl, Boolean hasImages,
            Boolean hasFiles) {
        List<CmsModel> modelList = new ArrayList<>();
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

    /**
     * @param site
     * @return
     */
    public Map<String, CmsModel> getMap(SysSite site) {
        Map<String, CmsModel> modelMap = modelCache.get(site.getId());
        if (empty(modelMap)) {
            File file = new File(siteComponent.getModelFilePath(site));
            if (notEmpty(file)) {
                try {
                    modelMap = objectMapper.readValue(file, new TypeReference<Map<String, CmsModel>>() {
                    });
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
     * @return
     */
    public boolean save(SysSite site, Map<String, CmsModel> modelMap) {
        File file = new File(siteComponent.getModelFilePath(site));
        if (empty(file)) {
            file.getParentFile().mkdirs();
        }
        try {
            objectMapper.writeValue(file, modelMap);
            file.setReadable(true, false);
            file.setWritable(true, false);
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

    /**
     * @param cacheEntityFactory
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws ClassNotFoundException 
     */
    @Autowired
    public void initCache(CacheEntityFactory cacheEntityFactory) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        modelCache = cacheEntityFactory.createCacheEntity("cmsModel");
    }
}
