package org.publiccms.logic.dao.sys;

import static com.publiccms.common.tools.CommonUtils.empty;
import static com.publiccms.common.tools.CommonUtils.getDate;
import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.util.Date;

import org.publiccms.entities.sys.SysUser;
import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

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
     * @return
     */
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
        if (!ORDERTYPE_ASC.equalsIgnoreCase(orderType)) {
            orderType = ORDERTYPE_DESC;
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

    /**
     * @param siteId
     * @param name
     * @return
     */
    public SysUser findByName(int siteId, String name) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.name = :name").setParameter("name", name);
        return getEntity(queryHandler);
    }

    /**
     * @param siteId
     * @param nickname
     * @return
     */
    public SysUser findByNickName(int siteId, String nickname) {
        QueryHandler queryHandler = getQueryHandler("from SysUser bean");
        queryHandler.condition("bean.siteId = :siteId").setParameter("siteId", siteId);
        queryHandler.condition("bean.nickName = :nickname").setParameter("nickname", nickname);
        return getEntity(queryHandler);
    }

    /**
     * @param siteId
     * @param email
     * @return
     */
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
