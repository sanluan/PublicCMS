package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsModel;
import com.publiccms.logic.dao.cms.CmsModelDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsModelService extends BaseService<CmsModel> {

    @Autowired
    private CmsModelDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer parentId, Boolean hasChild, Boolean isUrl, Boolean isImages, Boolean isPart,
            Boolean disabled, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, hasChild, isUrl, isImages, isPart, disabled, pageIndex, pageSize);
    }

    @Override
    public CmsModel delete(Serializable id) {
        CmsModel entity = getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(true);
        }
        return entity;
    }
}