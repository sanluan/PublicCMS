package com.publiccms.logic.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUserSetting;
import com.publiccms.entities.sys.SysUserSettingId;

/**
 *
 * SysUserSettingService
 * 
 */
@Service
@Transactional
public class SysUserSettingService extends BaseService<SysUserSetting> {
    public static final String OPTSECRET_SETTINGS_CODE = "otpsecret";
    public static final String SETTINGS_CODE_WEBAUTHN = "webauthn";

    /**
     * @param userId
     * @param code
     * @param data
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SysUserSetting getOrCreateOrUpdate(Long userId, String code, String data) {
        SysUserSettingId id = new SysUserSettingId(userId, code);
        SysUserSetting entity = getEntity(id);
        if (CommonUtils.notEmpty(data)) {
            if (null != entity) {
                entity.setData(data);
                entity.setUpdateDate(CommonUtils.getDate());
            } else if (CommonUtils.notEmpty(data)) {
                entity = new SysUserSetting(id, data, CommonUtils.getDate());
                save(entity);
            }
        }
        return entity;
    }
}