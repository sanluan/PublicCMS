package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogLogin;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogLoginDao extends BaseDao<LogLogin> {
    public PageHandler getPage(Integer siteId, Long userId, Date startCreateDate, Date endCreateDate, String channel,
            Boolean result, String name, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogLogin bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", tomorrow(endCreateDate));
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (notEmpty(result)) {
            queryHandler.condition("bean.result = :result").setParameter("result", result);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip like :ip").setParameter("ip", like(ip));
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        queryHandler.order("bean.createDate " + orderType + ",bean.id " + orderType);
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public int delete(Integer siteId, Date createDate) {
        if (notEmpty(siteId) || notEmpty(createDate)) {
            QueryHandler queryHandler = getDeleteQueryHandler("from LogLogin bean");
            if (notEmpty(siteId)) {
                queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
            }
            if (notEmpty(createDate)) {
                queryHandler.condition("bean.createDate <= :createDate").setParameter("createDate", createDate);
            }
            return delete(queryHandler);
        }
        return 0;
    }

    @Override
    protected LogLogin init(LogLogin entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}