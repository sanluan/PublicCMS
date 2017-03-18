package com.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:13 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeDirectory;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeDirectoryDao extends BaseDao<HomeDirectory> {
    public PageHandler getPage(Integer siteId, Long userId, Boolean disabled, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeDirectory bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeDirectory init(HomeDirectory entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}