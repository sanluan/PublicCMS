package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.entities.sys.SysUser;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class SysUserDao extends BaseDao<SysUser> {
    public PageHandler getPage(Integer siteId, Integer deptId, Date startRegisteredDate, Date endRegisteredDate,
            Date startLastLoginDate, Date endLastLoginDate, Boolean superuserAccess, Boolean emailChecked, Boolean disabled,
            String name, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        if (notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (notEmpty(deptId)) {
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (notEmpty(startRegisteredDate)) {
            queryHandler.condition("bean.registeredDate > :startRegisteredDate").setParameter("startRegisteredDate",
                    startRegisteredDate);
        }
        if (notEmpty(endRegisteredDate)) {
            queryHandler.condition("bean.registeredDate <= :endRegisteredDate").setParameter("endRegisteredDate",
                    tomorrow(endRegisteredDate));
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
        if (notEmpty(disabled)) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.nickName like :name or bean.email like :name)").setParameter(
                    "name", like(name));
        }
        if ("asc".equalsIgnoreCase(orderType)) {
            orderType = "asc";
        } else {
            orderType = "desc";
        }
        if (null == orderField) {
            orderField = BLANK;
        }
        switch (orderField) {
        case "lastLoginDate":
            queryHandler.order("bean.lastLoginDate " + orderType);
            break;
        case "loginCount":
            queryHandler.order("bean.loginCount " + orderType);
            break;
        case "registeredDate":
            queryHandler.order("bean.registeredDate " + orderType);
            break;
        default:
            queryHandler.order("bean.id " + orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    public SysUser findByName(int siteId, String name) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.name = :name").setParameter("name", name);
        return getEntity(queryHandler);
    }

    public SysUser findByNickName(int siteId, String nickname) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.nickName = :nickname").setParameter("nickname", nickname);
        return getEntity(queryHandler);
    }

    public SysUser findByEmail(int siteId, String email) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.email = :email and bean.emailChecked = :emailChecked").setParameter("email", email)
                .setParameter("emailChecked", true);
        return getEntity(queryHandler);
    }

    @Override
    protected SysUser init(SysUser entity) {
        if (empty(entity.getRegisteredDate())) {
            entity.setRegisteredDate(getDate());
        }
        return entity;
    }
}
