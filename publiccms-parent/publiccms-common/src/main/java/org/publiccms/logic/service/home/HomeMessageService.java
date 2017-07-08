package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeMessage;
import org.publiccms.logic.dao.home.HomeMessageDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeMessageService
 * 
 */
@Service
@Transactional
public class HomeMessageService extends BaseService<HomeMessage> {

    /**
     * @param userId
     * @param itemType
     * @param itemId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, String itemType, Long itemId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, itemType, itemId, pageIndex, pageSize);
    }

    @Autowired
    private HomeMessageDao dao;

}