package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeComment;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeCommentDao extends BaseDao<HomeComment> {
    public PageHandler getPage(Integer siteId, Long userId, 
                String itemType, Long itemId, Boolean disabled, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeComment bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(itemType)) {
            queryHandler.condition("bean.itemType = :itemType").setParameter("itemType", itemType);
        }
        if (notEmpty(itemId)) {
            queryHandler.condition("bean.itemId = :itemId").setParameter("itemId", itemId);
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if("asc".equalsIgnoreCase(orderType)){
            orderType = "asc";
        }else{
            orderType = "desc";
        }
        queryHandler.order("bean.createDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeComment init(HomeComment entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}