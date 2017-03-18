package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeDialog;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeDialogDao extends BaseDao<HomeDialog> {
    public PageHandler getPage(Boolean disabled, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeDialog bean");
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if("asc".equalsIgnoreCase(orderType)){
            orderType = "asc";
        }else{
            orderType = "desc";
        }
        queryHandler.order("bean.lastMessageDate " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeDialog init(HomeDialog entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}