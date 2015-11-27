package com.publiccms.logic.service.sys;

// Generated 2015-7-20 11:46:39 by SourceMaker

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.sys.SysDept;
import com.publiccms.logic.dao.sys.SysDeptDao;
import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class SysDeptService extends BaseService<SysDept> {

    @Autowired
    private SysDeptDao dao;

    @Transactional(readOnly = true)
    public PageHandler getPage(Integer parentId, Integer userId, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(parentId, userId, name, pageIndex, pageSize);
    }

    @Override
    public SysDept delete(Serializable id) {
        SysDept entity = getEntity(id);
        if (notEmpty(entity)) {
            @SuppressWarnings("unchecked")
            List<SysDept> list = (List<SysDept>) getPage(entity.getId(), null, null, null, null).getList();
            for (SysDept child : list) {
                delete(child.getId());
            }
            dao.delete(id);
        }
        return entity;
    }
}