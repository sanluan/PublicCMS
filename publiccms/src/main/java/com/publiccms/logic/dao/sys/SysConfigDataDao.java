package com.publiccms.logic.dao.sys;

// Generated 2016-7-16 11:54:15 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysConfigData;
import com.sanluan.common.base.BaseDao;

@Repository
public class SysConfigDataDao extends BaseDao<SysConfigData> {

    @Override
    protected SysConfigData init(SysConfigData entity) {
        return entity;
    }

}