package com.publiccms.logic.dao.system;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.system.SystemUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SystemUserDao extends BaseDao<SystemUser> {
    public PageHandler getPage(Date startDateRegistered, Date endDateRegistered, Date startLastLoginDate, Date endLastLoginDate,
            Boolean superuserAccess, Boolean emailChecked, Integer deptId, String name, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SystemUser bean");
        if (notEmpty(startDateRegistered)) {
            queryHandler.condition("bean.dateRegistered > :startDateRegistered").setParameter("startDateRegistered",
                    startDateRegistered);
        }
        if (notEmpty(endDateRegistered)) {
            queryHandler.condition("bean.dateRegistered <= :endDateRegistered").setParameter("endDateRegistered",
                    tomorrow(endDateRegistered));
        }
        if (notEmpty(startLastLoginDate)) {
            queryHandler.condition("bean.lastLoginDate > :startLastLoginDate").setParameter("startLastLoginDate",
                    startLastLoginDate);
        }
        if (notEmpty(endLastLoginDate)) {
            queryHandler.condition("bean.lastLoginDate <= :endLastLoginDate").setParameter("endLastLoginDate",
                    tomorrow(endLastLoginDate));
        }
        if (notEmpty(superuserAccess)) {
            queryHandler.condition("bean.superuserAccess = :superuserAccess").setParameter("superuserAccess", superuserAccess);
        }
        if (notEmpty(emailChecked)) {
            queryHandler.condition("bean.emailChecked = :emailChecked").setParameter("emailChecked", emailChecked);
        }
        if (notEmpty(deptId)) {
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.nickName like :name or bean.email like :name)").setParameter(
                    "name", like(name));
        }
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
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
        case "lastLoginDate":
            queryHandler.append("order by bean.lastLoginDate " + orderType);
            break;
        case "loginCount":
            queryHandler.append("order by bean.loginCount " + orderType);
            break;
        case "dateRegistered":
            queryHandler.append("order by bean.dateRegistered " + orderType);
            break;
        default:
            queryHandler.append("order by bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SystemUser findByEmail(String email) {
        QueryHandler queryHandler = getQueryHandler(
                "from SystemUser bean where bean.email = :email and bean.emailChecked = :emailChecked ").setParameter("email",
                email).setParameter("emailChecked", true);
        return getEntity(queryHandler);
    }

    @Override
    protected Class<SystemUser> getEntityClass() {
        return SystemUser.class;
    }

    @Override
    protected SystemUser init(SystemUser entity) {
        if (!notEmpty(entity.getDateRegistered())) {
            entity.setDateRegistered(getDate());
        }
        return entity;
    }
}
