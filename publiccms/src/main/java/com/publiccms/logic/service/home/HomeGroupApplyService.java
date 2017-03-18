package com.publiccms.logic.service.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeGroupApply;
import com.publiccms.logic.dao.home.HomeGroupApplyDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeGroupApplyService extends BaseService<HomeGroupApply> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean disabled, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(disabled, 
                pageIndex, pageSize);
    }
    
    @Autowired
    private HomeGroupApplyDao dao;
}