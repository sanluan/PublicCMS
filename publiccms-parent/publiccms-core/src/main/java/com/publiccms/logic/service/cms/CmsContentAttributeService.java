package com.publiccms.logic.service.cms;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.cms.CmsContentAttribute;

/**
 *
 * SysUserAttributeService
 * 
 */
@Service
@Transactional
public class CmsContentAttributeService extends BaseService<CmsContentAttribute> {

    protected static final String[] ignoreProperties = new String[] { "contentId" };

    /**
     * @param contentId
     * @param entity
     */
    public void updateAttribute(Long contentId, CmsContentAttribute entity) {
        CmsContentAttribute attribute = getEntity(contentId);
        if (null != attribute) {
            if (null != entity) {
                update(attribute.getContentId(), entity, ignoreProperties);
            } else {
                delete(attribute.getContentId());
            }
        } else {
            if (null != entity) {
                entity.setContentId(contentId);
                save(entity);
            }
        }
    }
}