package com.publiccms.logic.service.system;

// Generated 2015-7-20 11:46:39 by SourceMaker

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.system.SystemDept;
import com.publiccms.logic.dao.system.SystemDeptDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SystemDeptService extends BaseService<SystemDept> {

    @Autowired
    private SystemDeptDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer parentId, Integer userId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, userId, name, pageIndex, pageSize);
    }

    @Override
    public SystemDept delete(Serializable id) {
        SystemDept entity = getEntity(id);
        if (notEmpty(entity)) {
            @SuppressWarnings("unchecked")
            List<SystemDept> list = (List<SystemDept>) getPage(entity.getId(), null, null, null, null).getList();
            for (SystemDept child : list) {
                delete(child.getId());
            }
            dao.delete(id);
        }
        return entity;
    }
}