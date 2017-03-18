package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeGroupActive;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeGroupActiveDao extends BaseDao<HomeGroupActive> {
    public PageHandler getPage(Long groupId, String itemType, 
                Long userId, 
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeGroupActive bean");
        if (notEmpty(groupId)) {
            queryHandler.condition("bean.groupId = :groupId").setParameter("groupId", groupId);
        }
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeGroupActive init(HomeGroupActive entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}