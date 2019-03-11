package com.publiccms.logic.service.sys;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.CommonConstants;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.dao.sys.SysUserDao;

/**
 *
 * SysUserService
 * 
 */
@Service
@Transactional
public class SysUserService extends BaseService<SysUser> {

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
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer deptId, Date startRegisteredDate, Date endRegisteredDate,
            Date startLastLoginDate, Date endLastLoginDate, Boolean superuserAccess, Boolean emailChecked, Boolean disabled,
            String name, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, deptId, startRegisteredDate, endRegisteredDate, startLastLoginDate, endLastLoginDate,
                superuserAccess, emailChecked, disabled, name, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param name
     * @return
     */
    public SysUser findByName(short siteId, String name) {
        return dao.findByName(siteId, name);
    }

    /**
     * @param siteId
     * @param nickname
     * @return
     */
    public SysUser findByNickName(short siteId, String nickname) {
        return dao.findByNickName(siteId, nickname);
    }

    /**
     * @param siteId
     * @param email
     * @return
     */
    public SysUser findByEmail(short siteId, String email) {
        return dao.findByEmail(siteId, email);
    }

    /**
     * @param id
     * @param password
     * @param salt
     */
    public void updatePassword(Serializable id, String password, String salt) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setPassword(password);
            entity.setSalt(salt);
        }
    }

    /**
     * @param id
     * @param weak
     */
    public void updateWeekPassword(Serializable id, boolean weak) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setWeakPassword(weak);
        }
    }

    /**
     * @param id
     * @param ip
     * @return
     */
    public SysUser updateLoginStatus(Serializable id, String ip) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setLastLoginDate(CommonUtils.getDate());
            entity.setLastLoginIp(ip);
            entity.setLoginCount(entity.getLoginCount() + 1);
        }
        return entity;
    }

    /**
     * @param userId
     * @param roleId
     */
    public void deleteRoleIds(Serializable userId, Integer roleId) {
        SysUser entity = getEntity(userId);
        if (null != entity) {
            String roles = entity.getRoles();
            String[] roleArray = StringUtils.split(roles, CommonConstants.COMMA);
            ArrayUtils.removeElement(roleArray, roleId.toString());
            entity.setRoles(arrayToCommaDelimitedString(roleArray));
        }
    }

    /**
     * @param id
     * @param email
     */
    public void checked(Serializable id, String email) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setEmail(email);
            entity.setEmailChecked(true);
        }
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public SysUser updateStatus(Serializable id, boolean status) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private SysUserDao dao;

}
