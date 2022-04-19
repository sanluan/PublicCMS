package com.publiccms.logic.service.cms;

import com.publiccms.entities.cms.CmsCategoryAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;

/**
 *
 * CmsCategoryAttributeService
 * 
 */
@Service
@Transactional
public class CmsCategoryAttributeService extends BaseService<CmsCategoryAttribute> {
    private String[] ignoreProperties = new String[] { "categoryId" };

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