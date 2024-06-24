package com.publiccms.logic.service.cms;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsUserCollection;
import com.publiccms.logic.dao.cms.CmsUserCollectionDao;

import jakarta.annotation.Resource;

/**
 *
 * CmsUserCollectionService
 * 
 */
@Service
@Transactional
public class CmsUserCollectionService extends BaseService<CmsUserCollection> {

    /**
     * @param userId
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long contentId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, contentId, pageIndex, pageSize);
    }

    @Resource
    private CmsUserCollectionDao dao;

}