package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeFriend;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeFriendDao extends BaseDao<HomeFriend> {
    public PageHandler getPage(Long userId, 
                Long friendId, 
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeFriend bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(friendId)) {
            queryHandler.condition("bean.friendId = :friendId").setParameter("friendId", friendId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeFriend init(HomeFriend entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}