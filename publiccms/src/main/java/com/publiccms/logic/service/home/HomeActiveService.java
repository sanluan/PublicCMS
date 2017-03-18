package com.publiccms.logic.service.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeActive;
import com.publiccms.logic.dao.home.HomeActiveDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeActiveService extends BaseService<HomeActive> {

    @Transactional(readOnly = true)
    public PageHandler getPage(String itemType, Long userId, Long[] userIds, Integer pageIndex, Integer pageSize) {
        return dao.getPage(itemType, userId, userIds, pageIndex, pageSize);
    }

    @Autowired
    private HomeActiveDao dao;
}