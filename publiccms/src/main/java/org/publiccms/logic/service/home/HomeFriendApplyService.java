package org.publiccms.logic.service.home;

import org.publiccms.entities.home.HomeFriendApply;
import org.publiccms.logic.dao.home.HomeFriendApplyDao;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * HomeFriendApplyService
 * 
 */
@Service
@Transactional
public class HomeFriendApplyService extends BaseService<HomeFriendApply> {

    /**
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        return dao.getPage(pageIndex, pageSize);
    }

    @Autowired
    private HomeFriendApplyDao dao;

}