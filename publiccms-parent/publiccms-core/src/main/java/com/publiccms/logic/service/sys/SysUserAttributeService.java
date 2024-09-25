package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
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
    public static final String OPTSECRET_SETTINGS_CODE = "otpsecret";

    @Override
    public List<SysUserAttribute> getEntitys(Serializable[] ids) {
        return basedao.getEntitys(ids);
    }

    /**
     * @param userId
     * @param settings
     */
    public void updateSettings(Long userId, String settings) {
        SysUserAttribute attribute = getEntity(userId);
        if (null != attribute) {
            attribute.setSettings(settings);
        } else if (CommonUtils.notEmpty(settings)) {
            attribute = new SysUserAttribute();
            attribute.setUserId(userId);
            attribute.setSettings(settings);
            save(attribute);
        }
    }

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
        } else if (null != entity) {
            entity.setUserId(userId);
            save(entity);
        }
    }
}