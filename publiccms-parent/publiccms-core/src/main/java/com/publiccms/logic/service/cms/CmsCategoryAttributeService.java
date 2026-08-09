package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategory;
import com.publiccms.entities.cms.CmsCategoryAttribute;
import com.publiccms.logic.dao.cms.CmsCategoryDao;

import jakarta.annotation.Resource;

/**
 *
 * CmsCategoryAttributeService
 * 
 */
@Service
@Transactional
public class CmsCategoryAttributeService extends BaseService<CmsCategoryAttribute> {
    private String[] ignoreProperties = new String[] { "categoryId" };
    private String[] seoIgnoreProperties = new String[] { "categoryId", "data" };

    @Resource
    private CmsCategoryDao categoryDao;

    /**
     * @param siteId
     * @param entityList
     */
    public void updateSeo(short siteId, List<CmsCategoryAttribute> entityList) {
        if (CommonUtils.notEmpty(entityList)) {

            List<Serializable> categoryIdList = new ArrayList<>();
            for (CmsCategoryAttribute entity : entityList) {
                categoryIdList.add(Integer.valueOf(entity.getCategoryId()));
            }
            List<CmsCategory> categoryList = categoryDao.getEntitys(categoryIdList);
            for (CmsCategory category : categoryList) {
                if (siteId != category.getSiteId()) {
                    categoryIdList.remove(category.getId());
                }
            }

            List<CmsCategoryAttribute> unsaveList = null;
            for (CmsCategoryAttribute entity : entityList) {
                if (categoryIdList.contains(Integer.valueOf(entity.getCategoryId()))) {
                    if (null == update(entity.getCategoryId(), entity, seoIgnoreProperties)) {
                        if (null == unsaveList) {
                            unsaveList = new ArrayList<>();
                        }
                        unsaveList.add(entity);
                    }
                }
            }
            if (null != unsaveList) {
                save(unsaveList);
            }
        }
    }

    /**
     * @param categoryId
     * @param entity
     */
    public void updateAttribute(Integer categoryId, CmsCategoryAttribute entity) {
        CmsCategoryAttribute attribute = getEntity(categoryId);
        if (null != attribute) {
            if (null != entity) {
                update(attribute.getCategoryId(), entity, ignoreProperties);
            } else {
                delete(attribute.getCategoryId());
            }
        } else {
            entity.setCategoryId(categoryId);
            save(entity);
        }
    }
}