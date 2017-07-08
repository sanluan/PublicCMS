package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeComment;
import org.publiccms.logic.dao.home.HomeCommentDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeCommentService
 * 
 */
@Service
@Transactional
public class HomeCommentService extends BaseService<HomeComment> {

    /**
     * @param siteId
     * @param userId
     * @param itemType
     * @param itemId
     * @param disabled
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, String itemType, Long itemId, Boolean disabled, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, userId, itemType, itemId, disabled, orderType, pageIndex, pageSize);
    }

    @Autowired
    private HomeCommentDao dao;

}