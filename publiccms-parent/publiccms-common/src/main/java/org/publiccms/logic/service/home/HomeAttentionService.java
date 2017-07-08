package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeAttention;
import org.publiccms.logic.dao.home.HomeAttentionDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeAttentionService
 * 
 */
@Service
@Transactional
public class HomeAttentionService extends BaseService<HomeAttention> {

    /**
     * @param userId
     * @param attentionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long attentionId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, attentionId, pageIndex, pageSize);
    }

    @Autowired
    private HomeAttentionDao dao;

}