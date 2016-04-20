package com.publiccms.logic.service.sys;

// Generated 2015-7-22 13:48:39 by com.sanluan.common.source.SourceMaker

import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysRoleMoudle;
import com.publiccms.logic.dao.sys.SysRoleMoudleDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysRoleMoudleService extends BaseService<SysRoleMoudle> {

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer roleId, Integer moudleId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(roleId, moudleId, pageIndex, pageSize);
    }

    public void updateRoleMoudles(Integer roleId, Integer[] moudleIds) {
        if (notEmpty(roleId)) {
            @SuppressWarnings("unchecked")
            List<SysRoleMoudle> list = (List<SysRoleMoudle>) getPage(roleId, null, null, null).getList();
            for (SysRoleMoudle roleMoudle : list) {
                if (contains(moudleIds, roleMoudle.getMoudleId())) {
                    moudleIds = removeElement(moudleIds, roleMoudle.getMoudleId());
                } else {
                    delete(roleMoudle.getId());
                }
            }
            saveRoleMoudles(roleId, moudleIds);
        }
    }

    public void saveRoleMoudles(Integer roleId, Integer[] moudleIds) {
        if (notEmpty(roleId) && notEmpty(moudleIds)) {
            for (int moudleId : moudleIds) {
                save(new SysRoleMoudle(roleId, moudleId));
            }
        }
    }

    @Transactional(readOnly = true)
    public List<SysRoleMoudle> getEntitys(Integer[] roleIds, Integer[] moudleIds) {
        return dao.getEntitys(roleIds, moudleIds);
    }

    @Transactional(readOnly = true)
    public SysRoleMoudle getEntity(Integer[] roleIds, Integer moudleId) {
        return dao.getEntity(roleIds, moudleId);
    }

    public int deleteByRoleId(Integer roleId) {
        return dao.deleteByRoleId(roleId);
    }

    public int deleteByMoudleId(Integer moudleId) {
        return dao.deleteByMoudleId(moudleId);
    }

    @Autowired
    private SysRoleMoudleDao dao;
}