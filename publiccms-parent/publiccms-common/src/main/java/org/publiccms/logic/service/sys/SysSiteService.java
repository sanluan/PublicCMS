package org.publiccms.logic.service.sys;

import org.publiccms.entities.sys.SysSite;
import org.publiccms.logic.dao.sys.SysSiteDao;

// Generated 2015-7-3 16:18:22 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * SysSiteService
 * 
 */
@Service
@Transactional
public class SysSiteService extends BaseService<SysSite> {

    /**
     * @param disabled
     * @param name
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Boolean disabled, String name, Integer pageIndex, Integer pageSize) {
        return dao.getPage(disabled, name, pageIndex, pageSize);
    }

    /**
     * @param id
     * @return
     */
    public SysSite delete(Integer id) {
        SysSite entity = getEntity(id);
        if (null != entity) {
            entity.setDisabled(true);
        }
        return entity;
    }
    
    @Autowired
    private SysSiteDao dao;
    
}