package com.publiccms.logic.dao.sys;

// Generated 2025-03-17 09:19:15 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUserSetting;

/**
 *
 * SysUserSettingDao
 * 
 */
@Repository
public class SysUserSettingDao extends BaseDao<SysUserSetting> {

    @Override
    protected SysUserSetting init(SysUserSetting entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}