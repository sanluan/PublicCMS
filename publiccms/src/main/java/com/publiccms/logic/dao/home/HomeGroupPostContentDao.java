package com.publiccms.logic.dao.home;

// Generated 2016-11-19 9:58:46 by com.sanluan.common.source.SourceGenerator


import org.springframework.stereotype.Repository;

import com.publiccms.entities.home.HomeGroupPostContent;
import com.sanluan.common.base.BaseDao;
import com.sanluan.common.handler.PageHandler;
import com.sanluan.common.handler.QueryHandler;

@Repository
public class HomeGroupPostContentDao extends BaseDao<HomeGroupPostContent> {
    public PageHandler getPage(
                Integer pageIndex, Integer pageSize) {
        QueryHandler queryHandler = getQueryHandler("from HomeGroupPostContent bean");
        queryHandler.order("bean.id desc");
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected HomeGroupPostContent init(HomeGroupPostContent entity) {
        return entity;
    }

}