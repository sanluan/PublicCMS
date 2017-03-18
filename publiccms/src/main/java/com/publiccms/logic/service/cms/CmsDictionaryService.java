package com.publiccms.logic.service.cms;

// Generated 2016-11-20 14:50:37 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsDictionary;
import com.publiccms.logic.dao.cms.CmsDictionaryDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class CmsDictionaryService extends BaseService<CmsDictionary> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean multiple, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(multiple, 
                pageIndex, pageSize);
    }
    
    @Autowired
    private CmsDictionaryDao dao;
}