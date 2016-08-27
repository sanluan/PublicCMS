package com.publiccms.logic.service.sys;

import static org.apache.commons.lang3.ArrayUtils.removeElement;
import static org.apache.commons.lang3.StringUtils.split;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysUser;
import com.publiccms.logic.dao.sys.SysUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysUserService extends BaseService<SysUser> {
    
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer deptId, Date startRegisteredDate, Date endRegisteredDate,
            Date startLastLoginDate, Date endLastLoginDate, Boolean superuserAccess, Boolean emailChecked, Boolean disabled,
            String name, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, deptId, startRegisteredDate, endRegisteredDate, startLastLoginDate, endLastLoginDate,
                superuserAccess, emailChecked, disabled, name, orderField, orderType, pageIndex, pageSize);
    }

    public SysUser findByName(int siteId, String name) {
        return dao.findByName(siteId, name);
    }

    public SysUser findByNickName(int siteId, String nickname) {
        return dao.findByNickName(siteId, nickname);
    }

    public SysUser findByEmail(int siteId, String email) {
        return dao.findByEmail(siteId, email);
    }

    public void updatePassword(Serializable id, String password) {
        SysUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setPassword(password);
        }
    }

    public SysUser updateLoginStatus(Serializable id, String ip) {
        SysUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setLastLoginDate(getDate());
            entity.setLastLoginIp(ip);
            entity.setLoginCount(entity.getLoginCount() + 1);
        }
        return entity;
    }

    public void deleteRoleIds(Serializable userId, Integer roleId) {
        SysUser entity = dao.getEntity(userId);
        if (notEmpty(entity)) {
            String roles = entity.getRoles();
            String[] roleArray = split(roles, ',');
            removeElement(roleArray, roleId.toString());
            entity.setRoles(arrayToCommaDelimitedString(roleArray));
        }
    }

    public void checked(Serializable id, String email) {
        SysUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setEmail(email);
            entity.setEmailChecked(true);
        }
    }

    public SysUser updateStatus(Serializable id, boolean status) {
        SysUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(status);
        }
        return entity;
    }

    @Autowired
    private SysUserDao dao;
}
