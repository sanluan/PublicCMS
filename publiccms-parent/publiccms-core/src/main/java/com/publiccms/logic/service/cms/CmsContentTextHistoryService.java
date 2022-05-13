package com.publiccms.logic.service.cms;

// Generated 2022-5-10 by com.publiccms.common.generator.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.entities.cms.CmsContentTextHistory;
import com.publiccms.logic.dao.cms.CmsContentTextHistoryDao;
import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * CmsContentTextHistoryService
 * 
 */
@Service
@Transactional
public class CmsContentTextHistoryService extends BaseService<CmsContentTextHistory> {

    /**
     * @param contentId
     * @param fieldName
     * @param userId
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long contentId, String fieldName, Long userId, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(contentId, fieldName, userId, orderType, pageIndex, pageSize);
    }

    @Autowired
    private CmsContentTextHistoryDao dao;

}