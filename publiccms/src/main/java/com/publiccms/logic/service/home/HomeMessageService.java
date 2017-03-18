package com.publiccms.logic.service.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeMessage;
import com.publiccms.logic.dao.home.HomeMessageDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeMessageService extends BaseService<HomeMessage> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, String itemType, 
                Long itemId, 
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, itemType, 
                itemId, 
                pageIndex, pageSize);
    }
    
    @Autowired
    private HomeMessageDao dao;
}