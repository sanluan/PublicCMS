package com.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeActive;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeActiveDao extends BaseDao<HomeActive> {
    public PageHandler getPage(String itemType, Long userId, Long[] userIds, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeActive bean");
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(userIds)) {
            queryHandler.condition("bean.userIds = :userIds").setParameter("userIds", userIds);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeActive init(HomeActive entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}