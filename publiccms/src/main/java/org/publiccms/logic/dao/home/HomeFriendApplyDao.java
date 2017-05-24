package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeFriendApply;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeFriendApplyDao
 * 
 */
@Repository
public class HomeFriendApplyDao extends BaseDao<HomeFriendApply> {

    /**
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeFriendApply bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeFriendApply init(HomeFriendApply entity) {
        if (empty(entity.getCreateDate())) {
            entity.setCreateDate(getDate());
        }
        return entity;
    }

}