package com.publiccms.logic.service.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeFriendApply;
import com.publiccms.logic.dao.home.HomeFriendApplyDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeFriendApplyService extends BaseService<HomeFriendApply> {

    @Transactional(readOnly = true)
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        return dao.getPage(
                pageIndex, pageSize);
    }
    
    @Autowired
    private HomeFriendApplyDao dao;
}