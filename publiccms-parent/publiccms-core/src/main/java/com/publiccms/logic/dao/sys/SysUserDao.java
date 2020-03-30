package com.publiccms.logic.dao.sys;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUser;

/**
 *
 * SysUserDao
 * 
 */
@Repository
public class SysUserDao extends BaseDao<SysUser> {

    /**
     * @param siteId
     * @param deptId
     * @param startRegisteredDate
     * @param endRegisteredDate
     * @param startLastLoginDate
     * @param endLastLoginDate
     * @param superuserAccess
     * @param emailChecked
     * @param disabled
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    public PageHandler getPage(Short siteId, Integer deptId, Date startRegisteredDate, Date endRegisteredDate,
            Date startLastLoginDate, Date endLastLoginDate, Boolean superuserAccess, Boolean emailChecked, Boolean disabled,
            String name, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        if (CommonUtils.notEmpty(siteId)) {
            queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        }
        if (CommonUtils.notEmpty(deptId)) {
            queryHandler.condition("bean.deptId = :deptId").setParameter("deptId", deptId);
        }
        if (null != startRegisteredDate) {
            queryHandler.condition("bean.registeredDate > :startRegisteredDate").setParameter("startRegisteredDate",
                    startRegisteredDate);
        }
        if (null != endRegisteredDate) {
            queryHandler.condition("bean.registeredDate <= :endRegisteredDate").setParameter("endRegisteredDate",
                    endRegisteredDate);
        }
        if (null != startLastLoginDate) {
            queryHandler.condition("bean.lastLoginDate > :startLastLoginDate").setParameter("startLastLoginDate",
                    startLastLoginDate);
        }
        if (null != endLastLoginDate) {
            queryHandler.condition("bean.lastLoginDate <= :endLastLoginDate").setParameter("endLastLoginDate", endLastLoginDate);
        }
        if (null != superuserAccess) {
            queryHandler.condition("bean.superuserAccess = :superuserAccess").setParameter("superuserAccess", superuserAccess);
        }
        if (null != emailChecked) {
            queryHandler.condition("bean.emailChecked = :emailChecked").setParameter("emailChecked", emailChecked);
        }
        if (null != disabled) {
            queryHandler.condition("bean.disabled = :disabled").setParameter("disabled", disabled);
        }
        if (CommonUtils.notEmpty(name)) {
            queryHandler.condition("(bean.name like :name or bean.nickName like :name or bean.email like :name)")
                    .setParameter("name", like(name));
        }
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
        }
        if (null == orderField) {
            orderField = CommonConstants.BLANK;
        }
        switch (orderField) {
        case "lastLoginDate":
            queryHandler.order("bean.lastLoginDate ").append(orderType);
            break;
        case "loginCount":
            queryHandler.order("bean.loginCount ").append(orderType);
            break;
        case "registeredDate":
            queryHandler.order("bean.registeredDate ").append(orderType);
            break;
        default:
            queryHandler.order("bean.id ").append(orderType);
        }
        return getPage(queryHandler, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param name
     * @return entity
     */
    public SysUser findByName(short siteId, String name) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.name = :name").setParameter("name", name);
        return getEntity(queryHandler);
    }

    /**
     * @param siteId
     * @param nickname
     * @return entity
     */
    public SysUser findByNickName(short siteId, String nickname) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.nickName = :nickname").setParameter("nickname", nickname);
        return getEntity(queryHandler);
    }

    /**
     * @param siteId
     * @param email
     * @return entity
     */
    public SysUser findByEmail(short siteId, String email) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.email = :email and bean.emailChecked = :emailChecked").setParameter("email", email)
                .setParameter("emailChecked", true);
        return getEntity(queryHandler);
    }

    @Override
    protected SysUser init(SysUser entity) {
        if (null == entity.getRegisteredDate()) {
            entity.setRegisteredDate(CommonUtils.getDate());
        }
        return entity;
    }
}
