package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogLogin;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogLoginDao extends BaseDao<LogLogin> {
    public PageHandler getPage(Boolean result, Integer userId, String name, Date startCreateDate, Date endCreateDate, String ip,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogLogin bean");
        if (notEmpty(result)) {
            queryHandler.condition("bean.result = :result").setParameter("result", result);
        }
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(name)) {
            queryHandler.condition("bean.name like :name").setParameter("name", like(name));
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", tomorrow(endCreateDate));
        }
        if (notEmpty(ip)) {
            queryHandler.condition("bean.ip like :ip").setParameter("ip", like(ip));
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
        QueryHandler queryHandler = getDeleteQueryHandler("from LogLogin bean where bean.createDate <= :createDate");
        queryHandler.setParameter("createDate", createDate);
        return delete(queryHandler);
    }

    @Override
    protected LogLogin init(LogLogin entity) {
        if (!notEmpty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

    @Override
    protected Class<LogLogin> getEntityClass() {
        return LogLogin.class;
    }

}