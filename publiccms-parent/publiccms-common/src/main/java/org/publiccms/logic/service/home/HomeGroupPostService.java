package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeGroupPost;
import org.publiccms.logic.dao.home.HomeGroupPostDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeGroupPostService
 * 
 */
@Service
@Transactional
public class HomeGroupPostService extends BaseService<HomeGroupPost> {

    /**
     * @param siteId
     * @param groupId
     * @param userId
     * @param disabled
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long groupId, Long userId, Boolean disabled, String orderField, String orderType,
            Integer pageIndex, Integer pageSize) {
        return dao.getPage(siteId, groupId, userId, disabled, orderField, orderType, pageIndex, pageSize);
    }

    @Autowired
    private HomeGroupPostDao dao;

}