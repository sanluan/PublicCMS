package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysUserDao extends BaseDao<SysUser> {
    public PageHandler getPage(Date startDateRegistered, Date endDateRegistered, Date startLastLoginDate, Date endLastLoginDate,
            Boolean superuserAccess, Boolean emailChecked, Integer deptId, String name, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
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
            queryHandler.order("bean.lastLoginDate " + orderType);
            break;
        case "loginCount":
            queryHandler.order("bean.loginCount " + orderType);
            break;
        case "dateRegistered":
            queryHandler.order("bean.dateRegistered " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SysUser findByEmail(String email) {
        QueryHandler queryHandler = getQueryHandler(
                "from SysUser bean where bean.email = :email and bean.emailChecked = :emailChecked ").setParameter("email",
                email).setParameter("emailChecked", true);
        return getEntity(queryHandler);
    }

    @Override
    protected Class<SysUser> getEntityClass() {
        return SysUser.class;
    }

    @Override
    protected SysUser init(SysUser entity) {
        if (!notEmpty(entity.getDateRegistered())) {
            entity.setDateRegistered(getDate());
        }
        return entity;
    }
}
