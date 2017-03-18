package com.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeUserDao extends BaseDao<HomeUser> {
    public PageHandler getPage(Integer siteId, Boolean disabled, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeUser bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (null == orderField) {
            orderField = "";
        }
        switch (orderField) {
        case "lastLoginDate":
            queryHandler.order("bean.lastLoginDate " + orderType);
            break;
        case "createDate":
            queryHandler.order("bean.createDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeUser init(HomeUser entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}