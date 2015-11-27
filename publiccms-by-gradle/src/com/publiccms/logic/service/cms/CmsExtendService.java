package com.publiccms.logic.service.cms;

// Generated 2015-5-8 16:50:23 by SourceMaker

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsExtend;
import com.publiccms.logic.dao.cms.CmsExtendDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsExtendService extends BaseService<CmsExtend> {

    @Autowired
    private CmsExtendDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer itemType, Integer itemId, Integer extendType, Boolean isCustom, Integer pageIndex, Integer pageSize) {
        return dao.getPage(itemType, itemId, extendType, isCustom, pageIndex, pageSize);
    }
}