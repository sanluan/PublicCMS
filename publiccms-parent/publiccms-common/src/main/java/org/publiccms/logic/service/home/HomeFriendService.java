package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeFriend;
import org.publiccms.logic.dao.home.HomeFriendDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeFriendService
 * 
 */
@Service
@Transactional
public class HomeFriendService extends BaseService<HomeFriend> {

    /**
     * @param userId
     * @param friendId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Long userId, Long friendId, Integer pageIndex, Integer pageSize) {
        return dao.getPage(userId, friendId, pageIndex, pageSize);
    }

    @Autowired
    private HomeFriendDao dao;
    
}