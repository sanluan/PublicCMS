package com.publiccms.logic.service.sys;

import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.constants.Constants;
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
    public static final int CONTENT_PERMISSIONS_SELF = 0;
    public static final int CONTENT_PERMISSIONS_ALL = 1;
    public static final int CONTENT_PERMISSIONS_DEPT = 2;

    /**
     * @param siteId
     * @param deptId
     * @param startRegisteredDate
     * @param endRegisteredDate
     * @param startLastLoginDate
     * @param endLastLoginDate
     * @param superuser
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
            Date startLastLoginDate, Date endLastLoginDate, Boolean superuser, Boolean emailChecked, Boolean disabled,
            String name, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, deptId, startRegisteredDate, endRegisteredDate, startLastLoginDate, endLastLoginDate,
                superuser, emailChecked, disabled, name, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param name
     * @return entity
     */
    public SysUser findByName(short siteId, String name) {
        return dao.findByName(siteId, name);
    }

    /**
     * @param siteId
     * @param email
     * @return entity
     */
    public SysUser findByEmail(short siteId, String email) {
        return dao.findByEmail(siteId, email);
    }

    /**
     * @param id
     * @param nickname
     * @param cover
     * @param email
     * @return entity
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SysUser updateProfile(Serializable id, String nickname, String cover, String email) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setNickname(nickname);
            entity.setCover(cover);
            if (null != email) {
                entity.setEmail(email);
            }
        }
        return entity;
    }

    /**
     * @param id
     * @param password
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updatePassword(Serializable id, String password) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setPassword(password);
        }
    }

    /**
     * @param id
     * @param weak
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
    @Transactional(isolation = Isolation.READ_COMMITTED)
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
            String[] roleArray = StringUtils.split(roles, Constants.COMMA);
            ArrayUtils.removeElement(roleArray, roleId.toString());
            entity.setRoles(arrayToCommaDelimitedString(roleArray));
        }
    }

    /**
     * @param id
     * @param email
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void checked(Serializable id, String email) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setEmail(email);
            entity.setEmailChecked(true);
        }
    }

    /**
     * @param id
     * @param nickname
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SysUser updateNickname(Serializable id, String nickname) {
        SysUser entity = getEntity(id);
        if (null != entity) {
            entity.setNickname(nickname);
        }
        return entity;
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

    /**
     * @param siteId
     * @param ids
     * @param operateId
     * @param status
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<SysUser> updateStatus(short siteId, Serializable[] ids, Serializable operateId, boolean status) {
        List<SysUser> entityList = getEntitys(ids);
        if (null != entityList) {
            for (SysUser entity : entityList) {
                if (siteId == entity.getSiteId() && null != operateId && !operateId.equals(entity.getId())) {
                    entity.setDisabled(status);
                }
            }
        }
        return entityList;
    }

    @Resource
    private SysUserDao dao;

}
