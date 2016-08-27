package com.publiccms.logic.service.cms;

// Generated 2016-2-26 15:57:04 by com.sanluan.common.source.SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsCategoryType;
import com.publiccms.logic.dao.cms.CmsCategoryTypeDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsCategoryTypeService extends BaseService<CmsCategoryType> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, pageIndex, pageSize);
    }

    public CmsCategoryType updateExtendId(Integer id, Integer extendId) {
        CmsCategoryType entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setExtendId(extendId);
        }
        return entity;
    }

    @Autowired
    private CmsCategoryTypeDao dao;
}