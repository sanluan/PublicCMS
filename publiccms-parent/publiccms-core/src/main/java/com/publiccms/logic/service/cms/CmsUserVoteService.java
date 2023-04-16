package com.publiccms.logic.service.cms;

// Generated 2020-3-26 11:46:48 by com.publiccms.common.generator.SourceGenerator

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param anonymous
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return results page
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long voteId, Boolean anonymous, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(userId, voteId, anonymous, orderType, pageIndex, pageSize);
    }

    @Resource
    private CmsUserVoteDao dao;

}