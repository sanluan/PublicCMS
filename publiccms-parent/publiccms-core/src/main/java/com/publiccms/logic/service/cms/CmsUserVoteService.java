package com.publiccms.logic.service.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.entities.cms.CmsUserVote;
import com.publiccms.logic.dao.cms.CmsUserVoteDao;

/**
 *
 * CmsUserVoteService
 * 
 */
@Service
@Transactional
public class CmsUserVoteService extends BaseService<CmsUserVote> {

    /**
     * @param userId
     * @param voteId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional
    public PageHandler getPage(Long userId, Long voteId, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(userId, voteId, orderField, orderType, pageIndex, pageSize);
    }

    @Resource
    private CmsUserVoteDao dao;

}