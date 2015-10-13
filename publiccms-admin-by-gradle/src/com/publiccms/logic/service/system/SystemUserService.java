package com.publiccms.logic.service.system;

import static org.apache.commons.lang3.ArrayUtils.removeElement;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;
import static org.apache.commons.lang3.StringUtils.split;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.log.LogLogin;
import com.publiccms.entities.system.SystemUser;
import com.publiccms.logic.dao.log.LogLoginDao;
import com.publiccms.logic.dao.system.SystemUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemUserService extends BaseService<SystemUser> {
    @Autowired
    private SystemUserDao dao;
    @Autowired
    private LogLoginDao logLoginDao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Date startDateRegistered, Date endDateRegistered, Date startLastLoginDate, Date endLastLoginDate,
            Boolean superuserAccess, Boolean emailChecked, Integer deptId, String name, Boolean disabled, String orderField,
            String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(startDateRegistered, endDateRegistered, startLastLoginDate, endLastLoginDate, superuserAccess,
                emailChecked, deptId, name, disabled, orderField, orderType, pageIndex, pageSize);
    }

    public SystemUser findByName(String name) {
        return dao.getEntity(name, "name");
    }

    public SystemUser findByNickName(String nickName) {
        return dao.getEntity(nickName, "nickName");
    }

    public SystemUser findByEmail(String email) {
        return dao.findByEmail(email);
    }

    public SystemUser findByAuthToken(String authToken) {
        return dao.getEntity(authToken, "authToken");
    }

    public void updatePassword(Integer id, String password) {
        SystemUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setPassword(password);
        }
    }

    public SystemUser updateLoginStatus(Integer id, String username, String authToken, String ip) {
        SystemUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            LogLogin log = new LogLogin();
            log.setName(username);
            log.setUserId(id);
            log.setResult(true);
            log.setIp(ip);
            log.setErrorPassword(null);
            logLoginDao.save(log);
            entity.setAuthToken(authToken);
            entity.setLastLoginDate(getDate());
            entity.setLastLoginIp(ip);
            entity.setLoginCount(entity.getLoginCount() + 1);
        }
        return entity;
    }

    public void deleteRoleIds(Integer userId, Integer roleId) {
        SystemUser entity = dao.getEntity(userId);
        if (notEmpty(entity)) {
            String roles = entity.getRoles();
            String[] roleArray = split(roles, ',');
            removeElement(roleArray, roleId.toString());
            roles = arrayToCommaDelimitedString(roleArray);
            entity.setRoles(roles);
        }
    }

    public void checked(Integer userId, String email) {
        SystemUser entity = dao.getEntity(userId);
        if (notEmpty(entity)) {
            entity.setEmail(email);
            entity.setEmailChecked(true);
        }
    }

    public SystemUser updateStatus(Integer id, boolean status) {
        SystemUser entity = dao.getEntity(id);
        if (notEmpty(entity)) {
            entity.setDisabled(status);
            entity.setAuthToken(null);
        }
        return entity;
    }
}
