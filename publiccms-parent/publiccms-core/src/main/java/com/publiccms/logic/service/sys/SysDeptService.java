package com.publiccms.logic.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.sys.SysDept;
import com.publiccms.logic.dao.sys.SysDeptDao;

/**
 *
 * SysDeptService
 * 
 */
@Service
@Transactional
public class SysDeptService extends BaseService<SysDept> {

    /**
     * @param siteId
     * @param parentId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Integer parentId, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, parentId, userId, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param id
     * @return
     */
    public SysDept delete(short siteId, Integer id) {
        SysDept entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            @SuppressWarnings("unchecked")
            List<SysDept> list = (List<SysDept>) getPage(entity.getSiteId(), entity.getId(), null, null, null).getList();
            for (SysDept child : list) {
                child.setParentId(entity.getParentId());
            }
            dao.delete(id);
        }
        return entity;
    }

    @Autowired
    private SysDeptDao dao;

}