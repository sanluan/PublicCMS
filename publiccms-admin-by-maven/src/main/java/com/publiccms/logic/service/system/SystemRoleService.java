package com.publiccms.logic.service.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemRole;
import com.publiccms.logic.dao.system.SystemRoleDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemRoleService extends BaseService<SystemRole> {

    @Autowired
    private SystemRoleDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer deptId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(deptId, pageIndex, pageSize);
    }

    @Transactional(readOnly = true)
    public boolean ownsAllRight(Integer[] roleIds) {
        List<SystemRole> list = getEntitys(roleIds);
        for (SystemRole role : list) {
            if (role.isOwnsAllRight()) {
                return true;
            }
        }
        return false;
    }
}