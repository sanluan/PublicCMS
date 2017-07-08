package org.publiccms.logic.dao.home;

import org.publiccms.entities.home.HomeGroupPostContent;

// Generated 2016-11-19 9:58:46 by com.publiccms.common.source.SourceGenerator

import org.springframework.stereotype.Repository;

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * HomeGroupPostContentDao
 * 
 */
@Repository
public class HomeGroupPostContentDao extends BaseDao<HomeGroupPostContent> {

    /**
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageHandler getPage(Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeGroupPostContent bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeGroupPostContent init(HomeGroupPostContent entity) {
        return entity;
    }

}