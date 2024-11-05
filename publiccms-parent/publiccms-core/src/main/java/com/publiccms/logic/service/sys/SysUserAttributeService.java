package com.publiccms.logic.service.sys;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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

    public static final String OPTSECRET_SETTINGS_CODE = "otpsecret";
    public static final String SETTINGS_CODE_WEBAUTHN = "webauthn";

    @Override
    public List<SysUserAttribute> getEntitys(Serializable[] ids) {
        return basedao.getEntitys(ids);
    }

    /**
     * @param userId
     * @param settings
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
     * @param data 
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateAttribute(Long userId, String data) {
        SysUserAttribute attribute = getEntity(userId);
        if (null != attribute) {
            attribute.setData(data);
        } else if (CommonUtils.notEmpty(data)) {
            attribute = new SysUserAttribute();
            attribute.setUserId(userId);
            attribute.setSettings(data);
            save(attribute);
        }
    }
}