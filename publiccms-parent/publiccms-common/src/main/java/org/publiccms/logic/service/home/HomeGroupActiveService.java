package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeGroupActive;
import org.publiccms.logic.dao.home.HomeGroupActiveDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeGroupActiveService
 * 
 */
@Service
@Transactional
public class HomeGroupActiveService extends BaseService<HomeGroupActive> {

    /**
     * @param groupId
     * @param itemType
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long groupId, String itemType, Long userId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(groupId, itemType, userId, pageIndex, pageSize);
    }

    @Autowired
    private HomeGroupActiveDao dao;

}