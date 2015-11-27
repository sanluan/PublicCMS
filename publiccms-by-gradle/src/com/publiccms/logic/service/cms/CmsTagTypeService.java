package com.publiccms.logic.service.cms;

// Generated 2015-7-10 16:36:23 by SourceMaker


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsTagType;
import com.publiccms.logic.dao.cms.CmsTagTypeDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsTagTypeService extends BaseService<CmsTagType> {

    @Autowired
    private CmsTagTypeDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(
                pageIndex, pageSize);
    }
}