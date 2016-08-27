package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogOperate;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogOperateDao extends BaseDao<LogOperate> {
    public PageHandler getPage(Integer siteId, String channel, String operate, Long userId, Date startCreateDate,
            Date endCreateDate, String content, String ip, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogOperate bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(channel)) {
            queryHandler.condition("bean.channel = :channel").setParameter("channel", channel);
        }
        if (notEmpty(operate)) {
            queryHandler.condition("bean.operate = :operate").setParameter("operate", operate);
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
        if (notEmpty(content)) {
            queryHandler.condition("bean.content like :content").setParameter("content", like(content));
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
            QueryHandler queryHandler = getDeleteQueryHandler("from LogOperate bean");
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
    protected LogOperate init(LogOperate entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}