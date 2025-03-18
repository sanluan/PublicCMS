package com.publiccms.logic.dao.sys;

import com.publiccms.entities.sys.SysConfigData;

// Generated 2016-7-16 11:54:15 by com.publiccms.common.generator.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.tools.CommonUtils;

/**
 *
 * SysConfigDataDao
 * 
 */
@Repository
public class SysConfigDataDao extends BaseDao<SysConfigData> {

    @Override
    protected SysConfigData init(SysConfigData entity) {
        if (null == entity.getCreateDate()) {
            entity.setCreateDate(CommonUtils.getDate());
        }
        return entity;
    }

}