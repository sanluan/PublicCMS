package com.publiccms.logic.dao.plugin;

// Generated 2016-3-1 17:24:23 by com.sanluan.common.source.SourceMaker

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.plugin.PluginLottery;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class PluginLotteryDao extends BaseDao<PluginLottery> {
    public PageHandler getPage(Integer siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from PluginLottery bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(startStartDate)) {
            queryHandler.condition("bean.startDate > :startStartDate").setParameter("startStartDate", startStartDate);
        }
        if (notEmpty(endStartDate)) {
            queryHandler.condition("bean.startDate <= :endStartDate").setParameter("endStartDate", endStartDate);
        }
        if (notEmpty(startEndDate)) {
            queryHandler.condition("bean.endDate > :startEndDate").setParameter("startEndDate", startEndDate);
        }
        if (notEmpty(endEndDate)) {
            queryHandler.condition("bean.endDate <= :endEndDate").setParameter("endEndDate", endEndDate);
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
        case "startDate":
            queryHandler.order("bean.startDate " + orderType);
            break;
        case "endDate":
            queryHandler.order("bean.endDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected PluginLottery init(PluginLottery entity) {
        return entity;
    }

}