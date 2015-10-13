package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogOperate;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogOperateDao extends BaseDao<LogOperate> {
    public PageHandler getPage(String content, String operate, String ip, Integer userId, Date startCreateDate,
            Date endCreateDate, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogOperate bean");
        if (notEmpty(content)) {
            queryHandler.condition("bean.content like :content").setParameter("content", like(content));
        }
        if (notEmpty(operate)) {
            queryHandler.condition("bean.operate = :operate").setParameter("operate", operate);
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip like :ip").setParameter("ip", like(ip));
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
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (!notEmpty(orderField)) {
            orderField = "";
        }
        switch (orderField) {
        case "createDate":
            queryHandler.append("order by bean.createDate " + orderType);
            break;
        default:
            queryHandler.append("order by bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }
    

    public int delete(Date createDate) {
        QueryHandler queryHandler = getDeleteQueryHandler("from LogOperate bean where bean.createDate <= :createDate");
        queryHandler.setParameter("createDate", createDate);
        return delete(queryHandler);
    }

    @Override
    protected LogOperate init(LogOperate entity) {
        return entity;
    }

    @Override
    protected Class<LogOperate> getEntityClass() {
        return LogOperate.class;
    }

}