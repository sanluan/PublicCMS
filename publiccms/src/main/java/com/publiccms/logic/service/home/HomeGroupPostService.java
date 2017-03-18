package com.publiccms.logic.service.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeGroupPost;
import com.publiccms.logic.dao.home.HomeGroupPostDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeGroupPostService extends BaseService<HomeGroupPost> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long groupId, 
                Long userId, Boolean disabled, 
                String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, groupId, 
                userId, disabled, 
                orderField, orderType, pageIndex, pageSize);
    }
    
    @Autowired
    private HomeGroupPostDao dao;
}