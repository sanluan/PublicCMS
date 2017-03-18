package com.publiccms.logic.service.cms;

// Generated 2016-11-20 14:50:55 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsDictionaryData;
import com.publiccms.logic.dao.cms.CmsDictionaryDataDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsDictionaryDataService extends BaseService<CmsDictionaryData> {

    @Transactional(readOnly = true)
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(
                pageIndex, pageSize);
    }
    
    @Autowired
    private CmsDictionaryDataDao dao;
}