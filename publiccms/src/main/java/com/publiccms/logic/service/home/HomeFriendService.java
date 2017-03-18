package com.publiccms.logic.service.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.home.HomeFriend;
import com.publiccms.logic.dao.home.HomeFriendDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class HomeFriendService extends BaseService<HomeFriend> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long friendId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, friendId, pageIndex, pageSize);
    }

    @Autowired
    private HomeFriendDao dao;
}