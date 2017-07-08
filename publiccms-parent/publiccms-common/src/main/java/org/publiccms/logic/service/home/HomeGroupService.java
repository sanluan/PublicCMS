package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeGroup;
import org.publiccms.logic.dao.home.HomeGroupDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeGroupService
 * 
 */
@Service
@Transactional
public class HomeGroupService extends BaseService<HomeGroup> {

    /**
     * @param siteId
     * @param userId
     * @param orderField
     * @param orderType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer siteId, Long userId, String orderField, String orderType, Integer pageIndex,
            Integer pageSize) {
        return dao.getPage(siteId, userId, orderField, orderType, pageIndex, pageSize);
    }

    @Autowired
    private HomeGroupDao dao;

}