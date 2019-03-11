package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.Collection;

// Generated 2016-3-22 11:21:35 by com.publiccms.common.source.SourceGenerator

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsWord;
import com.publiccms.logic.dao.cms.CmsWordDao;
import com.publiccms.views.pojo.entities.ClickStatistics;

/**
 *
 * CmsWordService
 * 
 */
@Service
@Transactional
public class CmsWordService extends BaseService<CmsWord> {

    /**
     * @param siteId
     * @param hidden
     * @param startCreateDate
     * @param endCreateDate
     * @param name
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Boolean hidden, Date startCreateDate, Date endCreateDate, String name,
            String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, hidden, startCreateDate, endCreateDate, name, orderField, orderType, pageIndex, pageSize);
    }

    /**
     * @param entitys
     */
    public void updateStatistics(Collection<ClickStatistics> entitys) {
        for (ClickStatistics entityStatistics : entitys) {
            CmsWord entity = getEntity(entityStatistics.getId());
            if (null != entity) {
                entity.setSearchCount(entity.getSearchCount() + entityStatistics.getClicks());
            }
        }
    }

    /**
     * @param siteId
     * @param name
     * @return
     */
    public CmsWord getEntity(short siteId, String name) {
        return dao.getEntity(siteId, name);
    }

    /**
     * @param id
     * @param status
     * @return
     */
    public CmsWord updateStatus(Serializable id, boolean status) {
        CmsWord entity = getEntity(id);
        if (null != entity) {
            entity.setHidden(status);
        }
        return entity;
    }

    @Autowired
    private CmsWordDao dao;

}