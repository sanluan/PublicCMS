package com.publiccms.logic.service.cms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.cms.CmsCategoryAttribute;

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

    /**
     * @param entityList
     */
    public void updateSeo(List<CmsCategoryAttribute> entityList) {
        if (CommonUtils.notEmpty(entityList)) {
            List<CmsCategoryAttribute> unsaveList = null;
            for (CmsCategoryAttribute entity : entityList) {
                if (null == update(entity.getCategoryId(), entity, seoIgnoreProperties)) {
                    if (null == unsaveList) {
                        unsaveList = new ArrayList<>();
                    }
                    unsaveList.add(entity);
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