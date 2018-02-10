package com.publiccms.logic.service.cms;

import java.io.Serializable;
import java.util.List;

// Generated 2016-3-3 17:43:34 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsVoteItem;
import com.publiccms.logic.dao.cms.CmsVoteItemDao;

/**
 *
 * CmsVoteItemService
 * 
 */
@Service
@Transactional
public class CmsVoteItemService extends BaseService<CmsVoteItem> {

    /**
     * @param voteId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer voteId, String orderField, String orderType, Integer pageIndex, Integer pageSize) {
        return dao.getPage(voteId, orderField, orderType, pageIndex, pageSize);
    }

    public void updateScores(Serializable[] id) {
        List<CmsVoteItem> entitys = getEntitys(id);
        for (CmsVoteItem entity : entitys) {
            entity.setScores(entity.getScores() + 1);
        }
    }

    @Autowired
    private CmsVoteItemDao dao;

}