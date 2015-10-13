package com.publiccms.logic.service.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemRoleUser;
import com.publiccms.logic.dao.system.SystemRoleUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemRoleUserService extends BaseService<SystemRoleUser> {

    @Autowired
    private SystemRoleUserDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, Integer userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, userId, pageIndex, pageSize);
    }

    public void dealRoleUsers(Integer userId, Integer[] roleIds) {
        @SuppressWarnings("unchecked")
        List<SystemRoleUser> list = (List<SystemRoleUser>) getPage(null, userId, null, null).getList();
        if (null != roleIds) {
            for (SystemRoleUser roleUser : list) {
                if (!contains(roleIds, roleUser.getRoleId())) {
                    delete(roleUser.getId());
                    roleIds = removeElement(roleIds, roleUser.getRoleId());
                }
            }
            for (Integer roleId : roleIds) {
                save(new SystemRoleUser(roleId, userId));
            }
        } else {
            deleteByUserId(userId);
        }
    }

    public int deleteByUserId(Integer userId) {
        return dao.deleteByUserId(userId);
    }

    public int deleteByRole(Integer roleId) {
        return dao.deleteByRole(roleId);
    }
}