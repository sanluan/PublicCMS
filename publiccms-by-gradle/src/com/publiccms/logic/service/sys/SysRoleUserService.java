package com.publiccms.logic.service.sys;

// Generated 2015-7-20 11:46:39 by com.sanluan.common.source.SourceMaker

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysRoleUser;
import com.publiccms.logic.dao.sys.SysRoleUserDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysRoleUserService extends BaseService<SysRoleUser> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, userId, pageIndex, pageSize);
    }

    public void dealRoleUsers(Long userId, Integer[] roleIds) {
        @SuppressWarnings("unchecked")
        List<SysRoleUser> list = (List<SysRoleUser>) getPage(null, userId, null, null).getList();
        if (notEmpty(roleIds)) {
            for (SysRoleUser roleUser : list) {
                if (!contains(roleIds, roleUser.getRoleId())) {
                    delete(roleUser.getId());
                }
                roleIds = removeElement(roleIds, roleUser.getRoleId());
            }
            for (Integer roleId : roleIds) {
                save(new SysRoleUser(roleId, userId));
            }
        } else {
            deleteByUserId(userId);
        }
    }

    public int deleteByUserId(Long userId) {
        return dao.deleteByUserId(userId);
    }

    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

    @Autowired
    private SysRoleUserDao dao;
}