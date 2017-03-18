package com.publiccms.logic.dao.home;

// Generated 2016-11-13 11:38:14 by com.sanluan.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeAttention;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeAttentionDao extends BaseDao<HomeAttention> {
    public PageHandler getPage(Long userId, Long attentionId, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeAttention bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.id.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(attentionId)) {
            queryHandler.condition("bean.id.attentionId = :attentionId").setParameter("attentionId", attentionId);
        }
        queryHandler.order("bean.createDate desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeAttention init(HomeAttention entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}