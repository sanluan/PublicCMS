package org.publiccms.logic.service.cms;

import org.publiccms.entities.cms.CmsCategoryType;
import org.publiccms.logic.dao.cms.CmsCategoryTypeDao;

// Generated 2016-2-26 15:57:04 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsCategoryTypeService
 * 
 */
@Service
@Transactional
public class CmsCategoryTypeService extends BaseService<CmsCategoryType> {

    /**
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, pageIndex, pageSize);
    }

    /**
     * @param id
     * @param extendId
     * @return
     */
    public CmsCategoryType updateExtendId(Integer id, Integer extendId) {
        CmsCategoryType entity = getEntity(id);
        if (null != entity) {
            entity.setExtendId(extendId);
        }
        return entity;
    }

    @Autowired
    private CmsCategoryTypeDao dao;
    
}