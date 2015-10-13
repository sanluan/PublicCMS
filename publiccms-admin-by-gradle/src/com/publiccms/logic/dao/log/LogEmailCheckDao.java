package com.publiccms.logic.dao.log;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.log.LogEmailCheck;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class LogEmailCheckDao extends BaseDao<LogEmailCheck> {
    public PageHandler getPage(Integer userId, Date startCreateDate, Date endCreateDate, Boolean checked, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from LogEmailCheck bean");
        if (notEmpty(userId)) {
            queryHandler.condition("bean.userId = :userId").setParameter("userId", userId);
        }
        if (notEmpty(startCreateDate)) {
            queryHandler.condition("bean.createDate > :startCreateDate").setParameter("startCreateDate", startCreateDate);
        }
        if (notEmpty(endCreateDate)) {
            queryHandler.condition("bean.createDate <= :endCreateDate").setParameter("endCreateDate", tomorrow(endCreateDate));
        }
        if (notEmpty(checked)) {
            queryHandler.condition("bean.checked = :checked").setParameter("checked", checked);
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

    public LogEmailCheck findByCode(String code) {
        QueryHandler queryHandler = getQueryHandler("from LogEmailCheck bean where bean.code = :code and bean.checked = :checked")
                .setParameter("code", code).setParameter("checked", false);
        return getEntity(queryHandler);
    }

    public int delete(Date createDate) {
        QueryHandler queryHandler = getDeleteQueryHandler("from LogEmailCheck bean where bean.createDate <= :createDate");
        queryHandler.setParameter("createDate", createDate);
        return delete(queryHandler);
    }

    @Override
    protected LogEmailCheck init(LogEmailCheck entity) {
        if (!notEmpty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

    @Override
    protected Class<LogEmailCheck> getEntityClass() {
        return LogEmailCheck.class;
    }

}