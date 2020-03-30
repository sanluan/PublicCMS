package com.publiccms.logic.service.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsUserScore;
import com.publiccms.logic.dao.cms.CmsUserScoreDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsUserScoreService
 * 
 */
@Service
@Transactional
public class CmsUserScoreService extends BaseService<CmsUserScore> {

    /**
     * @param userId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, String itemType, Long itemId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, itemType, itemId, pageIndex, pageSize);
    }

    @Autowired
    private CmsUserScoreDao dao;

}