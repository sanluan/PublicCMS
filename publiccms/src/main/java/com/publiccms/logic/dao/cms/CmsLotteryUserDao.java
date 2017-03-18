package com.publiccms.logic.dao.cms;

// Generated 2016-3-1 17:24:24 by com.sanluan.common.source.SourceGenerator

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.cms.CmsLotteryUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class CmsLotteryUserDao extends BaseDao<CmsLotteryUser> {
    public PageHandler getPage(Long lotteryId, Long userId, 
                Boolean winning, Date startCreateDate, Date endCreateDate, 
                String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from CmsLotteryUser bean");
        if (notEmpty(lotteryId)) {
            queryHandler.condition("bean.lotteryId = :lotteryId").setParameter("lotteryId", lotteryId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(winning)) {
            queryHandler.condition("bean.winning = :winning").setParameter("winning", winning);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", endCreateDate);
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
    protected CmsLotteryUser init(CmsLotteryUser entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}