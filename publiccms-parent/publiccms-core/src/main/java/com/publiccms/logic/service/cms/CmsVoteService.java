package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.ArrayList;
// Generated 2020-3-26 12:04:23 by com.publiccms.common.generator.SourceGenerator
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsVote;
import com.publiccms.logic.dao.cms.CmsVoteDao;

/**
 *
 * CmsVoteService
 * 
 */
@Service
@Transactional
public class CmsVoteService extends BaseService<CmsVote> {

    /**
     * @param siteId
     * @param startStartDate
     * @param endStartDate
     * @param startEndDate
     * @param endEndDate
     * @param title
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Short siteId, Date startStartDate, Date endStartDate, Date startEndDate, Date endEndDate,
            String title, Boolean disabled, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, startStartDate, endStartDate, startEndDate, endEndDate, title, disabled, orderField, orderType,
                pageIndex, pageSize);
    }

    /**
     * @param siteId
     * @param id
     * @param scores
     * @return entity
     */
    public CmsVote updateScores(short siteId, Serializable id, int scores) {
        CmsVote entity = getEntity(id);
        if (null != entity && siteId == entity.getSiteId()) {
            entity.setScores(entity.getScores() + scores);
        }
        return entity;
    }

    public List<CmsVote> delete(short siteId, Serializable[] ids) {
        List<CmsVote> entityList = new ArrayList<>();
        for (CmsVote entity : getEntitys(ids)) {
            if (!entity.isDisabled() && siteId == entity.getSiteId()) {
                entity.setDisabled(true);
                entityList.add(entity);
            }
        }
        return entityList;
    }

    @Autowired
    private CmsVoteDao dao;

}