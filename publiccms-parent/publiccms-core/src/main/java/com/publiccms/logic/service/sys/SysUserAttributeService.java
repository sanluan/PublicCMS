package com.publiccms.logic.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.entities.sys.SysUserAttribute;

/**
 *
 * SysUserAttributeService
 * 
 */
@Service
@Transactional
public class SysUserAttributeService extends BaseService<SysUserAttribute> {

    protected static final String[] ignoreProperties = new String[] { "userId" };

    /**
     * @param userId
     * @param entity
     */
    public void updateAttribute(Long userId, SysUserAttribute entity) {
        SysUserAttribute attribute = getEntity(userId);
        if (null != attribute) {
            if (null != entity) {
                update(attribute.getUserId(), entity, ignoreProperties);
            } else {
                delete(attribute.getUserId());
            }
        } else {
            if (null != entity) {
                entity.setUserId(userId);
                save(entity);
            }
        }
    }
}