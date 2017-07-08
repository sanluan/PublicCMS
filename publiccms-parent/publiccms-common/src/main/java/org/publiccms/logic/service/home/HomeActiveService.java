package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeActive;
import org.publiccms.logic.dao.home.HomeActiveDao;

// Generated 2016-11-13 11:38:14 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeActiveService
 * 
 */
@Service
@Transactional
public class HomeActiveService extends BaseService<HomeActive> {

    /**
     * @param itemType
     * @param userId
     * @param userIds
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(String itemType, Long userId, Long[] userIds, Integer pageIndex, Integer pageSize) {
        return dao.getPage(itemType, userId, userIds, pageIndex, pageSize);
    }

    @Autowired
    private HomeActiveDao dao;
    
}