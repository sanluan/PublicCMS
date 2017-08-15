package org.publiccms.logic.service.sys;

// Generated 2015-7-20 11:46:39 by com.publiccms.common.source.SourceGenerator

import java.util.ArrayList;
import java.util.List;

import org.publiccms.entities.sys.SysDept;
import org.publiccms.logic.dao.sys.SysDeptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

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
    public PageHandler getPage(Integer siteId, Integer parentId, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, parentId, userId, pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param id
     * @return
     */
    public List<Integer> delete(int siteId, Integer id) {
        SysDept entity = getEntity(id);
        List<Integer> idList = new ArrayList<>();
        if (null != entity && siteId == entity.getSiteId()) {
            @SuppressWarnings("unchecked")
            List<SysDept> list = (List<SysDept>) getPage(entity.getSiteId(), entity.getId(), null, null, null).getList();
            for (SysDept child : list) {
                delete(child.getId());
                idList.add(child.getId());
            }
            dao.delete(id);
            idList.add(id);
        }
        return idList;
    }

    @Autowired
    private SysDeptDao dao;
    
}